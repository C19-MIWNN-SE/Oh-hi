package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.OHIUserService;
import nl.miwnn.cohort._9.OHI.Service.CohortService;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
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
    private final OHIUserService oHIUserService;
    private final CohortService cohortService;
    private Logger log;

    public CohortController( PersonService personService, OHIUserService oHIUserService, CohortService cohortService) {
        this.personService = personService;
        this.cohortService = cohortService;
        this.oHIUserService = oHIUserService;
    }

    @GetMapping("/add")
    public String addCohort( Model model) {
        model.addAttribute("cohort", new Cohort());
        List<Person> allMembers = personService.getAllPeople();
        model.addAttribute("allMembers", allMembers);
        // CSV lezer toevoegen om csv in te lezen?
        return ("cohort-add-edit");
    }

//    //todo - check if teacher or docent
//    @PreAuthorize("hasRole('DOCENT')")
    @PostMapping("/save")
    public String saveCohort(@Valid @ModelAttribute Cohort cohort, BindingResult bindingResult, MultipartFile file,
                              Model model, RedirectAttributes redirectAttributes) {

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

        cohortRepository.save(cohort);
        List<String> setupLinks = new ArrayList<>();

        for (Person member : cohort.getMembers()) {
            member.setCohort(cohort);
            String setupLink = oHIUserService.createAccount(member, "STUDENT");
            setupLinks.add(member.getFullName() + ": " + setupLink);
        }

        model.addAttribute("setupLink", setupLinks.toString());
        redirectAttributes.addFlashAttribute("setupLinks", setupLinks);
        redirectAttributes.addFlashAttribute("successMessage", "Het persoon is succesvol opgeslagen!");
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
