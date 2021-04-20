package com.planzajec.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "klasa", schema = "testplan")
public class Klasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKlasa")
    private int idKlasa;

    @Column(name = "nazwa")
    private String nazwa;


    public Klasa(){}

    public Klasa(int idKlasa, String nazwa) {
        this.idKlasa = idKlasa;
        this.nazwa = nazwa;
    }

    public int getIdKlasa() {
        return idKlasa;
    }

    public void setIdKlasa(int idKlasa) {
        this.idKlasa = idKlasa;
    }


    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klasa that = (Klasa) o;
        return idKlasa == that.idKlasa &&
                Objects.equals(nazwa, that.nazwa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKlasa, nazwa);
    }

    @Override
    public String toString() {
        return nazwa;
    }
}
