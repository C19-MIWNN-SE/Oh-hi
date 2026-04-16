package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.management.relation.Role;

/**
 * Author: INT-developers
 * Model of a person that can consist of either a teacher or student.
 */

@Entity
public class Person {

    //defaults
    private static final String DEFAULT_INFIX = null;
    private static final Image DEFAULT_IMAGE = null;
    private static final String DEFAULT_ABOUTME = null;
    private static final Role DEFAULT_ROLE = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Moet een naam hebben")
    private String firstName;

    private String infix;

    @NotNull(message = "Achternaam kan niet leeg zijn")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    private String aboutMe;

    private enum Role {
        STUDENT,
        TEACHER
    }

    private Role userRole;

    @ManyToOne
    @JoinColumn(name = "cohort_id")
    private Cohort cohort;

    public Person(String firstName, String infix, String lastName, Image image, String aboutMe, Role userRole) {
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.image = image;
        this.aboutMe = aboutMe;
        this.userRole = userRole;
    }

    public Person(String firstName, String lastName) {
        this(firstName, DEFAULT_INFIX, lastName, DEFAULT_IMAGE, DEFAULT_ABOUTME, DEFAULT_ROLE);
    }

    public Person() {
    }

    public String getFullName() {
        if (infix == null) {
            infix = "";
        }

        return String.format("%s %s %s", firstName, infix, lastName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setUserRole(Role value) {
        this.userRole = value;
    }

    public Role getUserRole() {
        return userRole;
    }

    public Cohort getCohort() {
        return cohort;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }
}
