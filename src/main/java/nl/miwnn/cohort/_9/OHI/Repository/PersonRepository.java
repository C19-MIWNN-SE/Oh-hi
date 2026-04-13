package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
