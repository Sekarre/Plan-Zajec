package com.planzajec.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "godzinalekcyjna", schema = "testplan")
public class GodzinaLekcyjna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGodzinaLekcyjna")
    private int idGodzinaLekcyjna;

    @Column(name = "Opis")
    private String opis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dzien_iddzien")
    private Dzien dzien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "czasTrwaniaLekcji_idczasTrwaniaLekcji")
    private CzasTrwaniaLekcji czasTrwaniaLekcji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Przedmiot_idPrzedmiot")
    private Przedmiot przedmiot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nauczyciel_idnauczyciel")
    private Nauczyciel nauczyciel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Sala_idSala")
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Klasa_idKlasa")
    private Klasa klasa;



    public int getIdGodzinaLekcyjna() {
        return idGodzinaLekcyjna;
    }

    public void setIdGodzinaLekcyjna(int idGodzinaLekcyjna) {
        this.idGodzinaLekcyjna = idGodzinaLekcyjna;
    }


    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }


    public Dzien getDzien() {
        return dzien;
    }

    public CzasTrwaniaLekcji getCzasTrwaniaLekcji() {
        return czasTrwaniaLekcji;
    }

    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }

    public Nauczyciel getNauczyciel() {
        return nauczyciel;
    }

    public Sala getSala() {
        return sala;
    }

    public Klasa getKlasa() {
        return klasa;
    }

    public void setDzien(Dzien dzien) {
        this.dzien = dzien;
    }

    public void setCzasTrwaniaLekcji(CzasTrwaniaLekcji czastrwanialekcji) {
        this.czasTrwaniaLekcji = czastrwanialekcji;
    }

    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    public void setNauczyciel(Nauczyciel nauczyciel) {
        this.nauczyciel = nauczyciel;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public void setKlasa(Klasa klasa) {
        this.klasa = klasa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GodzinaLekcyjna that = (GodzinaLekcyjna) o;
        return idGodzinaLekcyjna == that.idGodzinaLekcyjna &&
                Objects.equals(opis, that.opis) &&
                klasa.equals(that.klasa) &&
                sala.equals(that.sala) &&
                nauczyciel.equals(that.nauczyciel) &&
                przedmiot.equals(that.przedmiot) &&
                dzien.equals(that.dzien) &&
                czasTrwaniaLekcji.equals(that.czasTrwaniaLekcji);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGodzinaLekcyjna, opis);
    }
}
