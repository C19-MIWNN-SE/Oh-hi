package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.InterestRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private CohortService cohortService;

    @MockitoBean
    private OHIUserService ohiUserService;

    @MockitoBean
    private AccountTokenService accountTokenService;

    @MockitoBean
    private InterestService interestService;


    @Test
    @DisplayName("ShowPeople Should return ")
    void showPeople() {
        List<Person> people = List.of(
                new Person("Kat", "Dusk"),
                new Person("Simone", "Reeves")
        );
        when(personService.getAllPeople()).thenReturn(people);

        mockMvc.perform(get("person-overview"))
                .andExpect()
    }

    @Test
    void addPersonToCohort() {
    }

    @Test
    void saveMemberToCohort() {
    }

    @Test
    void deleteMemberFromCohort() {
    }

    @Test
    void showProfile() {
    }

    @Test
    void showEditForm() {
    }

    @Test
    void saveAboutMe() {
    }

    @Test
    void userSetUpAccount() {
    }

    @Test
    void finishSetup() {
    }
}