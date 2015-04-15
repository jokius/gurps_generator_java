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

public class AbstractController extends Main {
    public static User user;
    public static Label globalCost;

    protected void createMainStage(){
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/main.fxml"));
        childrenStage.setMinWidth(670);
        childrenStage.setMinHeight(560);
        try {
            Parent childrenRoot = loader.load();
            childrenStage.setScene(new Scene(childrenRoot, 670, 540));
            childrenStage.setTitle("GURPSGenerator");
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createGenerateStage(){
        Stage childrenStage = new Stage();
        childrenStage.setResizable(false);

        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/generateUser.fxml"));
        GenerateUserController controller = new GenerateUserController(childrenStage);
        view.setController(controller);
        try {
            Parent childrenRoot = view.load();
            childrenStage.setScene(new Scene(childrenRoot, 395, 260));
            childrenStage.setTitle("GURPSGenerator");
            childrenStage.show();
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
}
