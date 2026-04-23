package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author INT Development
 * Service class for account tokens
 */
@Service
public class AccountTokenService {
   private final OHIUserService oHIUserService;

    public AccountTokenService(OHIUserService oHIUserService) {
        this.oHIUserService = oHIUserService;
    }

    public List<String> accountTokensSetup(Cohort cohort) {
        List<String> setupLinks = new ArrayList<>();
        for (Person member : cohort.getMembers()) {
            member.setCohort(cohort);
            String setupLink = oHIUserService.createAccount(member, "STUDENT");
            setupLinks.add(member.getFullName() + ": " + setupLink);
        }
        return setupLinks;
    }
}
