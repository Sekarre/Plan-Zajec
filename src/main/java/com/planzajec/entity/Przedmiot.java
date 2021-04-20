package com.planzajec.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "przedmiot", schema = "testplan")
public class Przedmiot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPrzedmiot")
    private int idPrzedmiot;


    @Column(name = "nazwa")
    private String nazwa;

    @OneToMany(targetEntity = GodzinaLekcyjna.class, mappedBy = "przedmiot")
    private List<GodzinaLekcyjna> godzinyLekcyjne;

    @ManyToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "przedmiot_has_nauczyciel",
            joinColumns = @JoinColumn(name = "Przedmiot_idPrzedmiot"),
            inverseJoinColumns = @JoinColumn(name = "Nauczyciel_idNauczyciel"))
    private Set<Nauczyciel> nauczyciele = new HashSet<>();

    public Przedmiot(){}

    public Przedmiot(int idPrzedmiot, String nazwa) {
        this.idPrzedmiot = idPrzedmiot;
        this.nazwa = nazwa;
    }

    public int getIdPrzedmiot() {
        return idPrzedmiot;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setIdPrzedmiot(int idPrzedmiot) {
        this.idPrzedmiot = idPrzedmiot;
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

    public Set<Nauczyciel> getNauczyciele() {
        return nauczyciele;
    }

    public void setNauczyciele(Set<Nauczyciel> nauczyciele) {
        this.nauczyciele = nauczyciele;
    }

    public void addNauczyciel(Nauczyciel nauczyciel) {
        this.nauczyciele.add(nauczyciel);
        nauczyciel.getPrzedmioty().add(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrzedmiot, nazwa);
    }

    @Override
    public String toString() {
        return nazwa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Przedmiot that = (Przedmiot) o;
        return idPrzedmiot == that.idPrzedmiot &&
                Objects.equals(nazwa, that.nazwa);
    }

}
