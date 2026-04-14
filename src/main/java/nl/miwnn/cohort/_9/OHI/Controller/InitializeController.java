package nl.miwnn.cohort._9.OHI.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
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

    private final PersonRepository personRepository;

    public InitializeController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

   @EventListener(ContextRefreshedEvent.class)
    public void seed(){
        if(personRepository.count() == 0){
            seedPeople();
        }
    }

    private void seedPeople() {
        try {
            //later load this in via variable from the docent upload file
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

}
