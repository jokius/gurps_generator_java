package ru.gurps.generator.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.User;

import java.io.IOException;
import java.util.ResourceBundle;

public class AbstractController extends Main {
    public static User user;
    public static Label globalCost;
    public static Stage stage = new Stage();

    protected void createMainStage(){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/main.fxml"));
        loader.setResources(Main.locale);
        stage.setMinWidth(670);
        stage.setMinHeight(560);
        try {
            Parent childrenRoot = loader.load();
            stage.setScene(new Scene(childrenRoot, 670, 540));
            stage.setTitle("GURPSGenerator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createGenerateStage(){
        stage.setResizable(false);
        stage.setMinWidth(516);
        stage.setMinHeight(533);
        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/generateUser.fxml"));
        view.setResources(Main.locale);
        try {
            Parent childrenRoot = view.load();
            stage.setScene(new Scene(childrenRoot, 516, 533));
            stage.setTitle("GURPSGenerator");
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPoints(int points){
        String sPoints = Integer.toString(points);
        globalCost.setText(sPoints);
        user.update_single("currentPoints", sPoints);
        if(Integer.parseInt(user.maxPoints) >= points) globalCost.setTextFill(Color.GREEN);
        else globalCost.setTextFill(Color.RED);
    }

    public int globalCost(){
        return Integer.parseInt(globalCost.getText());
    }
}
