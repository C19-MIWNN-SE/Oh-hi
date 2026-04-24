package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.AccountToken;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.AccountTokenRespository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author INT Development
 * Service class for account tokens
 */
@Service
public class AccountTokenService {
   private final OHIUserService oHIUserService;
   private final AccountTokenRespository accountTokenRespository;

    public AccountTokenService(OHIUserService oHIUserService, AccountTokenRespository accountTokenRespository) {
        this.oHIUserService = oHIUserService;
        this.accountTokenRespository = accountTokenRespository;
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

    public AccountToken validateAndGet(String tokenVal){
        AccountToken token = accountTokenRespository.findByToken(tokenVal);

        if(token == null || token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Deze token is niet meer geldig");
        }
        return token;
    }

    public void markUsed(AccountToken token){
        token.setUsed(true);
        accountTokenRespository.save(token);
    }

}
