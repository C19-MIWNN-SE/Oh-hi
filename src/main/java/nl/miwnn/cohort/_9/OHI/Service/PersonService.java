package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author INT Developers
 * Service layer for Person
 */
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private Person person;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

//    public void findInfoPerson(@PathVariable Long id){
//
//        Optional<Person> person = personRepository.findById(id);
//    }

    public void showEditOrAddForm(Model model, RedirectAttributes redirectAttributes){
    }

    public void saveMemberToCohort(Person person){
        personRepository.save(person);
    }

        public boolean personAlreadyExists(Person person) {
            if (person.getInfix() == null || person.getInfix().isEmpty()) {
                return personRepository.findPersonByFirstNameAndLastName(
                        person.getFirstName(), person.getLastName()).isPresent();
            }
            return personRepository.findPersonByFirstNameAndInfixAndLastName(
                    person.getFirstName(), person.getInfix(), person.getLastName()).isPresent();
        }

    public void deleteMemberFromCohort(Long id){
        personRepository.deleteById(id);
    }

//    public Object findById() {
//        return null;
//    }

    public void savePerson(Person person) { personRepository.save(person);}

    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Persoon %d niet gevonden", id)));
    }
}
