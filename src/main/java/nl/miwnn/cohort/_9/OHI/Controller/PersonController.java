package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
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


//@GetMapping("/1")
//public String showProfile(Model model, RedirectAttributes redirectAttributes){
//
////    Person person = personRepository.findById(id)
////            .orElseThrow(() -> new IllegalArgumentException("Persoon bestaat niet " + id));
//
//    List<Person> people = new ArrayList<>();
//    Person Mark = new Person(4L, "Mark", null, "Sestero", null,
//            "I used to know a girl, she had a dozen guys. One of 'em found out about it..." +
//                    "beat her up so bad she ended up in a hospital on Guerrero Street");
//
//    log.info("De pagina wordt geladen");
//
//    model.addAttribute("introText", "Oh hi " + Mark.getFirstName() + "!");
//    model.addAttribute("aboutMe", Mark.getAboutMe());
//
//
//
//
//    return "PersonProfile";
//
//}
}
