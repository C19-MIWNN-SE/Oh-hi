package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import java.io.IOException;
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
    private final ImageRepository imageRepository;

    public PersonController(PersonRepository personRepository, PersonService personService, CohortRepository cohortRepository, ImageRepository imageRepository) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.cohortRepository = cohortRepository;
        this.imageRepository = imageRepository;
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

    @GetMapping("/add")
    public String addPersonToCohort(Model model) {
        model.addAttribute("person", new Person());

        return "person-add-edit";
    }

    @PostMapping("/save")
    public String saveMemberToCohort(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (personService.personAlreadyExists(person)) {
            bindingResult.rejectValue("firstName", "alreadyExists", "Dit persoon bestaat al");
        }

        if (bindingResult.hasErrors()) {
            return "person-add-edit";
        }

        try {
            personService.saveMemberToCohort(person);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
        }

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

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));

        log.info("De pagina wordt geladen");

        model.addAttribute("name", String.format("Oh hi %s!", person.getFullName()));
        model.addAttribute("aboutMe", person.getAboutMe());
        model.addAttribute("userRole", person.getEnumToLowerCase(person.getUserRole()));
        model.addAttribute("person", person);
        return "person-detail";
    }

    @PreAuthorize("#id == authentication.principal.person.id")
    @GetMapping("/profile/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));
        log.info("Bewerkformulier geopend voor: {}", id);
        model.addAttribute("person", personService.findById(id));
        return "person-profile-edit";
    }

    //Save results of add/edit to about me information
    @PreAuthorize("#id == authentication.principal.person.id")
    @PostMapping("/profile/save")
    public String saveAboutMe(@ModelAttribute Person aboutPerson,
                              @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {

        Person profilePerson = personService.findById(aboutPerson.getId());

        if (aboutPerson.getId() != null) {

            if (!imageFile.isEmpty()) {
                Image image = new Image();
                image.setData(imageFile.getBytes());
                image.setContentType(imageFile.getContentType());
                imageRepository.save(image);
                profilePerson.setImage(image);
            }
        }

        if (profilePerson == null) {
            throw new IllegalStateException("No person found with ID " + aboutPerson.getId());
        }
        profilePerson.setAboutMe(aboutPerson.getAboutMe());
        personService.savePerson(profilePerson);

        return "redirect:/person/" + profilePerson.getId();
    }
}
