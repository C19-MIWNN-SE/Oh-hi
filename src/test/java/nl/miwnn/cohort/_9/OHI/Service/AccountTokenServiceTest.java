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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
class AccountTokenServiceTest {
    private AccountTokenService accountTokenService;
    private OHIUserService ohiUserService;
    private AccountToken accountToken;
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
    @DisplayName("validateAndGet check if a token value exists in the repository and return token if Valid")
    void validateAndGetShouldCheckiftokeninAccountTokenRepoAndReturnTokenIfValid() {
        //arrange
        accountToken.setUsed(false);
        accountToken.setExpiresAt(LocalDateTime.now().plusDays(3));

        when(accountTokenRespository.findByToken("test")).thenReturn(accountToken);
        //act
        AccountToken result = accountTokenService.validateAndGet("test");

        //assert
        assertEquals(accountToken, result);

    }

    @Test
    @DisplayName("validateAndGet should throw an exception when the token is not found")
    void validateAndGetShouldThrowAnExceptionWhenTheTokenIsNotFound(){
        when(accountTokenRespository.findByToken("test2")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, ()-> accountTokenService.validateAndGet("test2"));
    }

    @Test
    @DisplayName("validateAndGet should throw an exception if the token has already been used")
    void validateAndGetShouldThrowExceptionIfTokenUsed(){
        //arrange
        accountToken.setUsed(true);
        accountToken.setExpiresAt(LocalDateTime.now().plusDays(3));
       //act
        when(accountTokenRespository.findByToken("test3")).thenReturn(accountToken);
        //assert
        assertThrows(IllegalArgumentException.class, ()-> accountTokenService.validateAndGet("test3"));

    }

    @Test
    @DisplayName("validateAndGet should throw an exception if the token is expired")
    void validateAndGetShouldThrowExceptionifTokenExpired(){
        //arrange
        accountToken.setUsed(false);
        accountToken.setExpiresAt(LocalDateTime.now());
       //act
        when(accountTokenRespository.findByToken("test4")).thenReturn(accountToken);
        //assert
        assertThrows(IllegalArgumentException.class, ()-> accountTokenService.validateAndGet("test4"));
    }


    @Test
    @DisplayName("markUsed should mark a token as having been used and save it to the repository")
    void markUsedShouldMarkATokenUsedAndSaveToAccountTokenRepo() {
        //arrange
        accountToken.setUsed(false);
        //act
        accountTokenService.markUsed(accountToken);
        //assert
        assertTrue(accountToken.isUsed(), "Token should be used");
        verify(accountTokenRespository).save(accountToken);

    }

}