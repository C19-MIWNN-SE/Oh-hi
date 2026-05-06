package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: INT Developers
 * Model about images pertaining to either the student or teacher
 */

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    private String filename;
    private String url;
    private String alttxt;

    @ManyToMany(mappedBy = "images")
    private Set<Person> people = new HashSet<>();

    private String contentType;

    public boolean isGroupPhoto() {
        return people.size() > 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFilename(String filename) { this.filename = filename;
    }

    public void setUrl(String url) { this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public String getUrl() {
        return url;
    }

    public String getAlttxt() {
        return alttxt;
    }

    public void setAlttxt(String alttxt) {
        this.alttxt = alttxt;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
}
