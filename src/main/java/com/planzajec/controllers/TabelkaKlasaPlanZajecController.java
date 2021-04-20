package com.planzajec.controllers;

import com.jfoenix.controls.JFXTextField;
import com.planzajec.dao.Impl.KlasaDaoImpl;
import com.planzajec.dao.KlasaDao;
import com.planzajec.entity.Klasa;
import com.planzajec.services.PlanZajec;
import com.planzajec.services.PlanZajecImpl;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TabelkaKlasaPlanZajecController implements Initializable {

    private ObservableList<Klasa> oblist = FXCollections.observableArrayList();

    private final KlasaDao klasaDao = new KlasaDaoImpl();

    @FXML
    private JFXTextField szukajTextField;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Klasa> table;

    @FXML
    private TableColumn<Klasa, Integer> col_lp;

    @FXML
    private TableColumn<Klasa, String> col_klasa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTable();
        ustawEventHandler();
    }

    private void fillTable(){
        col_klasa.setText("Klasa");

        col_klasa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        col_lp.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(table.getItems().indexOf(column.getValue()) + 1));

        List<Klasa> klasy = klasaDao.findAll();
        oblist.addAll(klasy);

        FilteredList<Klasa> klasaFilteredList = new FilteredList<>(oblist, value -> true);
        szukajTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            klasaFilteredList.setPredicate(klasa -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCase = newValue.toLowerCase();

                if (klasa.getNazwa().toLowerCase().contains(lowerCase))
                    return true;
                return false;
            });
        });

        SortedList<Klasa> klasaSortedData = new SortedList<>(klasaFilteredList);
        klasaSortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(klasaSortedData);
    }

    public void tableOnMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            loadNewView();
        }
    }

    public void tableOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loadNewView();
        }
    }

    private void ustawEventHandler() {
        KeyCombination searchKeyCombination = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (searchKeyCombination.match(event))
                szukajTextField.requestFocus();
        });
    }

    private void loadNewView() {
        try {
            Klasa wybranaKlasa = table.getSelectionModel().getSelectedItem();

            Logger.getGlobal().info("Wybrano klase {id:" + wybranaKlasa.getIdKlasa() + "}: " + wybranaKlasa.getNazwa());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/glowna/planZajec.fxml"));
            Pane pane = loader.load();

            PlanZajec planZajec = new PlanZajecImpl(wybranaKlasa);

            PlanZajecController planZajecController = loader.getController();
            planZajecController.setPlanZajec(planZajec);
            planZajecController.setDysponentPlanuZajec(wybranaKlasa);
            planZajecController.init();

            mainPane.getChildren().setAll(pane);
            Platform.runLater(planZajecController::synchronizujScrolle);

        } catch (Exception e){
            Logger.getGlobal().info(e.getMessage());
        }
    }
}