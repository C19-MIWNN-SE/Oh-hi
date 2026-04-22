package nl.miwnn.cohort._9.OHI.Repository;

import nl.miwnn.cohort._9.OHI.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
