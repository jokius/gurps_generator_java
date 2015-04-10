package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.lib.FeatureEventHandler;
import ru.gurps.generator.models.Addon;
import ru.gurps.generator.models.Feature;

import java.util.ArrayList;

public class FeaturesAbstractController extends AbstractController {
    private TableView<Feature> view;
    private TableView<Addon> tableView;
    private TableColumn<Feature, String> title;
    private TableColumn<Feature, String> titleEn;
    private TableColumn<Feature, String> type;
    private TableColumn<Feature, String> cost;
    private TableColumn<Feature, String> description;

    private AnchorPane bottomMenu;
    private ComboBox lvlComboBox;
    private Label lvlLabel;
    private TextField lvlText;
    private Button add;
    private Button remove;
    private Button full;
    private Label finalCost;
    private TextField finalCostText;
    private TableColumn<Addon, Boolean> activate;
    private TableColumn<Addon, String> addonName;
    private TableColumn<Addon, String> addonNameEn;
    private TableColumn<Addon, String> addonLevel;
    private TableColumn<Addon, String> addonCost;

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;

    private MenuButton searchButton;
    private MenuItem searchAll;
    private MenuItem searchTitle;
    private MenuItem searchTitleEn;
    private MenuItem searchCost;
    private MenuItem searchDescription;
    private TextField searchText;
    private boolean isAdvantage;

    protected ArrayList<Integer> featuresNumbers = new ArrayList<>();
    protected ObservableList<Feature> data = FXCollections.observableArrayList();

    public FeaturesAbstractController(TableView<Feature> view, TableView<Addon> tableView, TableColumn<Feature, String> title, TableColumn<Feature, String> titleEn, TableColumn<Feature, String> type, TableColumn<Feature, String> cost, TableColumn<Feature, String> description, AnchorPane bottomMenu, ComboBox lvlComboBox, Label lvlLabel, TextField lvlText, Button add, Button remove, Button full, Label finalCost, TextField finalCostText, TableColumn<Addon, Boolean> activate, TableColumn<Addon, String> addonName, TableColumn<Addon, String> addonNameEn, TableColumn<Addon, String> addonLevel, TableColumn<Addon, String> addonCost, CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3, CheckBox checkBox4, CheckBox checkBox5, MenuButton searchButton, MenuItem searchAll, MenuItem searchTitle, MenuItem searchTitleEn, MenuItem searchCost, MenuItem searchDescription, TextField searchText, boolean isAdvantage) {
        this.view = view;
        this.tableView = tableView;
        this.title = title;
        this.titleEn = titleEn;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.bottomMenu = bottomMenu;
        this.lvlComboBox = lvlComboBox;
        this.lvlLabel = lvlLabel;
        this.lvlText = lvlText;
        this.add = add;
        this.remove = remove;
        this.full = full;
        this.finalCost = finalCost;
        this.finalCostText = finalCostText;
        this.activate = activate;
        this.addonName = addonName;
        this.addonNameEn = addonNameEn;
        this.addonLevel = addonLevel;
        this.addonCost = addonCost;
        this.checkBox1 = checkBox1;
        this.checkBox2 = checkBox2;
        this.checkBox3 = checkBox3;
        this.checkBox4 = checkBox4;
        this.checkBox5 = checkBox5;
        this.searchButton = searchButton;
        this.searchAll = searchAll;
        this.searchTitle = searchTitle;
        this.searchTitleEn = searchTitleEn;
        this.searchCost = searchCost;
        this.searchDescription = searchDescription;
        this.searchText = searchText;
        this.isAdvantage = isAdvantage;
        for(int i = 1; 5 >= i; i++) featuresNumbers.add(i);

        run();
    }

    private void run(){
        data.addAll(new Feature().where("advantage", isAdvantage));
        FeatureEventHandler featureEventHandler = new FeatureEventHandler(user, data, view, tableView,
                bottomMenu, add, remove, activate, addonName, addonNameEn, addonLevel, addonCost, full, finalCost,
                lvlLabel, lvlText, lvlComboBox, finalCostText, globalCost);

        view.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, featureEventHandler);
            return row;
        });

        setFeatures();
        setSearch();
        checkBoxEvents();
    }

    private void setFeatures(){
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        if(isAdvantage) view.setPlaceholder(new Label("Преимуществ нет"));
        else view.setPlaceholder(new Label("Недостаков нет"));
        view.setItems(data);
    }

    protected void setSearch(){
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) searchButton.setDisable(true);
            else searchButton.setDisable(false);
        });

        searchAll.setOnAction(event -> {
            String query = "advantage=" + isAdvantage + " and UPPER(title) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(titleEn) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(cost) like UPPER('%" + searchText.getText() + "%') or " +
                    "advantage=" + isAdvantage + " and UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            view.setItems(new Feature().where(query));
        });

        for(String feature : new String[] {"Title", "TitleEn", "Cost", "Description"}){
            try {
                MenuItem menuItem = (MenuItem) this.getClass().getDeclaredField("search" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage="+ isAdvantage +" and UPPER("+ feature + ") like UPPER('%" + searchText.getText() + "%')";
                    view.setItems(new Feature().where(query));
                });
            } catch(IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkBoxEvents(){
        Integer[] numbers = {1, 2, 3, 4, 5};
        for(Integer number : numbers) {
            try {
                CheckBox checkBox = (CheckBox) this.getClass().getDeclaredField("checkBox" + number).get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=" + isAdvantage + " and type like ";
                    if(newValue) featuresNumbers.add(number);
                    else featuresNumbers.remove(number);
                    for(Integer lNumber : featuresNumbers) {
                        if(query.equals("advantage=" + isAdvantage + " and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=" + isAdvantage + " and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=" + isAdvantage + " and type like ")) query = "advantage=" + isAdvantage + " and type='6'";
                    view.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
