package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final CohortRepository cohortRepository;
    private Logger log;

    public CohortController(PersonRepository personRepository, PersonService personService, CohortRepository cohortRepository) {
        this.personRepository = personRepository;
        this.personService = personService;
        this.cohortRepository = cohortRepository;
    }

    @GetMapping("/add")
    public String addCohort(Model model) {
        model.addAttribute("cohort", new Cohort());
        List<Person> allMembers = personRepository.findAll();
        model.addAttribute("allMembers", allMembers);
        // Lijst van studenten uit repository halen om toe te voegen?
        // CSV lezer toevoegen om csv in te lezen?
        return ("cohort-add-edit");
    }

    @PostMapping("/save")
    public String saveCohort(@Valid @ModelAttribute Cohort cohort, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.warn("Validatiefouten bij opslaan: {}",
                    bindingResult.getErrorCount());
            List<Person> allMembers = personRepository.findAll();
            model.addAttribute("allMembers", allMembers);
            return "cohort-add-edit";
        }

        cohortRepository.save(cohort);
        return ("redirect:/person/overview");
    }


}
