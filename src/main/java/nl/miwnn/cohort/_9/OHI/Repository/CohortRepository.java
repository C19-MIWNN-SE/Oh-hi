package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
public interface CohortRepository extends JpaRepository<Cohort, Long> {

    Optional<Cohort> findById(Long id);

//    boolean existsByIdAndMembers_Id(Long cohortId, Long personId);

    @Query("SELECT COUNT(c) > 0 FROM Cohort c JOIN c.members m WHERE c.id = :cohortId AND m.id = :personId")
    boolean existsByIdAndMembers_Id(@Param("cohortId") Long cohortId, @Param("personId") Long personId);
}
