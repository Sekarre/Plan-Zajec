package com.planzajec;

import com.planzajec.preloader.ScreenPreloader;
import javafx.application.Application;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ScreenPreloader screenPreloader = new ScreenPreloader(primaryStage);
        screenPreloader.startPreloader();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
