package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Sara Omlor
 * Controller for cohortclass
 */

@RequestMapping("/cohort")

@Controller
public class CohortController {

    private final PersonRepository personRepository;
    private final PersonService personService;

    public CohortController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping("/add")
    public String addCohort (Model model) {
        model.addAttribute("cohort", new Cohort());
        List<Person> allMembers = personRepository.findAll();
        model.addAttribute("allMembers", allMembers);
        // Lijst van studenten uit repository halen om toe te voegen?
        // CSV lezer toevoegen om csv in te lezen?
        return ("cohort-add-edit");
    }




}
