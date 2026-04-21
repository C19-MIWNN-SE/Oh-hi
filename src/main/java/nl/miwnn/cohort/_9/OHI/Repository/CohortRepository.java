package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
public interface CohortRepository extends JpaRepository<Cohort, Long> {

    Optional<Cohort> findById(Long id);
}
