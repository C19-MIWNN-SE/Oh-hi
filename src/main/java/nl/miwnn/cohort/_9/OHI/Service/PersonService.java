package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.Image;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Model.Student;
import nl.miwnn.cohort._9.OHI.Repository.ImageRepository;
import nl.miwnn.cohort._9.OHI.Repository.OHIUserRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

/**
 * @author INT Developers
 * Service layer for Person
 */
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final ImageRepository imageRepository;
    private final StudentRepository studentRepository;
    private final OHIUserRepository ohiUserRepository;
    private Person person;

    public PersonService(PersonRepository personRepository, ImageRepository imageRepository, StudentRepository studentRepository, OHIUserRepository ohiUserRepository) {
        this.personRepository = personRepository;
        this.imageRepository = imageRepository;
        this.studentRepository = studentRepository;
        this.ohiUserRepository = ohiUserRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

    public void saveMemberToCohort(Person person){
        personRepository.save(person);
    }

    public void saveAllPeople(List<Person> people) {
        personRepository.saveAll(people);
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

        // Verwijder gerelateerde user
//        if (person.getOhiUser() != null) {
//            ohiUserRepository.delete(person.getOhiUser());
//        }
        if (person.getStudent() != null) {
            studentRepository.delete(person.getStudent());
        }

        if (person.getImage() != null) {
            imageRepository.delete(person.getImage());
        }

        personRepository.deleteById(id);

    }

    public void savePerson(Person person) { personRepository.save(person);}

    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Persoon %d niet gevonden", id)));
    }

    public Map<String, ?> getProfileInformation(Person person){
        Map<String, Object> information = new HashMap<>();

        information.put("name", String.format("Oh hi %s!", person.getFullName()));
        information.put("aboutMe", person.getAboutMe());
        information.put("location", person.getLocation());
        information.put("age", person.getAge());
        information.put("pronoun", person.getPronoun());
        information.put("userRole", person.getEnumToLowerCase(person.getUserRole()));
        information.put("employer", person.getStudent());
        information.put("person", person);

        return information;
    }

    public Person getPerson(Long personId){
        return personRepository.findById(personId).
                orElseThrow(() -> new IllegalStateException("Geen persoon gevonden"));

    }

    public void updateProfileImage(Long personId, MultipartFile imageFile) throws IOException {

        Person profilePerson = getPerson(personId);

        if (!imageFile.isEmpty()) {
            Image image = new Image();
            image.setData(imageFile.getBytes());
            image.setContentType(imageFile.getContentType());
            imageRepository.save(image);
            profilePerson.setImage(image);
        }
    }

    public void checkIfPersonIsStudent(Person profilePerson, Person aboutPerson){

        if (profilePerson.getEmployerField() && aboutPerson.getStudent() != null) {
            Student student = profilePerson.getStudent();

            if (student == null) {
                student = new Student();
                profilePerson.setStudent(student);
            }

            student.setEmployer(aboutPerson.getStudent().getEmployer());
        }
    }

    public void updatePersonInformation(Long personId, Person aboutPerson){
        Person profilePerson = getPerson(personId);

        profilePerson.setAboutMe(aboutPerson.getAboutMe());
        profilePerson.setLocation(aboutPerson.getLocation());
        profilePerson.setAge(aboutPerson.getAge());
        profilePerson.setPronoun(aboutPerson.getPronoun());

        checkIfPersonIsStudent(profilePerson, aboutPerson);

        personRepository.save(profilePerson);
    }
}
