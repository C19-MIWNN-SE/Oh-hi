package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Model.Student;
import nl.miwnn.cohort._9.OHI.Repository.CohortRepository;
import nl.miwnn.cohort._9.OHI.Repository.InterestRepository;
import nl.miwnn.cohort._9.OHI.Repository.PersonRepository;
import nl.miwnn.cohort._9.OHI.Service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sara Omlor
 * PURPOSE GOES HERE
 */
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;  // verwijder static

    @MockitoBean
    private PersonService personService;

    @MockitoBean  // verwijder @Autowired hier
    private CohortService cohortService;

    @MockitoBean
    private OHIUserService ohiUserService;

    @MockitoBean
    private AccountTokenService accountTokenService;

    @MockitoBean
    private InterestService interestService;


    @Test
    @WithMockUser(roles = "DOCENT")
    @DisplayName("ShowPeople Should return 200 and people in Model")
    void showPeopleShouldReturn200AndPeopleInModel() throws Exception {
        //arrange
        List<Person> people = List.of(
                new Person("Kat", "Dusk"),
                new Person("Simone", "Reeves")
        );
        when(personService.getAllPeople()).thenReturn(people);
        //act and assert
        mockMvc.perform(get("/person/overview"))
                .andExpect(status().isOk())
                .andExpect(view().name("person-overview"))
                .andExpect(model().attributeExists("people"));
    }

    @Test
    @WithMockUser(roles = "DOCENT")
    @DisplayName("addPersonToCohort with a cohortid should return 200 and set the cohort")
    void addPersonToCohortWithCohortIDShouldReturn200AndSetCohort() throws Exception {
        //arrange
        Cohort cohort = new Cohort();
        when(cohortService.findById(2L)).thenReturn(cohort);
        when(cohortService.getAllCohorts()).thenReturn(List.of());
        //act and assert
        mockMvc.perform(get("/person/add/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("person-add-edit"))
                .andExpect(model().attributeExists("person"))
                .andExpect(model().attributeExists("allCohorts"))
                .andExpect(model().attribute("person", hasProperty("cohort", equalTo(cohort))));

        verify(cohortService).findById(2L);
    }
//todo- ask about security needed here? how to test post methods
//    @Test
//    @WithMockUser(roles = "DOCENT")
//    @DisplayName("saveMemberToCohort should return to the form if the person already exists")
//    void saveMemberToCohortShouldReturnTheFormIfPersonAlreadyExists() throws Exception {
//
//        when(personService.personAlreadyExists(any())).thenReturn(true);
//        mockMvc.perform(post("/person/save/")
//                .param("firstName", "Simone")
//                .param("lastName", "Reeves"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("person-add-edit"))
//                .andExpect(model().attributeHasFieldErrors("person","firstName"));
//    }

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

    @Test
    @DisplayName("Student should see employer field on edit form")
    void studentShouldSeeEmployerFieldOnEditForm() throws Exception {
            // arrange
            Student student = new Student();
            student.setEmployer("Hema");

            Person person = new Person("Kat", "Dusk");
            person.setId(1L);
            person.setUserRole(Person.Role.STUDENT);
            person.setStudent(student);

            CustomUserDetails userDetails = new CustomUserDetails(person);

            when(personService.findById(1L)).thenReturn(person);

            // act & assert
            mockMvc.perform(get("/person/profile/edit/{id}", 1L)
                            .with(user(userDetails)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("person-profile-edit"))
                    .andExpect(model().attribute("person", hasProperty("student",
                            hasProperty("employer", is("Hema")))));
        }


        /*Om toegang te krijgen tot de profile edit*/
        static class CustomUserDetails implements UserDetails {
                private final Person person;

                public CustomUserDetails(Person person) {
                    this.person = person;
                }

                public Person getPerson() {
                    return person;
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
                }

                @Override
                public String getPassword() {
                    return "password";
                }

                @Override
                public String getUsername() {
                    return "user";
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
        }


    }

