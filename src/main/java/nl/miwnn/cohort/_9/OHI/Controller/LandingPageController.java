package nl.miwnn.cohort._9.OHI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alexander Banic
 * Code for landingpage of OHI.
 */

@Controller
public class LandingPageController {

    @GetMapping("")
    public String startpagina() {
        return "landing-page";
    }
}
