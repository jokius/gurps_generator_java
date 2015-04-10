package ru.gurps.generator.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.lib.*;
import ru.gurps.generator.models.*;
import java.io.IOException;

public class AbstractController extends ViewsAbstract {
    public static User user;
    public static Label globalCost;

    protected void cellEvents() {

        new LanguagesTable(languagesTableView, languagesNameColumn, languageSpokenColumn, languagesWrittenColumn,
                languagesCostColumn, languagesUserColumn, languagesDbColumn, languageNameText, languageSpokenChoiceBox,
                languageWrittenChoiceBox, languageCostText, languageAddButton, currentPoints);

        new CulturasTable(culturasTableView, culturasNameColumn, culturasCostColumn, culturasUserColumn, culturasDbColumn,
                culturaNameText, culturaCostText, culturaAddButton, currentPoints);
    }

    protected void buttonEvents() {
        userSheet.setOnAction(event -> {
            Stage childrenStage = new Stage();
            userSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> userSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/userSheet.fxml"));
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 700, 795));
                childrenStage.setTitle("GURPS Лист персонажа");
                childrenStage.show();
                childrenStage.setResizable(false);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
