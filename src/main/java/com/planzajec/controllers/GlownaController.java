package com.planzajec.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Logger;

public class GlownaController {

    @FXML
    private Pane mainPane;

    @FXML
    private Label nauczycieleSelectedLabel;

    @FXML
    private Label klasySelectedLabel;

    @FXML
    private Label czasSelectedLabel;

    public void klasaButtonOnMouseClicked() throws IOException {
        long time = System.currentTimeMillis();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/glowna/tabelkaKlasaPlanZajec.fxml"));
        Pane pane = loader.load();
        ustawScene(pane, klasySelectedLabel);
        Logger.getGlobal().info(System.currentTimeMillis() - time + " ");
    }

    public void nauczycielButtonOnMouseClicked() throws IOException {
        long time = System.currentTimeMillis();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/glowna/tabelkaNauczycielPlanZajec.fxml"));
        Pane pane = loader.load();
        ustawScene(pane, nauczycieleSelectedLabel);
        Logger.getGlobal().info(System.currentTimeMillis() - time + " ");

    }

    public void czasTrwaniaButtonOnMouseClicked() throws IOException {
        long time = System.currentTimeMillis();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/glowna/zarzadzajCzasemTrwaniaLekcji.fxml"));
        Pane pane = loader.load();
        ustawScene(pane, czasSelectedLabel);
        Logger.getGlobal().info(System.currentTimeMillis() - time + " ");

    }


    public void wyjscieButtonOnMouseClicked() {
        Platform.exit();
        System.exit(0);
    }

    private void ukryjLabele() {
        nauczycieleSelectedLabel.setVisible(false);
        klasySelectedLabel.setVisible(false);
        czasSelectedLabel.setVisible(false);
    }

    private void ustawScene(Pane pane, Label label) {
        mainPane.getChildren().setAll(pane);
        ukryjLabele();
        label.setVisible(true);
        pane.prefHeightProperty().bind(mainPane.heightProperty());
        pane.prefWidthProperty().bind(mainPane.widthProperty());
    }
}
