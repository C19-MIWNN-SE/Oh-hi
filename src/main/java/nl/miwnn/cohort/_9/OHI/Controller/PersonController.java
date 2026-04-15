package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }



@GetMapping("")
public String showPeople(Model model) {
    List<Person> people = personRepository.findAll();

    //people.add(new Person(1L, "Mark", "Sestero"));
    log.debug("person overview requested");
    model.addAttribute("people", people);

    return "PersonenOverview";
}


/* Oh hi Mees */


@GetMapping("/{id}")
public String showProfile(@PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){

    Person person = personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));

    log.info("De pagina wordt geladen");

    model.addAttribute("name", String.format("Oh hi %s!", person.getFullName()));
    model.addAttribute("aboutMe", person.getAboutMe());

    return "PersonProfile";

}
}
