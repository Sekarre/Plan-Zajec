package com.planzajec.preloader;

import com.planzajec.dao.Impl.GodzinaLekcyjnaDaoImpl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class ScreenPreloader {

    private Task<Boolean> loadAndCreate;
    private Stage stage;

    public ScreenPreloader(Stage primaryStage) {
        this.stage = new Stage();

        loadAndCreate = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                final Boolean[] result = {true};
                Platform.runLater(() -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/glowna/splashScreen.fxml"));
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (Exception ex) {
                        Logger.getGlobal().info("Nie wczytano splashScreena");
                        zamknijProgram("Wystapił błąd podczas ładowania.\nSpróbuj ponownie. ");
                        result[0] = false;
                    }
                });

                try {
                    new GodzinaLekcyjnaDaoImpl().size();
                } catch (ExceptionInInitializerError e) {
                    Logger.getGlobal().info(e.getMessage());
                    if (result[0]) zamknijProgram("Wystąpił błąd podczas ładowania. \n\nSprawdź połączenie z internetem i spróbuj ponownie. ");
                    result[0] = false;
                }

                return result[0];
            }

            @Override
            protected void succeeded() {
                if (this.getValue()) {
                    try {
                        Pane paneK = wczytaj("/view/glowna/tabelkaKlasaPlanZajec.fxml");
                        Pane paneN = wczytaj("/view/glowna/tabelkaNauczycielPlanZajec.fxml");
                        Pane paneC = wczytaj("/view/glowna/zarzadzajCzasemTrwaniaLekcji.fxml");
                        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/glowna/stronaGlowna.fxml"));
                        Pane mainPane = mainLoader.load();
                        primaryStage.setTitle("Plan Zajec");
                        primaryStage.setScene(new Scene(mainPane));
                        primaryStage.setMinHeight(945);
                        primaryStage.setMinWidth(1590);
                        primaryStage.setResizable(false);
                        stage.hide();
                        rysuj(paneK);
                        rysuj(paneN);
                        rysuj(paneC);

                        primaryStage.show();
                        stage.close();

                    } catch (Exception ex) {
                        Logger.getGlobal().info(ex.getMessage());
                        zamknijProgram("Wystapił błąd podczas ładowania.\nSpróbuj ponownie. ");
                    }
                }
            }
        };
    }

    public void startPreloader() {
        new Thread(loadAndCreate).start();
    }


    private Pane wczytaj(String resource) {
        Pane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            pane = loader.load();
        } catch (Exception ex) {
            Logger.getGlobal().info(ex.getMessage());
        }
        return pane;
    }

    private void rysuj(Pane pane) {
        stage.setScene(new Scene(pane));
    }

    private void zamknijProgram(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "");
            alert.setHeaderText(message);
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        });
    }

}
