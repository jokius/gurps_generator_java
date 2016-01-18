package ru.gurps.generator.desktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;

import java.io.IOException;
import java.lang.reflect.Field;

public class ParentController extends AbstractController {
    // global part
    public TextField maxPoints;
    public Label currentPoints;
    public Button characterSheet;

    // menus
    public Menu viewMenu;
    public MenuItem newMenuItem;
    public MenuItem checkNewVersion;

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
    public Tab alchemiesTab;
    public Tab languagesTab;
    public Tab culturasTab;
    public Tab equipmentTab;

    @FXML
    public void initialize() {
        globalCost = currentPoints;

        menus();
        tabsConfigure();
        maxPoints.setText(character.maxPoints);
        globalCost.setText(character.currentPoints);

        if(Integer.parseInt(character.maxPoints) >= Integer.parseInt(character.currentPoints)) globalCost.setTextFill(Color.GREEN);
        else globalCost.setTextFill(Color.RED);

        maxPoints.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if(!newValue.matches("\\d+")) {
                maxPoints.setText(character.maxPoints);
                return;
            }

            if(character.maxPoints.equals(newValue)) return;
            character.update_single("maxPoints", newValue);
            maxPoints.setText(newValue);

            if(Integer.parseInt(character.maxPoints) >= Integer.parseInt(character.currentPoints)) globalCost.setTextFill(Color.GREEN);
            else globalCost.setTextFill(Color.RED);
        });

        characterSheet.setOnAction(event -> {
            Stage childrenStage = new Stage();
            characterSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> characterSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/characters/sheet.fxml"));
            loader.setResources(Main.locale);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 713, 740));
                childrenStage.setTitle(Main.locale.getString("app_character_sheet"));
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
            character = null;
            stage.close();
            charactersStage();
        });

        checkNewVersion.setOnAction(event -> {
            Stage childrenStage = new Stage();
            characterSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> characterSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/newVersion.fxml"));
            loader.setResources(Main.locale);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 255, 98));
                childrenStage.setResizable(false);
                childrenStage.setTitle(Main.locale.getString("check_new_version"));
                childrenStage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
