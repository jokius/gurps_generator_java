package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import ru.gurps.generator.lib.TabConfigure;

public class MainWindowController extends AbstractController {
    @FXML
    public void initialize() {
        globalCost = currentPoints;

        new TabConfigure(viewMenu, mainTabPanel, paramsTab, advantagesTab, disadvantagesTab, modesTab, skillsTab,
                techniquesTab, spellTab, languagesTab, culturasTab, equipmentTab);

        buttonEvents();
        maxPoints.setText(user.maxPoints);

        
        currentPoints.setText(user.currentPoints);

        maxPoints.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            if("\\D".matches(newValue)) {
                maxPoints.setText(user.maxPoints);
                return;
            }

            if(user.maxPoints.equals(newValue)) return;
            user.maxPoints = newValue;
            maxPoints.setText(newValue);
            user.save();
        });
    }
}
