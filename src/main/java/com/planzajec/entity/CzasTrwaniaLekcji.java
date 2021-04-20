package com.planzajec.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "czastrwanialekcji", schema = "testplan")
public class CzasTrwaniaLekcji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idczasTrwaniaLekcji")
    private int idCzasTrwaniaLekcji;

    @Column(name = "numer")
    private Integer numer;

    @Column(name = "czasRozpoczecia")
    private LocalTime czasRozpoczecia;

    @Column(name = "czasZakonczenia")
    private LocalTime czasZakonczenia;

    @OneToMany(targetEntity = GodzinaLekcyjna.class, mappedBy = "czasTrwaniaLekcji", cascade = CascadeType.REMOVE)
    private List<GodzinaLekcyjna> godzinyLekcyjne;


    public CzasTrwaniaLekcji(){}

    public CzasTrwaniaLekcji(int idCzasTrwaniaLekcji, Integer numer, LocalTime czasRozpoczecia, LocalTime czasZakonczenia) {
        this(numer, czasRozpoczecia, czasZakonczenia);
        this.idCzasTrwaniaLekcji = idCzasTrwaniaLekcji;
    }

    public CzasTrwaniaLekcji(Integer numer, LocalTime czasRozpoczecia, LocalTime czasZakonczenia) {
        this.numer = numer;
        this.czasRozpoczecia = czasRozpoczecia;
        this.czasZakonczenia = czasZakonczenia;
    }


    public int getIdCzasTrwaniaLekcji() {
        return idCzasTrwaniaLekcji;
    }

    public void setIdCzasTrwaniaLekcji(int idczasTrwaniaLekcji) {
        this.idCzasTrwaniaLekcji = idczasTrwaniaLekcji;
    }


    public Integer getNumer() {
        return numer;
    }

    public void setNumer(Integer numer) {
        this.numer = numer;
    }


    public LocalTime getCzasRozpoczecia() {
        return czasRozpoczecia;
    }

    public void setCzasRozpoczecia(LocalTime czasRozpoczecia) {
        this.czasRozpoczecia = czasRozpoczecia;
    }


    public LocalTime getCzasZakonczenia() {
        return czasZakonczenia;
    }

    public void setCzasZakonczenia(LocalTime czasZakonczenia) {
        this.czasZakonczenia = czasZakonczenia;
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
        CzasTrwaniaLekcji that = (CzasTrwaniaLekcji) o;
        return idCzasTrwaniaLekcji == that.idCzasTrwaniaLekcji &&
                Objects.equals(numer, that.numer) &&
                Objects.equals(czasRozpoczecia, that.czasRozpoczecia) &&
                Objects.equals(czasZakonczenia, that.czasZakonczenia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCzasTrwaniaLekcji, numer, czasRozpoczecia, czasZakonczenia);
    }

    @Override
    public String toString() {
        return " numer = " + numer +
                ", czas rozpoczecia = " + czasRozpoczecia.toString() +
                ", czas zakonczenia = " + czasZakonczenia.toString();
    }
}
