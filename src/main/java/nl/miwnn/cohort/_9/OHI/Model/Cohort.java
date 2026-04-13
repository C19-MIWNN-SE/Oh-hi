package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Mees Drenth
 * Uitleg
 */

@Entity
public class Cohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Moet een cohort bevatten")
    private String group;

    @NotNull(message = "Cohort moet een vak behandelen")
    private String discipline;

    public Cohort(Long id, String group, String discipline) {
        this.id = id;
        this.group = group;
        this.discipline = discipline;
    }

    public Cohort(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
