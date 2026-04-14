package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
@Service
public class PersonService {private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

}
