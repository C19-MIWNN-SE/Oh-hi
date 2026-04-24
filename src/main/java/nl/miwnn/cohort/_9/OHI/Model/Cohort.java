package nl.miwnn.cohort._9.OHI.Model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

/**
 * Author: INT-Developers
 * Model about cohort and what type it is
 */

@Entity
public class Cohort implements Comparable<Cohort> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @NotNull(message = "Moet een cohortnummer bevatten")
    @CsvBindByName(column = "cohortNum")
    private Integer cohortNum;

    //    @NotNull(message = "Cohort moet een vak behandelen")
    @CsvBindByName(column = "discipline")
    private String discipline;

    @CsvDate("yyyy-MM-dd")
    @CsvBindByName(column = "startDate")
    private LocalDate startDate;

    @CsvDate("yyyy-MM-dd")
    @CsvBindByName(column = "endDate")
    private LocalDate endDate;

    @OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Person> members = new ArrayList<>();

    public Cohort(Integer cohortNum, String discipline, LocalDate startDate, LocalDate endDate) {
        this.cohortNum = cohortNum;
        this.discipline = discipline;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Cohort() {
    }

    @Override
    public int compareTo(Cohort otherCohort) {
        return Integer.compare(
                this.cohortNum != null ? this.cohortNum : 0,
                otherCohort.cohortNum != null ? otherCohort.cohortNum : 0
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCohortNum() {
        return cohortNum;
    }

    public void setCohortNum(Integer cohortNum) {
        this.cohortNum = cohortNum;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        this.members = members;
    }
}
