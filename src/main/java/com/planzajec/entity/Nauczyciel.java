package com.planzajec.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "nauczyciel", schema = "testplan")
public class Nauczyciel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNauczyciel")
    private int idNauczyciel;

    @Column(name = "rola")
    private String rola;

    @Column(name = "imie")
    private String imie;

    @Column(name = "nazwisko")
    private String nazwisko;

    @OneToMany(targetEntity = GodzinaLekcyjna.class, mappedBy = "nauczyciel")
    private List<GodzinaLekcyjna> godzinyLekcyjne;

    @ManyToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "przedmiot_has_nauczyciel",
            joinColumns = @JoinColumn(name = "Nauczyciel_idNauczyciel"),
            inverseJoinColumns = @JoinColumn(name = "Przedmiot_idPrzedmiot"))
    private Set<Przedmiot> przedmioty = new HashSet<>();


    public Nauczyciel(){}

    public Nauczyciel(int idNauczyciel, String rola, String imie, String nazwisko) {
        this.idNauczyciel = idNauczyciel;
        this.rola = rola;
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    public int getIdNauczyciel() {
        return idNauczyciel;
    }

    public void setIdNauczyciel(int idNauczyciel) {
        this.idNauczyciel = idNauczyciel;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }


    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }


    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public List<GodzinaLekcyjna> getGodzinyLekcyjne() {
        return godzinyLekcyjne;
    }

    public void setGodzinyLekcyjne(List<GodzinaLekcyjna> godzinyLekcyjne) {
        this.godzinyLekcyjne = godzinyLekcyjne;
    }

    public Set<Przedmiot> getPrzedmioty() {
        return przedmioty;
    }

    public void setPrzedmioty(Set<Przedmiot> przedmioty) {
        this.przedmioty = przedmioty;
    }

    public void addPrzedmiot(Przedmiot aPrzedmiot) {
        this.przedmioty.add(aPrzedmiot);
        aPrzedmiot.getNauczyciele().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nauczyciel that = (Nauczyciel) o;
        return idNauczyciel == that.idNauczyciel &&
                Objects.equals(rola, that.rola) &&
                Objects.equals(imie, that.imie) &&
                Objects.equals(nazwisko, that.nazwisko);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNauczyciel, rola, imie, nazwisko);
    }

    @Override
    public String toString() {
        return nazwisko + " " + imie;
    }
}
