package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sara Omlor
 * Controller for cohortclass
 */

@RequestMapping("/cohort")

@Controller
public class CohortController {

    @GetMapping("/add")
    public String addCohort (Model model) {
        model.addAttribute("cohort", new Cohort());
        return ("cohort-add-edit");
    }




}
