package com.planzajec.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dzien", schema = "testplan")
public class Dzien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddzien")
    private int idDzien;

    @Column(name = "nazwa")
    private String nazwa;

    @Column(name = "numer")
    private Integer numer;

    @OneToMany(targetEntity = GodzinaLekcyjna.class, mappedBy = "dzien")
    private List<GodzinaLekcyjna> godzinyLekcyjne = new ArrayList<>();

    public Dzien(){}
    public Dzien(int idDzien, String nazwa, Integer numer) {
        this.idDzien = idDzien;
        this.nazwa = nazwa;
        this.numer = numer;
    }

    public int getIdDzien() {
        return idDzien;
    }

    public void setIdDzien(int iddzien) {
        this.idDzien = iddzien;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getNumer() {
        return numer;
    }

    public void setNumer(Integer numer) {
        this.numer = numer;
    }

    public List<GodzinaLekcyjna> getGodzinyLekcyjne() {
        return godzinyLekcyjne;
    }

    public void setGodzinyLekcyjne(List<GodzinaLekcyjna> godzinyLekcyjne) {
        this.godzinyLekcyjne = godzinyLekcyjne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dzien that = (Dzien) o;
        return idDzien == that.idDzien &&
                Objects.equals(nazwa, that.nazwa) &&
                Objects.equals(numer, that.numer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDzien, nazwa, numer);
    }

    @Override
    public String toString() {
        return "Dzien{" +
                "iddzien=" + idDzien +
                ", nazwa='" + nazwa + '\'' +
                ", numer=" + numer +
                '}';
    }

}
