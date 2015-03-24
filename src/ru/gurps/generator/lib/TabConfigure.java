package ru.gurps.generator.lib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.lang.reflect.Field;

public class TabConfigure {
    private Menu viewMenu;
    private TabPane mainTabPanel;
    private Tab paramsTab;
    private Tab advantagesTab;
    private Tab disadvantagesTab;
    private Tab modesTab;
    private Tab skillsTab;
    private Tab techniquesTab;
    private Tab spellTab;
    private Tab languagesTab;
    private Tab culturasTab;
    private Tab equipmentTab;

    public TabConfigure(Menu viewMenu, TabPane mainTabPanel, Tab paramsTab, Tab advantagesTab, Tab disadvantagesTab,
                        Tab modesTab, Tab skillsTab, Tab techniquesTab, Tab spellTab, Tab languagesTab, Tab culturasTab,
                        Tab equipmentTab) {
        this.viewMenu = viewMenu;
        this.mainTabPanel = mainTabPanel;
        this.paramsTab = paramsTab;
        this.advantagesTab = advantagesTab;
        this.disadvantagesTab = disadvantagesTab;
        this.modesTab = modesTab;
        this.skillsTab = skillsTab;
        this.techniquesTab = techniquesTab;
        this.spellTab = spellTab;
        this.languagesTab = languagesTab;
        this.culturasTab = culturasTab;
        this.equipmentTab = equipmentTab;
        run();
    }

    private void run(){
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
}
