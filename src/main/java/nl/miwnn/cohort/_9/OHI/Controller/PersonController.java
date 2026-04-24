package nl.miwnn.cohort._9.OHI.Controller;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.*;
import nl.miwnn.cohort._9.OHI.Repository.*;
import nl.miwnn.cohort._9.OHI.Service.OHIUserService;
import nl.miwnn.cohort._9.OHI.Service.CohortService;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author INT Developers
 * Controller for Person class
 */
@Controller
@RequestMapping("person")
public class PersonController {
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final CohortRepository cohortRepository;
    private final CohortService cohortService;
    private final ImageRepository imageRepository;
    private final StudentRepository studentRepository;
    private final OHIUserService oHIUserService;
    private final AccountTokenRespository accountTokenRespository;
    private final OHIUserRepository oHIUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PersonController(PersonRepository personRepository, PersonService personService,  CohortRepository cohortRepository, ImageRepository imageRepository, StudentRepository studentRepository, OHIUserService oHIUserService, CohortService cohortService, AccountTokenRespository accountTokenRespository, OHIUserRepository oHIUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.cohortRepository = cohortRepository;
        this.cohortService = cohortService;
        this.imageRepository = imageRepository;
        this.studentRepository = studentRepository;
        this.oHIUserService = oHIUserService;
        this.accountTokenRespository = accountTokenRespository;
        this.oHIUserRepository = oHIUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/overview")
    public String showPeople(Model model) {
        List<Person> people = personRepository.findAll();
        List<Cohort> cohorts = cohortRepository.findAll();

        log.debug("person overview requested");
        model.addAttribute("people", people);
        Collections.sort(cohorts);
        model.addAttribute("allCohorts", cohorts);

        return "person-overview";
    }

    @GetMapping({"/add", "/add/{cohortId}"})
    public String addPersonToCohort(@PathVariable(required = false) Long cohortId, Model model) {
        Person person = new Person();
        if (cohortId != null) {
            Cohort cohort = cohortService.findById(cohortId);
            person.setCohort(cohort);
        }
        model.addAttribute("person", person);
        model.addAttribute("allCohorts", cohortService.getAllCohorts());
        return "person-add-edit";
    }

    @PostMapping("/save")
    public String saveMemberToCohort(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult,
                                     @RequestParam(value = "cohort.id", required = false) Long cohortId,
                                     RedirectAttributes redirectAttributes) {
        if (personService.personAlreadyExists(person)) {
            bindingResult.rejectValue("firstName", "alreadyExists", "Dit persoon bestaat al");
        }

        if (bindingResult.hasErrors()) {
            return "person-add-edit";
        }

        cohortService.addMemberToCohort(person, cohortId);

        try {
            personService.saveMemberToCohort(person);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
        }

        String setupLink = oHIUserService.createAccount(person, "STUDENT");
        redirectAttributes.addFlashAttribute("setupLink", setupLink);
        redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");

        return "redirect:/person/overview";
    }




    @GetMapping("/remove/{id}")
    public String deleteMemberFromCohort(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            personService.deleteMemberFromCohort(id);
            redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol verwijderd");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Het persoon kon niet verwijderd worden.");
        }
        return "redirect:/person/overview";
    }

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model) {

        Person person = personService.getPerson(id);
        model.addAllAttributes(personService.getProfileInformation(person));

        return "person-detail";
    }

    @PreAuthorize("#id == authentication.principal.person.id")
    @GetMapping("/profile/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        personService.getPerson(id);
        log.info("Bewerkformulier geopend voor: {}", id);
        model.addAttribute("person", personService.findById(id));

        return "person-profile-edit";
    }

    //todo - something is wrong with this pre authorization
//    @PreAuthorize("#id == authentication.principal.person.id")
    @PostMapping("/profile/save")
    public String saveAboutMe(@ModelAttribute Person aboutPerson,
                              @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes
    ) throws IOException {

        Person profilePerson = personService.findById(aboutPerson.getId());
        personService.updateProfileImage(aboutPerson.getId(), imageFile);
        personService.updatePersonInformation(aboutPerson.getId(), aboutPerson);

        redirectAttributes.addFlashAttribute("successMessage", "Je profiel is succesvol bijgewerkt!");
        redirectAttributes.addFlashAttribute("errorMessage", "Je profiel kon niet bijgewerkt worden");

        return "redirect:/person/" + profilePerson.getId();
    }

    //todo - add a page for editing login info for the user from the link
    @GetMapping("/account/setup")
    public String UserSetUpAccount(@PathVariable @RequestParam("token") String token, Model model){
        AccountToken accountToken = accountTokenRespository.findByToken(token);
                //.orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if(accountToken.isUsed() || accountToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("token has expired");
        }

        OHIUser newUser = accountToken.getOhiUser();
        model.addAttribute("newUser", newUser);
        model.addAttribute("token", token);
        return "user-account-setup";
    }

    //todo - save/post user updated account info
    @PostMapping("/account/setup")
    public String finishSetup(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String username,
                              @ModelAttribute OHIUser newAccPerson) {
        AccountToken accountToken = accountTokenRespository.findByToken(token);
        //.orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if(accountToken.isUsed() || accountToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("token has expired");
        }

        OHIUser user = accountToken.getOhiUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        oHIUserRepository.save(user);

        accountToken.setUsed(true);
        accountTokenRespository.save(accountToken);

        return "redirect:/";
    }

}
