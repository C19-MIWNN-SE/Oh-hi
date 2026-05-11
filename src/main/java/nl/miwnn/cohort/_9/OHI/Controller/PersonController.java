package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.*;
import nl.miwnn.cohort._9.OHI.Repository.*;
import nl.miwnn.cohort._9.OHI.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PersonService personService;
    private final CohortService cohortService;
    private final OHIUserService oHIUserService;
    private final AccountTokenService accountTokenService;
    private final InterestService interestService;

    public PersonController(PersonService personService, OHIUserService oHIUserService,
                            CohortService cohortService, AccountTokenService accountTokenService,
                            InterestService interestService) {
        this.personService = personService;
        this.cohortService = cohortService;
        this.oHIUserService = oHIUserService;
        this.accountTokenService = accountTokenService;
        this.interestService = interestService;
    }

    @GetMapping("/overview")
    public String showPeople(Model model) {
        List<Person> students = personService.getPeopleByRole(Role.STUDENT);
        List<Person> teachers = personService.getPeopleByRole(Role.TEACHER);
        List<Person> people = personService.getAllPeople();
        List<Cohort> cohorts = cohortService.getAllCohorts();

        log.debug("person overview requested");
        model.addAttribute("people", people);
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
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
    public String showProfile(@PathVariable Long id, Model model,
                              @AuthenticationPrincipal OHIUser loggedInUser) {

        Person person = personService.getPerson(id);
        model.addAllAttributes(personService.getProfileInformation(person));

        model.addAttribute("commentWriter", String.format("Schrijf een bericht als %s",
                oHIUserService.getNameOfPerson(loggedInUser)));

        model.addAttribute("loggedIn", loggedInUser.getPerson());

        return "person-detail";
    }

    @PreAuthorize("#id == authentication.principal.person.id")
    @GetMapping("/profile/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Person person = personService.findById(id);
        log.info("Bewerkformulier geopend voor: {}", id);

        model.addAttribute("person", person);
        model.addAttribute("allInterests", interestService.getAllInterests());

        return "person-profile-edit";
    }
    
    @PostMapping("/profile/save")
    public String saveAboutMe(@ModelAttribute Person aboutPerson,
                              @RequestParam("profileImageFile") MultipartFile profileImageFile, RedirectAttributes redirectAttributes
    ) throws IOException {

        Person profilePerson = personService.findById(aboutPerson.getId());
        try {
            personService.updateProfileImage(aboutPerson.getId(), profileImageFile);
            personService.updatePersonInformation(aboutPerson.getId(), aboutPerson);
            redirectAttributes.addFlashAttribute("successMessage", "Je profiel is succesvol bijgewerkt!");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Je profiel kon niet bijgewerkt worden");
        }

        return "redirect:/person/" + profilePerson.getId();
    }

    @GetMapping("/account/setup")
    public String UserSetUpAccount(@PathVariable @RequestParam("token") String token, Model model) {
        AccountToken accountToken = accountTokenService.validateAndGet(token);
        OHIUser newUser = accountToken.getOhiUser();
        model.addAttribute("newUser", newUser);
        model.addAttribute("token", token);
        return "user-account-setup";
    }

    @PostMapping("account/setup")
    public String finishSetup(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String username) {

        AccountToken accountToken = accountTokenService.validateAndGet(token);

        OHIUser user = accountToken.getOhiUser();
        oHIUserService.updateCredentials(user, username, password);

        accountTokenService.markUsed(accountToken);

        return "redirect:/";

    }

}
