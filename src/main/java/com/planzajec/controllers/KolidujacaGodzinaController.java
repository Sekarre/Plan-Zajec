package com.planzajec.controllers;

import com.planzajec.entity.GodzinaLekcyjna;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KolidujacaGodzinaController {


    @FXML
    private Label dzienLabel;

    @FXML
    private Label godzinaLabel;

    @FXML
    private Label klasaLabel;

    @FXML
    private Label nauczycielLabel;

    @FXML
    private Label przedmiotLabel;

    @FXML
    private Label salaLabel;

    private GodzinaLekcyjna godzinaLekcyjna;


    void setGodzinaLekcyjna(GodzinaLekcyjna godzinaLekcyjna) {
        this.godzinaLekcyjna = godzinaLekcyjna;
    }

    void setLabels() {
        dzienLabel.setText("Dzień: " + godzinaLekcyjna.getDzien().getNazwa());
        godzinaLabel.setText("Godzina: " + godzinaLekcyjna.getCzasTrwaniaLekcji().getCzasRozpoczecia() + " - " + godzinaLekcyjna.getCzasTrwaniaLekcji().getCzasZakonczenia());
        klasaLabel.setText("Klasa: " + godzinaLekcyjna.getKlasa().getNazwa());
        nauczycielLabel.setText("Nauczyciel: " + godzinaLekcyjna.getNauczyciel().getNazwisko() + " " + godzinaLekcyjna.getNauczyciel().getImie());
        przedmiotLabel.setText("Przedmiot: " + godzinaLekcyjna.getPrzedmiot().getNazwa());
        salaLabel.setText("Sala: " + godzinaLekcyjna.getSala().getNumer() + (godzinaLekcyjna.getSala().getNazwa() != null ? godzinaLekcyjna.getSala().getNazwa() : ""));
    }

}
