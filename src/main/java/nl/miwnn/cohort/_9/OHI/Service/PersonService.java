package nl.miwnn.cohort._9.OHI.Service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort._9.OHI.Model.*;
import nl.miwnn.cohort._9.OHI.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final InterestRepository interestRepository;
    private final ImageService imageService;

    public PersonService(PersonRepository personRepository, ImageRepository imageRepository, StudentRepository studentRepository, OHIUserRepository ohiUserRepository, InterestRepository interestRepository, ImageService imageService) {
        this.personRepository = personRepository;
        this.imageRepository = imageRepository;
        this.studentRepository = studentRepository;
        this.ohiUserRepository = ohiUserRepository;
        this.interestRepository = interestRepository;
        this.imageService = imageService;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPeople(){
        return personRepository.findAll();
    }

    public List<Person> getPeopleByRole(Role role){
        return personRepository.findByRole(role);
    }

    public List<Person> getPeopleByRoleAndCohort(Role role, Long cohortId){
        return personRepository.findByRoleAndCohort_Id(role, cohortId);
    }

    public List<Person> getAllPeopleSortedByCohortAvailability() {
        return getAllPeople().stream()
                .sorted(Comparator.comparing(p -> p.getCohort() == null))
                .toList();
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

    @Transactional
    public void removePerson(Long id){
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
        information.put("userRole", person.getEnumToLowerCase(person.getRole()));
        information.put("employer", person.getStudent());
        information.put("interests", person.getInterests());
        information.put("person", person);

        return information;
    }

    public Person getPerson(Long personId){
        return personRepository.findById(personId).
                orElseThrow(() -> new IllegalStateException("Geen persoon gevonden"));

    }

    public void updateProfileImage(Long personId, MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return;
        }

        Image savedImage = imageService.storeImage(file);

        Person personUpdating = personRepository.findById(personId).orElseThrow();
        personUpdating.setProfileImage(savedImage);

        personRepository.save(personUpdating);
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
        profilePerson.setBirthDate(aboutPerson.getBirthDate());
        profilePerson.setPronoun(aboutPerson.getPronoun());
        profilePerson.setInterests(getSelectedInterests(aboutPerson));
        checkIfPersonIsStudent(profilePerson, aboutPerson);

        personRepository.save(profilePerson);
    }

    public List<Interest> getSelectedInterests(Person aboutPerson){
        return interestRepository.findAllById(aboutPerson.getInterests().stream()
                .map(Interest::getId)
                .collect(Collectors.toList()));

    }

    public void linkAccountToPerson(Person person, OHIUser user) {
        person.setAccount(user);
        personRepository.save(person);
    }


}
