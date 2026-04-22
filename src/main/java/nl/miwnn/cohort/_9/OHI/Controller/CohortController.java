package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Service.CohortService;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * @author INT Development
 * Controller for cohortclass
 */

@RequestMapping("/cohort")
@Controller
public class CohortController {
    private final PersonService personService;
    private final CohortService cohortService;
    private Logger log;

    public CohortController(PersonService personService, CohortService cohortService) {
        this.personService = personService;
        this.cohortService = cohortService;
    }

    @GetMapping("/add")
    public String addCohort( Model model) {
        model.addAttribute("cohort", new Cohort());
        List<Person> allMembers = personService.getAllPeople();
        model.addAttribute("allMembers", allMembers);
        // CSV lezer toevoegen om csv in te lezen?
        return ("cohort-add-edit");
    }

    @PostMapping("/save")
    public String saveCohort(@Valid @ModelAttribute Cohort cohort, BindingResult bindingResult, MultipartFile file,  Model model) {

        if (file.isEmpty()) {
            model.addAttribute("message", "Selecteer een csv van cohort leden informatie");
            model.addAttribute("status", false);
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                CsvToBean<Person> csvToBean = new CsvToBeanBuilder<Person>(reader)
                        .withType(Person.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<Person> people = csvToBean.parse();
                personService.saveAllPeople(people);
                cohort.setMembers(people);

                //todo - make more specific
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        model.addAttribute("cohort", new Cohort());
        if (bindingResult.hasErrors()) {
            log.warn("Validatiefouten bij opslaan: {}",
                    bindingResult.getErrorCount());
            List<Person> allMembers = personService.getAllPeople();

            model.addAttribute("allMembers", allMembers);
            return "cohort-add-edit";
        }

        for (Person member : cohort.getMembers()) {
            member.setCohort(cohort);
        }

        cohortService.saveCohort(cohort);
        return ("redirect:/person/overview");
    }

    @GetMapping("/{id}")
    public String showCohort(@PathVariable Long id, Model model){

        Cohort cohort = cohortService.findById(id);
        model.addAttribute("cohort", cohort);
        model.addAttribute("members", cohort.getMembers());
        model.addAttribute("cohortName", String.format("Cohort %d - %s", cohort.getCohortNum(), cohort.getDiscipline()));

        List<Cohort> cohorts = cohortService.getAllCohorts();
        Collections.sort(cohorts);
        model.addAttribute("allCohorts", cohorts);

        return "cohort-overview";
    }
}
