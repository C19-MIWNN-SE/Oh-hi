package nl.miwnn.cohort._9.OHI.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * @author INT Development
 * Service class for cohorts
 */
@Service
public class CohortService {
    private final CohortRepository cohortRepository;
    private final PersonRepository personRepository;

    public CohortService(CohortRepository cohortRepository, PersonRepository personRepository) {
        this.cohortRepository = cohortRepository;
        this.personRepository = personRepository;
    }

//    @Transactional(readOnly = true)
    public List<Cohort> getAllCohorts(){
        return cohortRepository.findAll();
    }

    public Cohort findById(Long id) {
        return cohortRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cohort %d niet gevonden", id)));
    }

    public void readCSV(Cohort cohort, MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<Person> csvToBean = new CsvToBeanBuilder<Person>(reader)
                    .withType(Person.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Person> people = csvToBean.parse();
            personRepository.saveAll(people);
            cohort.setMembers(people);

            //todo - make more specific
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCohort(Cohort cohort) {
        cohortRepository.save(cohort);
    }

    public void addMemberToCohort(Person person, Long cohortId){
        if (cohortId != null) {
            Cohort cohort = findById(cohortId);
            person.setCohort(cohort);
        } else {
            person.setCohort(null);
        }
    }
}
