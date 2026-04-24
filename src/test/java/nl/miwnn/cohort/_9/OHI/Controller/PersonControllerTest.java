package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.InterestRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

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

    //I think the personController class needs to be cleaned up, but doing tests for now
    @Mock
    private PersonRepository personRepository;
    @Mock
    private CohortRepository cohortRepository;
    @Mock
    private InterestRepository interestRepository;


    @Test
    void showPeople() {
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