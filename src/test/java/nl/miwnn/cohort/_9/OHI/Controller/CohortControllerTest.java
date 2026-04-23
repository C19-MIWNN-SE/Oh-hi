package nl.miwnn.cohort._9.OHI.Controller;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Service.AccountTokenService;
import nl.miwnn.cohort._9.OHI.Service.CohortService;
import nl.miwnn.cohort._9.OHI.Service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author INT Development
 * Testclass for cohort controller
 */
@WebMvcTest(CohortController.class)
class CohortControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CohortService cohortService;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private AccountTokenService accountTokenService;

    @Test
    void addCohort() {
    }

    @Test
    @WithMockUser(roles = "DOCENT")
    @DisplayName("showCohortOVerviewById")
    void showCohortOverviewById() throws Exception {
        // Arrange
        Cohort cohort = new Cohort(1, "Software Engineering",
                LocalDate.of(2026, 04, 01),
                LocalDate.of(2026, 05, 01));
        when(cohortService.findById(1L)).thenReturn(cohort);

        mockMvc.perform(get("/cohort/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("cohort-overview"))
                .andExpect(model().attributeExists("cohort"));
    }

    @Test
    void saveCohort() {
    }

    @Test
    void showCohort() {
    }
}