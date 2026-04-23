package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.AccountToken;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
public interface AccountTokenRespository extends JpaRepository<AccountToken, Long> {
    AccountToken findByToken(String token);
}
