package nl.miwnn.cohort._9.OHI.Controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author INT Development
 * Code for welcome and login page for OHI.
 */

@Controller
public class WelcomeLoginController {

    @GetMapping("")
    public String welcomeLoginPAge() {
        return "welcome-login";
    }

    @GetMapping("/login-redirect")
    public String loginRedirect(HttpServletRequest request, Authentication authentication) {
        OHIUser user = (OHIUser) authentication.getPrincipal();
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/person/overview";
        } else if (request.isUserInRole("ROLE_STUDENT")) {
            return "redirect:/cohort/" + user.getPerson().getCohort().getId();
        } else if (request.isUserInRole("ROLE_DOCENT")) {
            return "redirect:/person/overview";
        } return "welcome-login";
    }
}
