package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.Valid;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Model.Role;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.*;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.*;

/**
 * @author INT Development
 * Controller for cohortclass
 */

@RequestMapping("/cohort")
@Controller
public class CohortController {
    private final PersonService personService;
    private final CohortService cohortService;
    private final AccountTokenService accountTokenService;
    private final ImageService imageService;
    private Logger log;

    public CohortController(PersonService personService,
                            CohortService cohortService,
                            AccountTokenService accountTokenService, ImageService imageService) {
        this.personService = personService;
        this.cohortService = cohortService;
        this.accountTokenService = accountTokenService;
        this.imageService = imageService;
    }

    @GetMapping("/add")
    public String addCohort(Model model) {
        model.addAttribute("cohort", new Cohort());
        List<Person> allMembers = personService.getAllPeople();
        model.addAttribute("allMembers", allMembers);
        // CSV lezer toevoegen om csv in te lezen?
        return ("cohort-add-edit");
    }

    //    //todo - check if teacher or docent
//    @PreAuthorize("hasRole('DOCENT')")
    @PostMapping("/save")
    public String saveCohort(@Valid @ModelAttribute Cohort cohort,
                             BindingResult bindingResult,
                             MultipartFile file,
                             Model model, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Selecteer een csv van cohort leden informatie");
            model.addAttribute("status", false);
        } else {
            cohortService.readCSV(cohort, file);
        }
        model.addAttribute("cohort", new Cohort());
        if (bindingResult.hasErrors()) {
            // dit werkt nu ineens niet meer, de bindingresults validatie
//            log.warn("Validatiefouten bij opslaan: {}",
//                    bindingResult.getErrorCount());
            model.addAttribute("allMembers", personService.getAllPeople());
            return "cohort-add-edit";
        }
        cohortService.saveCohort(cohort);
        List<String> setupLinks = accountTokenService.accountTokensSetup(cohort);
        model.addAttribute("setupLink", setupLinks.toString());
        redirectAttributes.addFlashAttribute("setupLinks", setupLinks);
        redirectAttributes.addFlashAttribute("successMessage",
                "Het cohort is succesvol opgeslagen!");
        return ("redirect:/person/overview");
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN') or @cohortService.isMemberOfCohort(#id, authentication)")
    @GetMapping("/{id}")
    public String showCohort(@PathVariable("id") Long id, Model model) {

        Cohort cohort = cohortService.findById(id);
        model.addAttribute("cohort", cohort);
        List<Person> students = personService.getPeopleByRoleAndCohort(Role.STUDENT, id);
        List<Person> teachers = personService.getPeopleByRoleAndCohort(Role.TEACHER, id);
        model.addAttribute("members", cohort.getMembers());
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
        model.addAttribute("cohortName", String.format("Cohort %d - %s", cohort.getCohortNum(), cohort.getDiscipline()));


        model.addAttribute("cohortDate", cohortService.getCohortTimeline(cohort));

        List<Cohort> cohorts = cohortService.getAllCohorts();
        Collections.sort(cohorts);
        model.addAttribute("allCohorts", cohorts);

        Set<Image> images = cohortService.getCohortImages(id);
        model.addAttribute("images", images);

        return "cohort-overview";
    }

    @GetMapping("/{id}/group-photos")
    public String showGroupUploadForm(@PathVariable Long id, Model model) {
        Cohort cohort = cohortService.findById(id);

        model.addAttribute("cohort", cohort);
        model.addAttribute("people", cohort.getMembers());

        return "cohort-upload-group-photo";
    }

    @PostMapping("/{id}/group-photos")
    public String uploadGroupImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("personIds") List<Long> personIds) throws IOException {

        imageService.groupImageUpload(file, personIds);

        return "redirect:/cohort/" + id;
    }

}
