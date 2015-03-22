package ru.gurps.generator.lib;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.models.Addon;
import ru.gurps.generator.models.Feature;

public class ViewsAbstact {
    // global part
    @FXML
    protected TextField maxPoints;

    @FXML
    protected Label currentPoints;

    @FXML
    protected Button userSheet;
    
    
    // params part
    @FXML
    protected TextField sm;

    @FXML
    protected CheckBox noFineManipulators;

    @FXML
    protected TextField st;

    @FXML
    protected Label stCost;

    @FXML
    protected TextField dx;

    @FXML
    protected Label dxCost;

    @FXML
    protected TextField iq;

    @FXML
    protected Label iqCost;

    @FXML
    protected TextField ht;

    @FXML
    protected Label htCost;

    @FXML
    protected TextField hp;

    @FXML
    protected Label hpCost;

    @FXML
    protected TextField will;

    @FXML
    protected Label willCost;

    @FXML
    protected TextField per;

    @FXML
    protected Label perCost;

    @FXML
    protected TextField fp;

    @FXML
    protected Label fpCost;

    @FXML
    protected TextField bs;

    @FXML
    protected Label bsCost;

    @FXML
    protected Label bg;

    @FXML
    protected TextField move;

    @FXML
    protected Label moveCost;

    @FXML
    protected Label doge;

    @FXML
    protected Label thrust;

    @FXML
    protected Label swing;
    
    // advantages part
    @FXML
    protected TableView<Feature> advantagesView;

    @FXML
    protected TableView<Addon> advantagesAddonsTableView;

    @FXML
    protected TableColumn<Feature, String> advantagesTitle = new TableColumn<>("title");

    @FXML
    protected TableColumn<Feature, String> advantagesTitleEn = new TableColumn<>("titleEn");

    @FXML
    protected TableColumn<Feature, String> advantagesType = new TableColumn<>("type");

    @FXML
    protected TableColumn<Feature, String> advantagesCost = new TableColumn<>("cost");

    @FXML
    protected TableColumn<Feature, String> advantagesDescription = new TableColumn<>("description");

    @FXML
    protected AnchorPane advantagesBottomMenu;

    @FXML
    protected ComboBox advantagesLvlComboBox;

    @FXML
    protected Label advantagesLvlLabel;

    @FXML
    protected TextField advantagesLvlText;

    @FXML
    protected Button advantagesAdd;

    @FXML
    protected Button advantagesRemove;

    @FXML
    protected Button advantagesFull;

    @FXML
    protected Label advantagesFinalCost;

    @FXML
    protected TextField advantagesFinalCostText;

    @FXML
    protected TableColumn<Addon, Boolean> advantagesActivate = new TableColumn<>("activate");

    @FXML
    protected TableColumn<Addon, String> advantagesAddonName = new TableColumn<>("title");

    @FXML
    protected TableColumn<Addon, String> advantagesAddonNameEn = new TableColumn<>("title_en");

    @FXML
    protected TableColumn<Addon, String> advantagesAddonLevel= new TableColumn<>("level");

    @FXML
    protected TableColumn<Addon, String> advantagesAddonCost = new TableColumn<>("cost");

    @FXML
    protected CheckBox advantage1CheckBox;

    @FXML
    protected CheckBox advantage2CheckBox;

    @FXML
    protected CheckBox advantage3CheckBox;

    @FXML
    protected CheckBox advantage4CheckBox;

    @FXML
    protected CheckBox advantage5CheckBox;

    @FXML
    protected MenuButton advantagesSearchButton;

    @FXML
    protected MenuItem advantagesSearchAll;

    @FXML
    protected MenuItem advantagesSearchTitle;

    @FXML
    protected MenuItem advantagesSearchTitleEn;

    @FXML
    protected MenuItem advantagesSearchCost;

    @FXML
    protected MenuItem advantagesSearchDescription;

    @FXML
    protected TextField advantagesSearchText;
    
    // disadvantages part
    @FXML
    protected TableView<Feature> disadvantagesView;

    @FXML
    protected TableView<Addon> disadvantagesAddonsTableView;

    @FXML
    protected TableColumn<Feature, String> disadvantagesTitle = new TableColumn<>("title");

    @FXML
    protected TableColumn<Feature, String> disadvantagesTitleEn = new TableColumn<>("title_en");

    @FXML
    protected TableColumn<Feature, String> disadvantagesType = new TableColumn<>("type");

    @FXML
    protected TableColumn<Feature, Integer> disadvantagesCost = new TableColumn<>("cost");

    @FXML
    protected TableColumn<Feature, String> disadvantagesDescription = new TableColumn<>("description");

    @FXML
    protected AnchorPane disadvantagesBottomMenu;

    @FXML
    protected ComboBox disadvantagesLvlComboBox;

    @FXML
    protected Label disadvantagesLvlLabel;

    @FXML
    protected TextField disadvantagesLvlText;

    @FXML
    protected Button disadvantagesAdd;

    @FXML
    protected Button disadvantagesRemove;

    @FXML
    protected Button disadvantagesFull;

    @FXML
    protected Label disadvantagesFinalCost;

    @FXML
    protected TextField disadvantagesFinalCostText;

    @FXML
    protected TableColumn<Addon, Boolean> disadvantagesActivate = new TableColumn<>("activate");

    @FXML
    protected TableColumn<Addon, String> disadvantagesAddonName = new TableColumn<>("title");

    @FXML
    protected TableColumn<Addon, String> disadvantagesAddonNameEn = new TableColumn<>("title_en");

    @FXML
    protected TableColumn<Addon, String> disadvantagesAddonLevel = new TableColumn<>("level");

    @FXML
    protected TableColumn<Addon, String> disadvantagesAddonCost = new TableColumn<>("cost");

    @FXML
    protected CheckBox disadvantage1CheckBox;

    @FXML
    protected CheckBox disadvantage2CheckBox;

    @FXML
    protected CheckBox disadvantage3CheckBox;

    @FXML
    protected CheckBox disadvantage4CheckBox;

    @FXML
    protected CheckBox disadvantage5CheckBox;

    @FXML
    protected MenuButton disadvantagesSearchButton;

    @FXML
    protected MenuItem disadvantagesSearchAll;

    @FXML
    protected MenuItem disadvantagesSearchTitle;

    @FXML
    protected MenuItem disadvantagesSearchTitleEn;

    @FXML
    protected MenuItem disadvantagesSearchCost;

    @FXML
    protected MenuItem disadvantagesSearchDescription;

    @FXML
    protected TextField disadvantagesSearchText;
}
