package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * @author INT Developers
 * Initialize databade when the application is started empty
 */

@Controller
public class InitializeController {
    private final Logger log = LoggerFactory.getLogger(InitializeController.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OHIUserRepository ohiUserRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CohortRepository cohortRepository;

    public InitializeController(OHIUserRepository ohiUserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.ohiUserRepository = ohiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (cohortRepository.count() == 0) {
            seedCohorts();
        }
        if (personRepository.count() == 0) {
            seedPeople();
        }
        if (ohiUserRepository.count() == 0) {
            seedUsers();
        }
    }

    private void seedCohorts() {
        try {
            ClassPathResource resource = new ClassPathResource("static/cohorts.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());

            CsvToBean<Cohort> csvToBean = new CsvToBeanBuilder<Cohort>(reader)
                    .withType(Cohort.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Cohort> cohorts = csvToBean.parse();

            cohortRepository.saveAll(cohorts);

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }


    private void seedPeople() {
        try {
            ClassPathResource resource = new ClassPathResource("static/people.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());

            CsvToBean<Person> csvToBean = new CsvToBeanBuilder<Person>(reader)
                    .withType(Person.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Person> people = csvToBean.parse();

            personRepository.saveAll(people);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    //todo
    private void seedUsers() {
        if (ohiUserRepository.count() == 0) {
            Person testUser = new Person("Hans", "Hans");
            personRepository.save(testUser);
            OHIUser docent = new OHIUser("docent", passwordEncoder.encode("docent"), "DOCENT");
            OHIUser student = new OHIUser("student", passwordEncoder.encode("student"), "STUDENT");
            OHIUser hans = new OHIUser("hans", passwordEncoder.encode("hans"), "STUDENT", testUser);
            ohiUserRepository.save(docent);
            ohiUserRepository.save(student);
            ohiUserRepository.save(hans);
        }
    }
}
