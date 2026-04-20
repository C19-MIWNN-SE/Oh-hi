package nl.miwnn.cohort._9.OHI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author INT Development
 * Code for welcome and login page for OHI.
 */

@Controller
public class WelcomeLoginController {

//    @GetMapping("")
//    public String redirect () {
//        return "redirect:/-welcome-login";
//    }


    @GetMapping("")
    public String startpagina() {
        return "welcome-login";
    }
}
