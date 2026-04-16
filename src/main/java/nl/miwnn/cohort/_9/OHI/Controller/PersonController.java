package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;


import java.util.ArrayList;
import java.util.List;

/**
 * @author INT Developers
 * Controller for Person - how people will be displayed
 */
@Controller
@RequestMapping("profiles")
public class PersonController {
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final CohortRepository cohortRepository;

    public PersonController(PersonRepository personRepository, PersonService personService, CohortRepository cohortRepository) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.cohortRepository = cohortRepository;
    }

@GetMapping("")
public String showPeople(Model model) {
    List<Person> people = personRepository.findAll();
    List<Cohort> cohorts = cohortRepository.findAll();

    log.debug("person overview requested");
    model.addAttribute("people", people);
    model.addAttribute("allCohorts", cohorts);

    return "PersonenOverview";
}


/* Oh hi Mees */
@GetMapping("/add")
public String addPersonToCohort(Model model){
    model.addAttribute("person", new Person());

    return "add-edit-form";
}

@PostMapping("/save")
public String saveMemberToCohort(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, RedirectAttributes redirectAttributes){
    if (personService.personAlreadyExists(person)){
            bindingResult.rejectValue("firstName", "alreadyExists", "Dit persoon bestaat al");
    }

    if (bindingResult.hasErrors()) {
        return "add-edit-form";
    }

    try {
        personService.saveMemberToCohort(person);
    } catch (Exception exception){
        redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
    }

    redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");
    return "redirect:/profiles";
}

@GetMapping("/remove/{id}")
public String deleteMemberFromCohort(@PathVariable Long id, RedirectAttributes redirectAttributes){

    try {
        personService.deleteMemberFromCohort(id);
        redirectAttributes.addFlashAttribute("successMessage",  "Het persoon is succesvol verwijderd");
    } catch (Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", "Het persoon kon niet verwijderd worden.");
    }

    return "redirect:/profiles";
}


@GetMapping("/{id}")
public String showProfile(@PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){

    Person person = personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));

    log.info("De pagina wordt geladen");

    model.addAttribute("name", String.format("Oh hi %s!", person.getFullName()));
    model.addAttribute("aboutMe", person.getAboutMe());
    model.addAttribute("userRole", person.getEnumToLowerCase(person.getUserRole()));
    model.addAttribute("person", person);


    return "PersonProfile";

}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("Bewerkformulier geopend voor: {}", id);
        //Person personToEdit = personRepository.findById(id);

//        return "add-edit-profile";

        return "redirect:/{id}";
}
    @PostMapping("/saveProfile")
    public String saveProfile(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        try {
            personService.saveMemberToCohort(person);
        } catch (Exception exception){
            redirectAttributes.addFlashAttribute("Dit persoon kon niet worden opgeslagen");
        }

        redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");
        return "redirect:/profiles";
    }

}
