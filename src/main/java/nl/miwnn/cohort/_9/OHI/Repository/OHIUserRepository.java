package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alexander Banic
 * Repository for saving users
 */
public interface OHIUserRepository extends JpaRepository<OHIUser, Long> {
    Optional<OHIUser> findByUsername(String username);
}
