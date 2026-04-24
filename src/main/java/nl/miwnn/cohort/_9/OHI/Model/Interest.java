package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;

/**
 * Author: INT Developers
 * Model about interest of a student or teacher that can pertain to one or more people.
 */

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interest;

    public Interest(Long id, String interest) {
        this.id = id;
        this.interest = interest;
    }

    public Interest() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
