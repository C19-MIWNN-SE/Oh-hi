package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author INT Development
 * Testclass for userservice
 */
@ExtendWith(MockitoExtension.class)
class OHIUserServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private OHIUserService ohiUserService;

    @Test
    void testUsernameCreationValidation() {
        // Arrange
        Person mockPerson = new Person("Johnny", "Wiseau");
        // Als code x van repository wordt uitgevoerd, geef dan getal y terug (mocking)
        when(personRepository.countPeopleByFirstName("Johnny")).thenReturn(1);
        String expectedUsername = "Johnny2";

        // Act
        String actualUsername = ohiUserService.usernameCreation(mockPerson);

        // Arrange
        Person mockPerson1 = new Person("Tohnny", "Wiseau");
        when(personRepository.countPeopleByFirstName("Tohnny")).thenReturn(5);
        String expectedUsername1 = "Tohnny6";

        // Act
        String actualUsername1 = ohiUserService.usernameCreation(mockPerson1);

        // Assert
        assertEquals(expectedUsername, actualUsername);

        assertEquals(expectedUsername1, actualUsername1);
    }

    @Test
    void testUserPersonLink() {
        // Arrange
        Person expectedPerson = new Person("Johnny", "Wiseau");

        // Act
        OHIUser actualUser = new OHIUser("user1", "pw1", "STUDENT", expectedPerson);

        // Assert
        assertEquals(expectedPerson, actualUser.getPerson());
    }

    @Test
    void updateCredentials() {
    }
}