package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author INT Development
 * Service class for cohorts
 */
@Service
public class CohortService {
    private final CohortRepository cohortRepository;

    public CohortService(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

//    @Transactional(readOnly = true)
    public List<Cohort> getAllCohorts(){
        return cohortRepository.findAll();
    }
    public Cohort findById(Long id) {
        return cohortRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cohort %d niet gevonden", id)));
    }

    public void saveCohort(Cohort cohort) {
        cohortRepository.save(cohort);
    }
}
