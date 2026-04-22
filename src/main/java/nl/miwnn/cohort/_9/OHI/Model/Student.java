package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Author: Mees Drenth
 * Model about a student that has a future employer and extends Person
 */

@Entity
//@DiscriminatorValue("STUDENT")
public class Student extends Person{

    private String employer;

    public Student(String firstName, String infix, String lastName, Image image, String aboutMe, String location,
                   Integer age, String pronoun, Role userRole, String employer) {
        super(firstName, infix, lastName, image, aboutMe, location, age, pronoun, userRole);
        this.employer = employer;
    }

    public Student(String firstName, String lastName, String employer) {
        super(firstName, lastName);
        this.employer = employer;
    }

    public Student() {
        super();
    }

    public Student(String employer) {
        this.employer = employer;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
}
