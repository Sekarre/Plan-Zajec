package com.planzajec.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.planzajec.dao.CzasTrwaniaLekcjiDao;
import com.planzajec.dao.Impl.CzasTrwaniaLekcjiDaoImpl;
import com.planzajec.entity.CzasTrwaniaLekcji;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ZarzadzajCzasemTrwaniaLekcjiController implements Initializable {

    private ObservableList<CzasTrwaniaLekcji> oblist = FXCollections.observableArrayList();

    private final CzasTrwaniaLekcjiDao czasTrwaniaLekcjiDao = new CzasTrwaniaLekcjiDaoImpl();

    private CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji;

    @FXML
    private AnchorPane panelDolny;

    @FXML
    private TableView<CzasTrwaniaLekcji> table;

    @FXML
    private TableColumn<CzasTrwaniaLekcji, Integer> col_numer;

    @FXML
    private TableColumn<CzasTrwaniaLekcji, String> col_czasRozpoczecia;

    @FXML
    private TableColumn<CzasTrwaniaLekcji, String> col_czasZakonczenia;

    @FXML
    private Label infoLabel;

    @FXML
    private JFXTextField numerTextField;

    @FXML
    private JFXTimePicker czasRozpoczeciaPicker;

    @FXML
    private JFXTimePicker czasZakonczeniaPicker;

    @FXML
    private JFXButton addButtonFast;

    @FXML
    private JFXButton refresh;


    @FXML
    void tableOnMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            getSelectedItem();
        }


    }

    public void tableOnKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ENTER: getSelectedItem(); break;
            case ESCAPE: refresh(); break;
        }
    }

    public void panelDolnyOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE)
            usunCzasTrwaniaLekcji();
    }

    private void ustawEventHandler() {
        KeyCombination saveKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination deleteKeyCombination = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);

        table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (saveKeyCombination.match(event))
                zapiszButtonOnAction();
            else if(deleteKeyCombination.match(event))
                usunCzasTrwaniaLekcji();
        });

        panelDolny.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (saveKeyCombination.match(event))
                zapiszButtonOnAction();
        });
    }


    @FXML
    void usunButtonOnAction() {
        usunCzasTrwaniaLekcji();
    }

    @FXML
    void zapiszButtonOnAction() {
        zapiszCzasTrwaniaLekcji();
    }

    @FXML
    void addButtonFastOnAction() {
        dodajCzasTrwaniaLekcji();
    }

    public void refreshButtonOnAction() {
        refresh();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wybranyCzasTrwaniaLekcji = null;
        ustawTooltip("Info", "Dodanie nowej godziny  (+45min)", addButtonFast);
        ustawTooltip("Info", "Odznaczenie elementu ", refresh);
        ustawEventHandler();
        setFactory();
        fillTable();

    }

    private void setFactory() {
        col_numer.setCellValueFactory(new PropertyValueFactory<>("numer"));
        col_czasRozpoczecia.setCellValueFactory(new PropertyValueFactory<>("czasRozpoczecia"));
        col_czasZakonczenia.setCellValueFactory(new PropertyValueFactory<>("czasZakonczenia"));
    }

    private void fillTable(){
        table.getItems().clear();
        List<CzasTrwaniaLekcji> czasTrwaniaLekcjiList = czasTrwaniaLekcjiDao.findAllOrderBy("numer", true);
        oblist.addAll(czasTrwaniaLekcjiList);
        table.setItems(oblist);
    }

    private void fillEdit() {
        numerTextField.setText(wybranyCzasTrwaniaLekcji.getNumer().toString());
        czasRozpoczeciaPicker.setValue(wybranyCzasTrwaniaLekcji.getCzasRozpoczecia());
        czasZakonczeniaPicker.setValue(wybranyCzasTrwaniaLekcji.getCzasZakonczenia());
    }

    private void zapiszCzasTrwaniaLekcji(){
        if (wybranyCzasTrwaniaLekcji == null) zapiszNowy();
        else {
            try {
                wybranyCzasTrwaniaLekcji.setNumer(Integer.valueOf(numerTextField.getText()));
                wybranyCzasTrwaniaLekcji.setCzasRozpoczecia(czasRozpoczeciaPicker.getValue());
                wybranyCzasTrwaniaLekcji.setCzasZakonczenia(czasZakonczeniaPicker.getValue());
                if (waliduj(wybranyCzasTrwaniaLekcji)) {
                    czasTrwaniaLekcjiDao.saveOrUpdate(wybranyCzasTrwaniaLekcji);
                    if (wybranyCzasTrwaniaLekcji.getNumer().equals(oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 1).getNumer()))
                        if (sprawdzCzyPrzesunac(wybranyCzasTrwaniaLekcji) && walidujDodatkowo(wybranyCzasTrwaniaLekcji))
                            przesunCzasy(wybranyCzasTrwaniaLekcji.getNumer(), true);
                    ustawInfo("Zapisano: " + wybranyCzasTrwaniaLekcji.toString());
                } else {
                    ustawInfo("Wystąpił błąd. Niepoprawne dane. ");
                    Logger.getGlobal().info("Błąd walidacji. ");
                }

            } catch (Exception e) {
                Logger.getGlobal().info(e.getMessage());
                ustawInfo("Nie można zapisać elementu. ");
            }
            fillTable();
            wyczyscDaneDoEdycji();
        }
    }

    private void zapiszNowy() {
        try {
            wybranyCzasTrwaniaLekcji = new CzasTrwaniaLekcji(Integer.valueOf(numerTextField.getText()), czasRozpoczeciaPicker.getValue(), czasZakonczeniaPicker.getValue());
            if (waliduj(wybranyCzasTrwaniaLekcji)){
                if(oblist.size() == 0 || wybranyCzasTrwaniaLekcji.getNumer() - 1 >= oblist.size()) {
                    czasTrwaniaLekcjiDao.saveOrUpdate(wybranyCzasTrwaniaLekcji);
                }
                else if(wybranyCzasTrwaniaLekcji.getNumer().equals(oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 1).getNumer()))
                    if(sprawdzCzyPrzesunac(wybranyCzasTrwaniaLekcji) && walidujDodatkowo(wybranyCzasTrwaniaLekcji)) {
                        czasTrwaniaLekcjiDao.saveOrUpdate(wybranyCzasTrwaniaLekcji);
                        przesunCzasy(wybranyCzasTrwaniaLekcji.getNumer(), true);
                        ustawInfo("Zapisano: " + wybranyCzasTrwaniaLekcji.toString());
                    }
                    else {
                        ustawInfo("Wystąpił błąd. Niepoprawne dane. ");
                        Logger.getGlobal().info("Błąd walidacji. ");
                    }
            }
            else
                ustawInfo("Wystąpił błąd. Niepoprawne dane. ");
            fillTable();
            wyczyscDaneDoEdycji();
        } catch (Exception ex){
            ustawInfo("Wystąpił błąd. Niepoprawne dane. ");
            Logger.getGlobal().info("Błąd wczytywania. ");
        }

    }


    private void usunCzasTrwaniaLekcji() {
        if (wybranyCzasTrwaniaLekcji != null){
            ButtonType tak = new ButtonType("Usuń mimo to", ButtonBar.ButtonData.YES);
            ButtonType nie = new ButtonType("Anuluj", ButtonBar.ButtonData.NO);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Czy napewno chcesz go usunąć? ",tak, nie);
            alert.setHeaderText("Usunięcie rekordu spowoduje usunięcie wszystkich powiązanych godzin lekcyjnych. \n");
            alert.setTitle("Potwierdzenie");
            alert.showAndWait();

            if (alert.getResult() == tak) {
                if(!wybranyCzasTrwaniaLekcji.getNumer().equals(oblist.size()))
                    przesunCzasy(wybranyCzasTrwaniaLekcji.getNumer(), false);
                czasTrwaniaLekcjiDao.delete(wybranyCzasTrwaniaLekcji);
                ustawInfo("Usunięto: " + wybranyCzasTrwaniaLekcji.toString());
                wybranyCzasTrwaniaLekcji = null;
                wyczyscDaneDoEdycji();
                fillTable();
            }
        } else {
            Logger.getGlobal().info("wybranyCzasTrwaniaLekcji jest NULL");
            ustawInfo("Nie można usunąć elementu. ");
        }
    }

    private void dodajCzasTrwaniaLekcji() {

        CzasTrwaniaLekcji czasTrwaniaLekcji = new CzasTrwaniaLekcji();

        CzasTrwaniaLekcji ostatniCzasTrwaniaLekcji;

        try {
            ostatniCzasTrwaniaLekcji = oblist.get(oblist.size() - 1);
            czasTrwaniaLekcji.setNumer(ostatniCzasTrwaniaLekcji.getNumer() + 1);
            czasTrwaniaLekcji.setCzasRozpoczecia(ostatniCzasTrwaniaLekcji.getCzasZakonczenia().plusMinutes(10));
            czasTrwaniaLekcji.setCzasZakonczenia(ostatniCzasTrwaniaLekcji.getCzasZakonczenia().plusMinutes(10 + 45));
        } catch (ArrayIndexOutOfBoundsException e){
            czasTrwaniaLekcji.setNumer(1);
            czasTrwaniaLekcji.setCzasRozpoczecia(LocalTime.of(8,0,0,0));
            czasTrwaniaLekcji.setCzasZakonczenia(LocalTime.of(8,45,0,0));
        } catch (Exception e){
            Logger.getGlobal().info("Error w com.planzajec.controllers.ZarzadzajCzasemTrwaniaController.dodajCzasTrwaniaLekcji");
        }

        czasTrwaniaLekcjiDao.saveOrUpdate(czasTrwaniaLekcji);
        ustawInfo("Dodano: " + czasTrwaniaLekcji.toString());

        wyczyscDaneDoEdycji();
        fillTable();
    }

    private void wyczyscDaneDoEdycji() {
        wybranyCzasTrwaniaLekcji = null;

        numerTextField.setText("");
        czasRozpoczeciaPicker.setValue(LocalTime.of(0,0));
        czasZakonczeniaPicker.setValue(LocalTime.of(0,0));
    }



    private boolean waliduj(CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji) {
        int numer = wybranyCzasTrwaniaLekcji.getNumer();
        CzasTrwaniaLekcji nastepnyCzas = null;
        CzasTrwaniaLekcji poprzedniCzas = null;
        if (oblist.size() == 0 || oblist.size() == 1);
        else if(numer <= 0 || numer > oblist.size() + 1) return false;
        else if (numer <= 1) nastepnyCzas = oblist.get(numer);
        else if (numer >= oblist.size()) poprzedniCzas = oblist.get(numer - 2);
        else {
            nastepnyCzas = oblist.get(numer);
            poprzedniCzas = oblist.get(numer - 2);
        }

        if(!sprawdzPrzylegajaceGodziny(poprzedniCzas, nastepnyCzas)) return false;
        return true;
    }


    private boolean walidujDodatkowo(CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji) {
        CzasTrwaniaLekcji nastepnyCzas;
        CzasTrwaniaLekcji poprzedniCzas;
        if(wybranyCzasTrwaniaLekcji.getNumer() == 1) {
            if (wybranyCzasTrwaniaLekcji.getCzasZakonczenia().isAfter(oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 1).getCzasRozpoczecia())) {
                return false;
            }
        }
        else {
            nastepnyCzas = oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 1);
            poprzedniCzas = oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 2);
            if(!sprawdzPrzylegajaceGodziny(poprzedniCzas, nastepnyCzas)) return false;
        }

        return true;
    }

    private boolean sprawdzPrzylegajaceGodziny(CzasTrwaniaLekcji poprzedniCzas, CzasTrwaniaLekcji nastepnyCzas) {
        if(wybranyCzasTrwaniaLekcji.getCzasRozpoczecia().equals(wybranyCzasTrwaniaLekcji.getCzasZakonczenia()))
            return false;

        if (wybranyCzasTrwaniaLekcji.getCzasRozpoczecia().isAfter(wybranyCzasTrwaniaLekcji.getCzasZakonczenia()))
            return false;

        if (nastepnyCzas != null) {
            if (wybranyCzasTrwaniaLekcji.getCzasZakonczenia().isAfter(nastepnyCzas.getCzasRozpoczecia())) {
                return false;
            }
        }

        if (poprzedniCzas != null) {
            if (wybranyCzasTrwaniaLekcji.getCzasRozpoczecia().isBefore(poprzedniCzas.getCzasZakonczenia()))
                return false;
        }

        return true;
    }

    private void ustawInfo(String info) {
        infoLabel.setText(info);
        ustawTimerZnikaniaPowiadomienia(3);
    }

    private void ustawTooltip(String tooltipInfo, String text, JFXButton button) {
        final Tooltip tooltip = new Tooltip(tooltipInfo);
        tooltip.setText(text);
        button.setTooltip(tooltip);
    }

    private void refresh() {
        table.getSelectionModel().clearSelection();
        numerTextField.setDisable(false);
        table.requestFocus();
        wyczyscDaneDoEdycji();
    }

    private void ustawTimerZnikaniaPowiadomienia(int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(event -> infoLabel.setText(""));
        pause.play();
    }

    private void getSelectedItem() {
        try {
            wybranyCzasTrwaniaLekcji = table.getSelectionModel().getSelectedItem();
            fillEdit();
            numerTextField.setDisable(true);

        } catch (Exception e) {
            wyczyscDaneDoEdycji();
            numerTextField.setDisable(false);
            Logger.getGlobal().info("Wybrano nazwę kolumny");
            ustawInfo("Wybierz pole.");
        }
    }


    /** false - nie przesuwac, true - przesuwac*/
    private Boolean sprawdzCzyPrzesunac(CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji) {
        try {
            if(wybranyCzasTrwaniaLekcji == oblist.get(wybranyCzasTrwaniaLekcji.getNumer() - 1))
                return false;
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private void przesunCzasy(int numer, boolean inkrementacja) {
        CzasTrwaniaLekcji czasTrwaniaLekcji;
        int wartosc = inkrementacja ? 1 : -1;
        for (int i = numer - 1; i < oblist.size(); i++) {
            czasTrwaniaLekcji = oblist.get(i);
            czasTrwaniaLekcji.setNumer(czasTrwaniaLekcji.getNumer() + wartosc);
            czasTrwaniaLekcjiDao.saveOrUpdate(czasTrwaniaLekcji);
        }

    }


}
