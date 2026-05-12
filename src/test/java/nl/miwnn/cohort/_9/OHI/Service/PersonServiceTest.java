package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Model.Role;
import nl.miwnn.cohort._9.OHI.Model.Student;
import nl.miwnn.cohort._9.OHI.Repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Author: Mees Drenth
 * Uitleg
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private InterestRepository interestRepository;  // dit mist!

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private OHIUserRepository ohiUserRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("Teacher should not get employer set")
    void checkIfPersonIsStudentShouldNotSetEmployerWhenPersonIsTeacher() {
        // arrange
        Person profilePerson = new Person("Robert", "Jan");
        profilePerson.setRole(Role.TEACHER);

        Student incomingStudent = new Student();
        incomingStudent.setEmployer("Hema");

        Person aboutPerson = new Person("Robert", "Jan");
        aboutPerson.setStudent(incomingStudent);

        // act
        personService.checkIfPersonIsStudent(profilePerson, aboutPerson);

        // assert
        assertNull(profilePerson.getStudent()); // teacher krijgt nooit een student object
    }

    private void assertNull(Student student) {
    }

    @Test
    @DisplayName("updatePersonInformation should update location on person")
    void updatePersonInformationShouldUpdateLocation() {
        // arrange
        Person profilePerson = new Person("Mark", "Anthony");
        profilePerson.setId(1L);
        profilePerson.setRole(Role.STUDENT);

        Person aboutPerson = new Person("Mark", "Anthony");
        aboutPerson.setLocation("Groningen");
        aboutPerson.setInterests(List.of());

        when(personRepository.findById(1L)).thenReturn(Optional.of(profilePerson));

        // act
        personService.updatePersonInformation(1L, aboutPerson);

        // assert
        assertThat(profilePerson.getLocation(), is("Groningen"));
        verify(personRepository).save(profilePerson);
    }
}
