package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Service.OHIUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alexander Banic
 * Code for landingpage of OHI.
 */

@Controller
public class LandingPageController {
//    OHIUserService ohiUserService;
//
//    public LandingPageController(OHIUserService ohiUserService) {
//        this.ohiUserService = ohiUserService;
//    }

    @GetMapping("")
    public String redirect () {
        return "redirect:/userlogin";
    }


    @GetMapping("/userlogin")
    public String startpagina() {
        return "landing-page";
    }
}
