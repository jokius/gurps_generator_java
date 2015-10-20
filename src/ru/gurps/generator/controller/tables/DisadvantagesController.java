package ru.gurps.generator.controller.tables;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.controller.helpers.FeaturesAbstractController;
import ru.gurps.generator.models.rules.Addon;
import ru.gurps.generator.models.rules.Feature;

public class DisadvantagesController extends AbstractController {
    public TableView<Feature> tableView;
    public TableView<Addon> addonsTableView;
    public TableColumn<Feature, String> name;
    public TableColumn<Feature, String> nameEn;
    public TableColumn<Feature, String> featureType;
    public TableColumn<Feature, String> cost;
    public TableColumn<Feature, String> description;

    public AnchorPane bottomMenu;
    public ComboBox<Integer> lvlComboBox;
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

    public CheckMenuItem checkBox1;
    public CheckMenuItem checkBox2;
    public CheckMenuItem checkBox3;
    public CheckMenuItem checkBox4;
    public CheckMenuItem checkBox5;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchCost;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;

    @FXML
    private void initialize() {
        new FeaturesAbstractController(tableView, addonsTableView, name, nameEn, featureType, cost, description, bottomMenu, lvlComboBox,
                lvlLabel, lvlText, add, remove, full, finalCost, finalCostText, activate, addonName, addonNameEn,
                addonLevel, addonCost, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, searchButton, searchAll,
                searchName, searchNameEn, searchCost, searchDescription, reset, searchText, false);
    }
}
