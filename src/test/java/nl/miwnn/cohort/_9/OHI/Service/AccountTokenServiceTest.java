package nl.miwnn.cohort._9.OHI.Service;

import nl.miwnn.cohort._9.OHI.Model.AccountToken;
import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.OHIUser;
import nl.miwnn.cohort._9.OHI.Model.Person;

import nl.miwnn.cohort._9.OHI.Repository.AccountTokenRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
class AccountTokenServiceTest {
    private AccountTokenService accountTokenService;
    private OHIUserService ohiUserService;
    private AccountToken accountToken;
    //private Person person;
    private Cohort cohort;
    private String token = UUID.randomUUID().toString();

    @Mock
    private AccountTokenRespository accountTokenRespository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        accountTokenService = new AccountTokenService(ohiUserService, accountTokenRespository);
        Person person = new Person("Susie","Testgal");
        Person person2 = new Person("Tommy", "Testboi");
        cohort = new Cohort(19,
                             "testing",
                                      LocalDate.of(2020,1,4),
                                      LocalDate.of(2021,4,5));
        person.setCohort(cohort);
        person2.setCohort(cohort);
        OHIUser user = new OHIUser("username",
                                   "password",
                                       "STUDENT",
                                            person);
        accountToken = new AccountToken(token,user, LocalDateTime.now().plusDays(7));
    }

    @Test
    @DisplayName("accountTokenSetup should create token links for members of a cohort")
    void accountTokensSetupShouldCreateTokenLinksForMembersOfACohort() {
        assertNotNull(accountTokenService.accountTokensSetup(cohort));
    }

    @Test
    void validateAndGet() {
    }

    @Test
    void markUsed() {
    }
}