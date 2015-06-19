package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.gurps.generator.Main;

import java.io.IOException;
import java.lang.reflect.Field;

public class MainWindowController extends AbstractController {
    // global part
    public TextField maxPoints;
    public Label currentPoints;
    public Button userSheet;

    // menus
    public Menu viewMenu;
    public MenuItem newMenuItem;

    // tabs
    public TabPane mainTabPanel;

    public Tab paramsTab;
    public Tab advantagesTab;
    public Tab disadvantagesTab;
    public Tab quirksTab;
    public Tab modesTab;
    public Tab skillsTab;
    public Tab techniquesTab;
    public Tab spellTab;
    public Tab languagesTab;
    public Tab culturasTab;
    public Tab equipmentTab;

    @FXML
    public void initialize() {
        globalCost = currentPoints;

        menus();
        tabsConfigure();
        maxPoints.setText(user.maxPoints);
        globalCost.setText(user.currentPoints);

        if(Integer.parseInt(user.maxPoints) >= Integer.parseInt(user.currentPoints)) globalCost.setTextFill(Color.GREEN);
        else globalCost.setTextFill(Color.RED);

        maxPoints.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                maxPoints.setText(user.maxPoints);
                return;
            }

            if(user.maxPoints.equals(newValue)) return;
            user.update_single("maxPoints", newValue);
            maxPoints.setText(newValue);

            if(Integer.parseInt(user.maxPoints) >= Integer.parseInt(user.currentPoints)) globalCost.setTextFill(Color.GREEN);
            else globalCost.setTextFill(Color.RED);
        });

        userSheet.setOnAction(event -> {
            Stage childrenStage = new Stage();
            userSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> userSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/userSheet.fxml"));
            loader.setResources(Main.locale);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 713, 740));
                childrenStage.setTitle(Main.locale.getString("app_user_sheet"));
                childrenStage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void tabsConfigure(){
        ObservableList<CheckMenuItem> checkMenuItems = FXCollections.observableArrayList();
        for(Field field : this.getClass().getDeclaredFields()){
            if(Tab.class.isAssignableFrom(field.getType())){
                try {
                    Tab tab = (Tab) field.get(this);
                    CheckMenuItem checkMenuItem = new CheckMenuItem(tab.getText());
                    checkMenuItem.setSelected(false);
                    for(Tab viewTab : mainTabPanel.getTabs()){
                        if(viewTab == tab){
                            checkMenuItem.setSelected(true);
                            break;
                        }
                    }

                    checkMenuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue) mainTabPanel.getTabs().add(tab);
                        else mainTabPanel.getTabs().remove(tab);
                    });

                    tab.setOnClosed(event -> checkMenuItem.setSelected(false));
                    checkMenuItems.add(checkMenuItem);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        viewMenu.getItems().addAll(checkMenuItems);
    }

    private void menus(){
        newMenuItem.setOnAction(event -> {
            user = null;
            stage.close();
            usersStage();
        });
    }
}
