package com.planzajec.controllers;

import com.jfoenix.controls.JFXTextField;
import com.planzajec.dao.Impl.NauczycielDaoImpl;
import com.planzajec.dao.NauczycielDao;
import com.planzajec.entity.Nauczyciel;
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
import java.util.*;
import java.util.logging.Logger;

public class TabelkaNauczycielPlanZajecController implements Initializable {

    private ObservableList<Nauczyciel> oblist = FXCollections.observableArrayList();

    private final NauczycielDao nauczycielDao = new NauczycielDaoImpl();

    @FXML
    private JFXTextField szukajTextField;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Nauczyciel> table;

    @FXML
    private TableColumn<Nauczyciel, Integer> col_lp;

    @FXML
    private TableColumn<Nauczyciel, String> col_imie;

    @FXML
    private TableColumn<Nauczyciel, String> col_nazwisko;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTable();
        ustawEventHandler();
    }

    private void fillTable(){

        col_imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        col_nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        col_lp.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Integer>(table.getItems().indexOf(column.getValue()) + 1));

        List<Nauczyciel> nauczyciele = new ArrayList<>(nauczycielDao.findAll());

        oblist.addAll(nauczyciele);

        FilteredList<Nauczyciel> nauczycielFilteredList = new FilteredList<>(oblist, value -> true);
        szukajTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            nauczycielFilteredList.setPredicate(nauczyciel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCase = newValue.toLowerCase();

                if (nauczyciel.getImie().toLowerCase().contains(lowerCase))
                    return true;
                else if (nauczyciel.getNazwisko().toLowerCase().contains(lowerCase))
                    return true;
                return false;
            });
        });

        SortedList<Nauczyciel> nauczycielSortedData = new SortedList<>(nauczycielFilteredList);
        nauczycielSortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(nauczycielSortedData);
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

    private void loadNewView(){
        try {
            Nauczyciel wybranyNauczyciel = table.getSelectionModel().getSelectedItem();

            Logger.getGlobal().info("Wybrano nauczyciela {id:"
                    + wybranyNauczyciel.getIdNauczyciel() + "}: "
                    + wybranyNauczyciel.getImie() + " "
                    + wybranyNauczyciel.getNazwisko());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/glowna/planZajec.fxml"));
            Pane pane = loader.load();

            PlanZajec planZajec = new PlanZajecImpl(wybranyNauczyciel);

            PlanZajecController planZajecController = loader.getController();
            planZajecController.setPlanZajec(planZajec);
            planZajecController.setDysponentPlanuZajec(wybranyNauczyciel);
            planZajecController.init();

            mainPane.getChildren().setAll(pane);
            Platform.runLater(planZajecController::synchronizujScrolle);


        } catch (Exception e){
            Logger.getGlobal().info(e.getMessage());
        }

    }


}