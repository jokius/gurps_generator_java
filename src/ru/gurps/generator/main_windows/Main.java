package ru.gurps.generator.main_windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/views/main.fxml"));
        primaryStage.setTitle("GURPSGenerator");
        primaryStage.setScene(new Scene(root, 641, 224));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}