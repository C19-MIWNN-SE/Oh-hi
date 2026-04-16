package nl.miwnn.cohort._9.OHI.Service;

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
public class PersonService {private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

    public void findInfoPerson(@PathVariable Long id){

        Optional<Person> person = personRepository.findById(id);

    }


    public void showEditOrAddForm(Model model, RedirectAttributes redirectAttributes){

    }

    public void addMemberToCohort(Model model){

    }

    public void deleteMemberFromCohort(Long id){
        personRepository.deleteById(id);
    }

}
