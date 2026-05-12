package nl.miwnn.cohort._9.OHI.Model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

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
    public static final LocalDate DEFAULT_BIRTHDATE = null;
    public static final String DEFAULT_PRONOUN = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Moet een naam hebben")
    @CsvBindByName(column = "firstName")
    private String firstName;

    private String infix;

    @CsvBindByName(column = "lastName")
    @NotNull(message = "Achternaam kan niet leeg zijn")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @Lob
    @Column(name = "about_me")
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "cohort_id")
    private Cohort cohort;

    @Transient
    @CsvBindByName(column = "cohort_id")
    public Long cohortId;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OHIUser account;

    @ManyToMany
    @JoinTable(
            name = "person_images",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")

    )
    private Set<Image> images = new HashSet<>();

    private String location;
    private LocalDate birthDate;
    private String pronoun;

    @OneToOne(cascade = CascadeType.ALL)
    private Student student;

    @Enumerated(EnumType.STRING)
    @CsvBindByName(column = "userRole")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "person_interest",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<Interest> interests = new ArrayList<>();

    public Person(String firstName, String infix, String lastName, Image profileImage, String aboutMe, String location,
                  LocalDate birthDate, String pronoun, Role role) {
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.aboutMe = aboutMe;
        this.role = role;
        this.location = location;
        this.birthDate = birthDate;
        this.pronoun = pronoun;
    }

    public Person(String firstName, String lastName) {
        this(firstName, DEFAULT_INFIX, lastName, DEFAULT_IMAGE, DEFAULT_ABOUTME, DEFAULT_LOCATION,
                DEFAULT_BIRTHDATE, DEFAULT_PRONOUN, DEFAULT_ROLE);
    }

    public Person() {
    }

    public boolean getEmployerField(){
        return role == Role.STUDENT;
    }

    public boolean isPersonBirthday(){
        if (birthDate == null) return false;
        LocalDate today = LocalDate.now();
        return birthDate.getDayOfMonth() == today.getDayOfMonth()
                && birthDate.getMonthValue() == today.getMonthValue();
    }

    public String getFullName() {
        if (infix == null || infix.isEmpty()) {
            return String.format("%s\n%s", firstName, lastName);
        }
        return String.format("%s\n%s %s", firstName, infix, lastName);
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

    public Image getProfileImage() {
        return profileImage;
    }

    public void setImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public Image getImage() {
        return profileImage;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    //todo - see if this alone works
    public String getEnumToLowerCase(Role role) {
        if (role.toString().equals("TEACHER")) {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
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

    public Long getCohortId() {
        return cohortId;
    }

    public void setCohortId(Long cohortId) {
        this.cohortId = cohortId;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public Set<Image> getImages() { return images;
    }

    public void setInterests(List<Interest> interests) {
        this.interests.clear();
        if (interests != null) {
            this.interests.addAll(interests);
        }
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
