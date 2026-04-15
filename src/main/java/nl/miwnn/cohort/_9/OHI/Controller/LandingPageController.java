package nl.miwnn.cohort._9.OHI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Alexander Banic
 * Code for landingpage of OHI.
 */

@Controller
public class LandingPageController {

    @GetMapping("")
    public String redirect () {
        return "redirect:/userlogin";
    }


    @GetMapping("/userlogin")
    public String startpagina() {
        return "landing-page";
    }
}
