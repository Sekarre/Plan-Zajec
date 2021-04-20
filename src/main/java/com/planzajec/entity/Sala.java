package com.planzajec.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sala", schema = "testplan")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSala")
    private int idSala;

    @Column(name = "numer")
    private String numer;

    @Column(name = "nazwa")
    private String nazwa;

    @OneToMany(targetEntity = GodzinaLekcyjna.class, mappedBy = "sala")
    private List<GodzinaLekcyjna> godzinyLekcyjne;


    public Sala(){}

    public Sala(int idSala, String numer, String nazwa) {
        this.idSala = idSala;
        this.numer = numer;
        this.nazwa = nazwa;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }


    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
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
        Sala that = (Sala) o;
        return idSala == that.idSala &&
                Objects.equals(numer, that.numer) &&
                Objects.equals(nazwa, that.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSala, numer, nazwa);
    }

    @Override
    public String toString() {
            return  "[" + numer + "] " + (nazwa == null ? "" : nazwa);
    }
}
