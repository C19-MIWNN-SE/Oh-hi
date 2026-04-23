package nl.miwnn.cohort._9.OHI.Model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.management.relation.Role;

/**
 * Author: INT-developers
 * Model of a person that can consist of either a teacher or student.
 */

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "dtype")
//@DiscriminatorValue("Person")
public class Person {

    private static final String DEFAULT_INFIX = null;
    private static final Image DEFAULT_IMAGE = null;
    private static final String DEFAULT_ABOUTME = null;

    private static final Role DEFAULT_ROLE = Role.STUDENT;
    public static final String DEFAULT_LOCATION = null;
    public static final Integer DEFAULT_AGE = null;
    public static final String DEFAULT_PRONOUN = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Moet een naam hebben")
    private String firstName;

    private String infix;

    @NotNull(message = "Achternaam kan niet leeg zijn")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    @Column(name = "about_me")
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "cohort_id")
    private Cohort cohort;

//    @Transient
//    @CsvBindByName(column = "cohort_id")
//    private Long cohortId;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OHIUser account;

    //todo review this - separate enum roles from a method to format the text
    public enum Role {
        STUDENT("Student"),
        TEACHER("Docent");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private String location;
    private Integer age;
    private String pronoun;

    @OneToOne(cascade = CascadeType.ALL)
    private Student student;

    private Role userRole;

    public Person(String firstName, String infix, String lastName, Image image, String aboutMe, String location,
                  Integer age, String pronoun, Role userRole) {
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.image = image;
        this.aboutMe = aboutMe;
        this.userRole = userRole;
        this.location = location;
        this.age = age;
        this.pronoun = pronoun;
    }

    public Person(String firstName, String lastName) {
        this(firstName, DEFAULT_INFIX, lastName, DEFAULT_IMAGE, DEFAULT_ABOUTME, DEFAULT_LOCATION,
                DEFAULT_AGE, DEFAULT_PRONOUN, DEFAULT_ROLE);
    }

    public Person() {
    }

    public boolean getEmployerField(){
        return userRole == Role.STUDENT;
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

    //todo - see if this alone works
    public String getEnumToLowerCase(Role role) {
        if (userRole.toString().equals("TEACHER")) {
            return "Docent";
        } else return "Student";
    }

    public Cohort getCohort() {
        return cohort;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public OHIUser getAccount() {
        return account;
    }

    public void setAccount(OHIUser account) {
        this.account = account;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
