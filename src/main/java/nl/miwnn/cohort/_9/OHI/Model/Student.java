package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;

/**
 * Author: INT Developers
 * Model about a student that has a future employer
 */

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employer;

    public Student() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmployer() { return employer; }

    public void setEmployer(String employer) { this.employer = employer; }
}