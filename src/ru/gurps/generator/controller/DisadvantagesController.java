package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.models.Addon;
import ru.gurps.generator.models.Feature;

public class DisadvantagesController {
    public TableView<Feature> view;
    public TableView<Addon> tableView;
    public TableColumn<Feature, String> title;
    public TableColumn<Feature, String> titleEn;
    public TableColumn<Feature, String> type;
    public TableColumn<Feature, String> cost;
    public TableColumn<Feature, String> description;

    public AnchorPane bottomMenu;
    public ComboBox lvlComboBox;
    public Label lvlLabel;
    public TextField lvlText;
    public Button add;
    public Button remove;
    public Button full;
    public Label finalCost;
    public TextField finalCostText;
    public TableColumn<Addon, Boolean> activate;
    public TableColumn<Addon, String> addonName;
    public TableColumn<Addon, String> addonNameEn;
    public TableColumn<Addon, String> addonLevel;
    public TableColumn<Addon, String> addonCost;

    public CheckBox checkBox1;
    public CheckBox checkBox2;
    public CheckBox checkBox3;
    public CheckBox checkBox4;
    public CheckBox checkBox5;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchTitle;
    public MenuItem searchTitleEn;
    public MenuItem searchCost;
    public MenuItem searchDescription;
    public TextField searchText;

    @FXML
    private void initialize() {
        new FeaturesAbstractController(view, tableView, title, titleEn, type, cost, description, bottomMenu, lvlComboBox,
                lvlLabel, lvlText, add, remove, full, finalCost, finalCostText, activate, addonName, addonNameEn,
                addonLevel, addonCost, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, searchButton, searchAll,
                searchTitle, searchTitleEn, searchCost, searchDescription, searchText, false);
    }
}
