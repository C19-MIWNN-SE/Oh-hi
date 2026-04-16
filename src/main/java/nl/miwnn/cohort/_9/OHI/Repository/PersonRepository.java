package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByFirstNameAndInfixAndLastName(String firstName, String infix, String lastName);
    Optional<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);

    Person getPersonById(Long id);
}
