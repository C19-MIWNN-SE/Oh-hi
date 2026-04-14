package nl.miwnn.cohort._9.OHI.Model;

import jakarta.persistence.*;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

/**
 * Author: INT-Developers
 * Model about cohort and what type it is
 */

@Entity
public class Cohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull(message = "Moet een cohort bevatten")
    private String cohortNum;

    //@NotNull(message = "Cohort moet een vak behandelen")
    private String discipline;

    public Cohort(String cohortNum, String discipline) {
        this.cohortNum = cohortNum;
        this.discipline = discipline;
    }

    public Cohort(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCohortNum() {
        return cohortNum;
    }

    public void setCohortNum(String cohortNum) {
        this.cohortNum = cohortNum;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
