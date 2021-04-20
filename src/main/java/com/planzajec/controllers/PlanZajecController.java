package com.planzajec.controllers;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.planzajec.entity.CzasTrwaniaLekcji;
import com.planzajec.entity.Dzien;
import com.planzajec.entity.GodzinaLekcyjna;
import com.planzajec.entity.Nauczyciel;
import com.planzajec.entity.enums.Dni;
import com.planzajec.pdf.PdfGenerator;
import com.planzajec.pdf.PdfGeneratorImpl;
import com.planzajec.services.PlanZajec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class PlanZajecController {

    private PlanZajec planZajec;
    private Object dysponentPlanuZajec; //owner

    private ObservableList<GodzinaLekcyjna> obListGodzinaLekcyjnaPon = FXCollections.observableArrayList();
    private ObservableList<GodzinaLekcyjna> obListGodzinaLekcyjnaWt = FXCollections.observableArrayList();
    private ObservableList<GodzinaLekcyjna> obListGodzinaLekcyjnaSr = FXCollections.observableArrayList();
    private ObservableList<GodzinaLekcyjna> obListGodzinaLekcyjnaCzw = FXCollections.observableArrayList();
    private ObservableList<GodzinaLekcyjna> obListGodzinaLekcyjnaPt = FXCollections.observableArrayList();
    private ObservableList<CzasTrwaniaLekcji> obListCzasTrwania = FXCollections.observableArrayList();

    @FXML
    private AnchorPane planZajecPane;

    @FXML
    private Label tytulLabel;

    @FXML
    private JFXListView<CzasTrwaniaLekcji> col_czas;

    @FXML
    private JFXListView<CzasTrwaniaLekcji> col_numer;

    @FXML
    private JFXListView<GodzinaLekcyjna> col_pon;

    @FXML
    private JFXListView<GodzinaLekcyjna> col_wt;

    @FXML
    private JFXListView<GodzinaLekcyjna> col_sr;

    @FXML
    private JFXListView<GodzinaLekcyjna> col_czw;

    @FXML
    private JFXListView<GodzinaLekcyjna> col_pt;

    @FXML
    private JFXListView<Dni> col_dzien;


    private double godzinaCellHeight = 98;
    private double dzienCellWidth = 218.55;
    private double fontSize = 14;

    void init() {
        tytulLabel.setText(planZajec.getNazwa());
        fillTable();

    }

    void setPlanZajec(PlanZajec planZajec) {
        this.planZajec = planZajec;
    }

    private void fillTable() {
        clearAll();
        setObList();
        setItemsListView();
        setCellFactories();
        setAdditionalListeners();
        col_dzien.getItems().addAll(Dni.values());
    }

    private void updateTable() {
        clearAll();
        setObList();
        setItemsListView();
        col_dzien.getItems().addAll(Dni.values());

    }

    private void setCellFactories() {
        setCellFactoryGodzinaLekcyjna(col_pon);
        setCellFactoryGodzinaLekcyjna(col_wt);
        setCellFactoryGodzinaLekcyjna(col_sr);
        setCellFactoryGodzinaLekcyjna(col_czw);
        setCellFactoryGodzinaLekcyjna(col_pt);
        setCellFactoryCzasTrwania(col_czas);
        setCellFactoryNumer(col_numer);
        setCellFactoryDzien(col_dzien);
    }

    private void setAdditionalListeners() {
        onEnterPressed(col_pon);
        onEnterPressed(col_wt);
        onEnterPressed(col_sr);
        onEnterPressed(col_czw);
        onEnterPressed(col_pt);

    }

    private void setObList() {
        obListCzasTrwania.addAll(planZajec.getCzasTrwaniaWszystkichLekcji());
        obListGodzinaLekcyjnaPon.addAll(planZajec.getPlan().get(Dni.Poniedziałek.name()));
        obListGodzinaLekcyjnaWt.addAll(planZajec.getPlan().get(Dni.Wtorek.name()));
        obListGodzinaLekcyjnaSr.addAll(planZajec.getPlan().get(Dni.Środa.name()));
        obListGodzinaLekcyjnaCzw.addAll(planZajec.getPlan().get(Dni.Czwartek.name()));
        obListGodzinaLekcyjnaPt.addAll(planZajec.getPlan().get(Dni.Piątek.name()));
    }

    private void setItemsListView() {
        col_czas.setItems(obListCzasTrwania);
        col_pon.setItems(obListGodzinaLekcyjnaPon);
        col_wt.setItems(obListGodzinaLekcyjnaWt);
        col_sr.setItems(obListGodzinaLekcyjnaSr);
        col_czw.setItems(obListGodzinaLekcyjnaCzw);
        col_pt.setItems(obListGodzinaLekcyjnaPt);
        col_numer.setItems(obListCzasTrwania);

    }


    private void clearAll() {
        obListGodzinaLekcyjnaPon.clear();
        obListGodzinaLekcyjnaWt.clear();
        obListGodzinaLekcyjnaSr.clear();
        obListGodzinaLekcyjnaCzw.clear();
        obListGodzinaLekcyjnaPt.clear();
        obListCzasTrwania.clear();
        col_dzien.getItems().clear();
    }


    private void setCellFactoryGodzinaLekcyjna(JFXListView<GodzinaLekcyjna> listView) {
        listView.setCellFactory(param -> {
            JFXListCell<GodzinaLekcyjna> cell = new JFXListCell<GodzinaLekcyjna>(){
                @Override
                protected void updateItem(GodzinaLekcyjna item, boolean empty) {
                    super.updateItem(item, empty);
                    String last = "";
                    if(item != null) {
                        if (dysponentPlanuZajec.getClass().equals(Nauczyciel.class))
                            last = item.getKlasa().getNazwa();
                        else
                            last = item.getNauczyciel().getImie().substring(0, 1) + ". " + item.getNauczyciel().getNazwisko();
                    }
                    setText((item == null) ? " " : item.getPrzedmiot().getNazwa()
                            + "\n\nSala: "
                            + item.getSala().getNumer()
                            + "\n"
                            + (dysponentPlanuZajec.getClass() == Nauczyciel.class ? "Klasa: " : "")
                            + last
                    );

                    setAlignment(Pos.CENTER);
                    setTextAlignment(TextAlignment.CENTER);
                    setFont(new Font(fontSize));
                    setPrefSize(30, godzinaCellHeight);
                }
            };

            cell.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2) {
                    ustawWartosciWEdycjiGodziny(cell);
                }
            });

            return cell;
        });
    }

    private void onEnterPressed(ListView<GodzinaLekcyjna> col) {
        col.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/glowna/edycjaGodziny.fxml"));
                Parent parent = null;
                try {
                    parent = fxmlLoader.load();
                } catch (IOException e) {
                    Logger.getGlobal().info(e.getMessage());
                }
                EdycjaGodzinyController edycjaGodzinyController = fxmlLoader.getController();

                GodzinaLekcyjna wybranaGodzinaLekcyjna = col.getSelectionModel().getSelectedItem();
                if(wybranaGodzinaLekcyjna == null) {
                    wybranaGodzinaLekcyjna = new GodzinaLekcyjna();

                    //Zakladamy ze wybrany jest poniedzialek
                    int index = 0;

                    if (col.equals(col_wt)) index = 1;
                    else if (col.equals(col_sr)) index = 2;
                    else if (col.equals(col_czw)) index = 3;
                    else if (col.equals(col_pt)) index = 4;

                    Dni dzien = col_dzien.getItems().get(index);
                    Dzien wybranyDzien = new Dzien(index + 1, dzien.name(), index + 1);
                    CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji;

                    try {
                        wybranyCzasTrwaniaLekcji = col_numer.getItems().get(col.getSelectionModel().getSelectedIndex());
                        wybranaGodzinaLekcyjna.setDzien(wybranyDzien);
                        wybranaGodzinaLekcyjna.setCzasTrwaniaLekcji(wybranyCzasTrwaniaLekcji);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        Logger.getGlobal().info("Nic nie wybrano");
                        wybranaGodzinaLekcyjna = null;
                    }
                }
                if(wybranaGodzinaLekcyjna != null)
                    ustawSceneEdycji(edycjaGodzinyController, wybranaGodzinaLekcyjna, parent);
            }
        });
    }

    private void ustawWartosciWEdycjiGodziny(JFXListCell<GodzinaLekcyjna> cell){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/glowna/edycjaGodziny.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            Logger.getGlobal().info(e.getMessage());
        }
        EdycjaGodzinyController edycjaGodzinyController = fxmlLoader.getController();
        GodzinaLekcyjna wybranaGodzinaLekcyjna = cell.getItem();

        if(wybranaGodzinaLekcyjna == null) {
            wybranaGodzinaLekcyjna = new GodzinaLekcyjna();

            //Zakladamy ze wybrany jest poniedzialek
            int index = 0;

            if (cell.getListView().equals(col_wt)) index = 1;
            else if (cell.getListView().equals(col_sr)) index = 2;
            else if (cell.getListView().equals(col_czw)) index = 3;
            else if (cell.getListView().equals(col_pt)) index = 4;


            Dni dzien = col_dzien.getItems().get(index);
            Dzien wybranyDzien = new Dzien(index + 1, dzien.name(), index + 1);

            CzasTrwaniaLekcji wybranyCzasTrwaniaLekcji = col_numer.getItems().get(cell.getIndex());

            wybranaGodzinaLekcyjna.setDzien(wybranyDzien);
            wybranaGodzinaLekcyjna.setCzasTrwaniaLekcji(wybranyCzasTrwaniaLekcji);

        }
        ustawSceneEdycji(edycjaGodzinyController, wybranaGodzinaLekcyjna, parent);

    }

    private void ustawSceneEdycji(EdycjaGodzinyController edycjaGodzinyController, GodzinaLekcyjna wybranaGodzinaLekcyjna, Parent parent) {
        edycjaGodzinyController.setGodzina(wybranaGodzinaLekcyjna);
        edycjaGodzinyController.setPlanZajecController(this);
        edycjaGodzinyController.init();

        Scene scene = new Scene(parent, 680, 600);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }


    private void setCellFactoryCzasTrwania(JFXListView<CzasTrwaniaLekcji> listView) {
        listView.setCellFactory(param -> new JFXListCell<CzasTrwaniaLekcji>(){
            @Override
            protected void updateItem(CzasTrwaniaLekcji item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null) ? " " : item.getCzasRozpoczecia() + " - " + item.getCzasZakonczenia());
                setAlignment(Pos.CENTER);
                setFont(new Font(fontSize));
                setPrefSize(30, godzinaCellHeight);
            }
        });
    }


    private void setCellFactoryNumer(JFXListView<CzasTrwaniaLekcji> listView) {
        listView.setCellFactory(param -> new JFXListCell<CzasTrwaniaLekcji>(){
            @Override
            protected void updateItem(CzasTrwaniaLekcji item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null) ? " " : item.getNumer() + " ");
                setAlignment(Pos.CENTER);
                setFont(new Font(fontSize));
                setPrefSize(30, godzinaCellHeight);
            }
        });
    }

    private void setCellFactoryDzien(JFXListView<Dni> listView) {
        listView.setCellFactory(param -> new JFXListCell<Dni>(){
            @Override
            protected void updateItem(Dni item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null) ? " " : item.name());
                setAlignment(Pos.CENTER);
                setFont(new Font(fontSize));
                setPrefSize(dzienCellWidth,50);
            }
        });
    }

    void zapiszGodzine(GodzinaLekcyjna godzinaLekcyjna) {
        this.planZajec.addGodzinaLekcyjna(godzinaLekcyjna);
        updateTable();

    }

    void usunGodzine(GodzinaLekcyjna godzinaLekcyjna) {
        this.planZajec.deleteGodzinaLekcyjna(godzinaLekcyjna);
        updateTable();
    }

    void setDysponentPlanuZajec(Object dysponentPlanuZajec) {
        this.dysponentPlanuZajec = dysponentPlanuZajec;
    }

    Object getDysponentPlanuZajec() {
        return dysponentPlanuZajec;
    }

    public void generujPdfOnAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage  = (Stage) planZajecPane.getScene().getWindow();
        File file  = fileChooser.showSaveDialog(stage);
        Alert alert;
        if (file != null) {
            PdfGenerator pdfGenerator = new PdfGeneratorImpl();
                try {
                    Logger.getGlobal().info(file.getPath());
                    pdfGenerator.drawTable(file.getPath(), planZajec);
                    alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                    alert.setHeaderText("Pomyślnie wygenerowano pdf'a. ");
                    alert.setTitle("Informacja");
                    alert.showAndWait();

                } catch (Exception ex) {
                    Logger.getGlobal().info(ex.getMessage());
                    alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setHeaderText("Nie udało się wygenerować pdf'a. ");
                    alert.setTitle("Błąd");
                    alert.showAndWait();
                }
        }

    }

    public void wyczyscPlanOnAction() {
        if(czyPotwierdza()) {
            for (Map.Entry<String, List<GodzinaLekcyjna>> entry : planZajec.getPlan().entrySet()) {
                List<GodzinaLekcyjna> godzinaList = entry.getValue();
                for (GodzinaLekcyjna godzina : godzinaList) {
                    if (godzina != null)
                        planZajec.deleteGodzinaLekcyjna(godzina);
                }
            }
            updateTable();
        }
    }


    void synchronizujScrolle() {
        ScrollBar scrollBarNumer = znajdzScrolla(col_numer);

        //Nie ma jednego scrolla, wiec calej reszty tez nie
        if(!scrollBarNumer.isVisible()) return;

        ScrollBar scrollBarCzas = znajdzScrolla(col_czas);
        ScrollBar scrollBarPon = znajdzScrolla(col_pon);
        ScrollBar scrollBarWt = znajdzScrolla(col_wt);
        ScrollBar scrollBarSr = znajdzScrolla(col_sr);
        ScrollBar scrollBarCzw = znajdzScrolla(col_czw);
        ScrollBar scrollBarPt = znajdzScrolla(col_pt);


        bindujScrolla(scrollBarNumer, scrollBarCzas);
        bindujScrolla(scrollBarCzas, scrollBarPon);
        bindujScrolla(scrollBarPon, scrollBarWt);
        bindujScrolla(scrollBarWt, scrollBarSr);
        bindujScrolla(scrollBarSr, scrollBarCzw);
        bindujScrolla(scrollBarCzw, scrollBarPt);
    }

    private ScrollBar znajdzScrolla(ListView listView) {
        return (ScrollBar) listView.lookup(".scroll-bar");
    }

    private void bindujScrolla(ScrollBar scrollBarOne, ScrollBar scrollBarTwo) {
        try {
            scrollBarOne.valueProperty().bindBidirectional(scrollBarTwo.valueProperty());
        } catch (NullPointerException ex) {
            Logger.getGlobal().info(ex.getMessage());
        }
    }


    private boolean czyPotwierdza() {
        ButtonType tak = new ButtonType("Tak", ButtonBar.ButtonData.YES);
        ButtonType nie = new ButtonType("Nie", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING, "",tak, nie);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText("Wykonanie tej akcji spowoduje usunięcie wszystkich powiązanych z tym planem godzin. ");
        alert.setContentText("Czy napewno chcesz to zrobić? ");
        alert.showAndWait();
        return alert.getResult() == tak;
    }


}
