package com.planzajec.controllers;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.planzajec.dao.*;
import com.planzajec.dao.Impl.*;
import com.planzajec.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class EdycjaGodzinyController {

    private GodzinaLekcyjna godzinaLekcyjna;
    private PlanZajecController planZajecController;
    private NauczycielDao nauczycielDao = new NauczycielDaoImpl();
    private PrzedmiotDao przedmiotDao = new PrzedmiotDaoImpl();
    private SalaDao salaDao = new SalaDaoImpl();
    private KlasaDao klasaDao = new KlasaDaoImpl();

    private ObservableList<Klasa> klasaObservableList = FXCollections.observableArrayList();
    private ObservableList<Nauczyciel> nauczycielObservableList = FXCollections.observableArrayList();
    private ObservableList<Przedmiot> przedmiotObservableList = FXCollections.observableArrayList();
    private ObservableList<Sala> salaObservableList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane edycjaPane;

    @FXML
    private JFXTextField dzienTextField;

    @FXML
    private JFXTextField godzinaTextField;

    @FXML
    private JFXComboBox<Klasa> klasaJFXComboBox;

    @FXML
    private JFXComboBox<Nauczyciel> nauczycielJFXComboBox;

    @FXML
    private JFXComboBox<Przedmiot> przedmiotJFXComboBox;

    @FXML
    private JFXComboBox<Sala> salaJFXComboBox;

    @FXML
    private MenuButton klasaPopup;

    @FXML
    private MenuButton nauczycielPopup;

    @FXML
    private MenuButton salaPopup;

    @FXML
    private ImageView klasaOkImage;

    @FXML
    private ImageView salaOkImage;

    @FXML
    private ImageView nauczycielOkImage;

    @FXML
    private Label mainInfoLabel;


    @FXML
    void nauczycielJFXComboBoxOnAction() {
        przedmiotObservableList.setAll(nauczycielJFXComboBox.getValue().getPrzedmioty());
        przedmiotJFXComboBox.setValue(null);
        ustawTaska(nauczycielJFXComboBox.getSelectionModel().getSelectedItem().getIdNauczyciel(), Nauczyciel.class,  nauczycielPopup, nauczycielOkImage);
    }

    @FXML
    public void klasaJFXComboBoxOnAction() {
        ustawTaska(klasaJFXComboBox.getSelectionModel().getSelectedItem().getIdKlasa(), Klasa.class,  klasaPopup, klasaOkImage);
    }

    @FXML
    public void salaJFXComboBoxOnAction() {
        ustawTaska(salaJFXComboBox.getSelectionModel().getSelectedItem().getIdSala(), Sala.class,  salaPopup, salaOkImage);
    }


    private void ustawEventHandler() {
        KeyCombination saveKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination deleteKeyCombination = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
        edycjaPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (saveKeyCombination.match(event))
                zapiszGodzineButtonOnAction();
            if (deleteKeyCombination.match(event))
                usunGodzineButtonOnAction(event);
        });
    }


    void setPlanZajecController(PlanZajecController planZajecController) {
        this.planZajecController = planZajecController;
    }

    void setGodzina(GodzinaLekcyjna godzinaLekcyjna) {
        this.godzinaLekcyjna = godzinaLekcyjna;
    }

    void init() {
        dzienTextField.setText(godzinaLekcyjna.getDzien().getNazwa());
        godzinaTextField.setText(godzinaLekcyjna.getCzasTrwaniaLekcji().getCzasRozpoczecia()
                + " - " + godzinaLekcyjna.getCzasTrwaniaLekcji().getCzasZakonczenia());

        if(godzinaLekcyjna.getKlasa() != null)
            klasaJFXComboBox.setValue(godzinaLekcyjna.getKlasa());
        klasaObservableList.setAll(klasaDao.findAllOrderBy("nazwa", true));

        if(godzinaLekcyjna.getNauczyciel() != null) {
            nauczycielJFXComboBox.setValue(godzinaLekcyjna.getNauczyciel());
            przedmiotObservableList.setAll(godzinaLekcyjna.getNauczyciel().getPrzedmioty());
        } else {
            List<Przedmiot> przedmioty = new ArrayList<>(przedmiotDao.findAllOrderBy("nazwa", true));
            przedmiotObservableList.setAll(przedmioty);
        }
        List<Nauczyciel> nauczyciele = new ArrayList<>(nauczycielDao.findAllOrderBy("nazwisko", true));
        nauczycielObservableList.setAll(nauczyciele);

        if(godzinaLekcyjna.getPrzedmiot() != null)
            przedmiotJFXComboBox.setValue(godzinaLekcyjna.getPrzedmiot());

        if(godzinaLekcyjna.getSala() != null)
            salaJFXComboBox.setValue(godzinaLekcyjna.getSala());

        salaObservableList.setAll(salaDao.findAllOrderBy("numer", true));

        if(planZajecController.getDysponentPlanuZajec().getClass().getTypeName().equals(Nauczyciel.class.getTypeName())){
            nauczycielJFXComboBox.setDisable(true);
            nauczycielJFXComboBox.setValue((Nauczyciel) planZajecController.getDysponentPlanuZajec());
            przedmiotObservableList.setAll(((Nauczyciel) planZajecController.getDysponentPlanuZajec()).getPrzedmioty());

        } else if (planZajecController.getDysponentPlanuZajec().getClass().getTypeName().equals(Klasa.class.getTypeName())){
            klasaJFXComboBox.setDisable(true);
            klasaJFXComboBox.setValue((Klasa) planZajecController.getDysponentPlanuZajec());
        }

        klasaJFXComboBox.setItems(klasaObservableList);
        nauczycielJFXComboBox.setItems(nauczycielObservableList);
        przedmiotJFXComboBox.setItems(przedmiotObservableList);
        salaJFXComboBox.setItems(salaObservableList);
        ustawEventHandler();
    }


    public void zapiszGodzineButtonOnAction() {
        if (!(klasaPopup.isVisible() || nauczycielPopup.isVisible() || salaPopup.isVisible())) {
                godzinaLekcyjna.setSala(salaJFXComboBox.getSelectionModel().getSelectedItem());
                godzinaLekcyjna.setNauczyciel(nauczycielJFXComboBox.getSelectionModel().getSelectedItem());
                godzinaLekcyjna.setKlasa(klasaJFXComboBox.getSelectionModel().getSelectedItem());
                godzinaLekcyjna.setPrzedmiot(przedmiotJFXComboBox.getSelectionModel().getSelectedItem());

                if(!sprawdzCzyNull()) {
                    planZajecController.zapiszGodzine(godzinaLekcyjna);
                    mainInfoLabel.setText("Zapisano. ");
                    mainInfoLabel.setStyle("-fx-text-fill: green");
                    return;
                }
        }

        mainInfoLabel.setText("Nie można wykonać tej akcji. ");
        mainInfoLabel.setStyle("-fx-text-fill: red");
        Logger.getGlobal().info("Nie mozna zapisac");
    }

    public void usunGodzineButtonOnAction(Event actionEvent) {
        if(godzinaLekcyjna != null) {
            if(!sprawdzCzyNull()){
                ButtonType tak = new ButtonType("Tak", ButtonBar.ButtonData.YES);
                ButtonType nie = new ButtonType("Nie", ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "",tak, nie);
                alert.setTitle("Potwierdzenie");
                alert.setHeaderText("Na pewno chcesz usunąć wybraną godzine? ");
                alert.showAndWait();
                if(alert.getResult() == tak) {
                    planZajecController.usunGodzine(godzinaLekcyjna);
                    cofnijButtonOnAction(actionEvent);
                }
                return;
            }
        }
        mainInfoLabel.setText("Nie można wykonać tej akcji. ");
        mainInfoLabel.setStyle("-fx-text-fill: red");
        Logger.getGlobal().info("godzina to null");

    }

    public void onEscPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            ((Stage)((Node) keyEvent.getSource()).getScene().getWindow()).close();
        }
    }


    public void cofnijButtonOnAction(Event actionEvent) {
        ((Stage)((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    private void ustawTaska(int callerId, Class callerClass, MenuButton popup, ImageView okImage) {
        final Boolean[] result = {false};
        final GodzinaLekcyjna[] znalezionaGodzinaLekcyjna = {null};

        Task isCollidingTaskV2 = new Task() {
            @Override
            protected Boolean call() {
                if (isCancelled()) return true;
                znalezionaGodzinaLekcyjna[0] = szukaj(
                        godzinaLekcyjna.getCzasTrwaniaLekcji().getIdCzasTrwaniaLekcji(),
                        godzinaLekcyjna.getDzien().getIdDzien(),
                        callerId,
                        callerClass);
                if (znalezionaGodzinaLekcyjna[0] != null) {
                    result[0] = true;
                }

                return result[0];
            }


            @Override
            protected void succeeded() {
                if(result[0]) {
                    okImage.setImage(new Image(getClass().getResourceAsStream("/style/images/notOkey.jpg")));
                    MenuItem item = new MenuItem();
                    item.setGraphic(stworzWidokMenuItem());
                    popup.getItems().setAll(item);
                    popup.setVisible(true);
                }
                else {
                    okImage.setImage(new Image(getClass().getResourceAsStream("/style/images/okey.png")));
                    popup.setVisible(false);
                }
                Logger.getGlobal().info(result[0] + "");
            }


            private GodzinaLekcyjna szukaj(int idCzasTrwania, int idDzien, int callerId, Class klasaBazowa) {
                GodzinaLekcyjnaDao godzinaLekcyjnaDao = new GodzinaLekcyjnaDaoImpl();
                return godzinaLekcyjnaDao.findCorresponding(
                        idCzasTrwania,
                        idDzien,
                        callerId,
                        klasaBazowa
                );
            }

            private VBox stworzWidokMenuItem() {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/glowna/kolidujacaGodzina.fxml"));
                    VBox pane = fxmlLoader.load();
                    KolidujacaGodzinaController kolidujacaGodzinaController = fxmlLoader.getController();
                    kolidujacaGodzinaController.setGodzinaLekcyjna(znalezionaGodzinaLekcyjna[0]);
                    kolidujacaGodzinaController.setLabels();
                    return pane;

                } catch (Exception e) {
                    Logger.getGlobal().info(e.getMessage());
                }
                return null;
            }
        };

        new Thread(isCollidingTaskV2).start();
    }


    /**true - jezeli jest nullem, false - jezeli nie jest nullem*/
    private Boolean sprawdzCzyNull() {
        return (godzinaLekcyjna.getSala() == null || godzinaLekcyjna.getPrzedmiot() == null || godzinaLekcyjna.getNauczyciel() == null || godzinaLekcyjna.getKlasa() == null);
    }

}

