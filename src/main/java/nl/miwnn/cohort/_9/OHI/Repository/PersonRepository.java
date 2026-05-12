package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Cohort;
import nl.miwnn.cohort._9.OHI.Model.Person;
import nl.miwnn.cohort._9.OHI.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByFirstNameAndInfixAndLastName(String firstName, String infix, String lastName);
    Optional<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);

    Person getPersonById(Long id);

    List<Person> findByRole(Role role);
    List<Person> findByRoleAndCohort_Id(Role role, Long cohortId);

    int countPeopleByFirstName(String firstName);
}


