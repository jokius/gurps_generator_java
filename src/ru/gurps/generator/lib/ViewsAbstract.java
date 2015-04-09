package ru.gurps.generator.lib;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.gurps.generator.models.Addon;
import ru.gurps.generator.models.Cultura;
import ru.gurps.generator.models.Feature;
import ru.gurps.generator.models.Language;

public class ViewsAbstract {
    // global part
    public TextField maxPoints;
    public Label currentPoints;
    public Button userSheet;

    // menus
    public Menu viewMenu;

    // tabs
    public TabPane mainTabPanel;

    public Tab paramsTab;
    public Tab advantagesTab;
    public Tab disadvantagesTab;
    public Tab modesTab;
    public Tab skillsTab;
    public Tab techniquesTab;
    public Tab spellTab;
    public Tab languagesTab;
    public Tab culturasTab;
    public Tab equipmentTab;
    
    // advantages part
    public TableView<Feature> advantagesView;
    public TableView<Addon> advantagesAddonsTableView;
    public TableColumn<Feature, String> advantagesTitle;
    public TableColumn<Feature, String> advantagesTitleEn;
    public TableColumn<Feature, String> advantagesType;
    public TableColumn<Feature, String> advantagesCost;
    public TableColumn<Feature, String> advantagesDescription;

    public AnchorPane advantagesBottomMenu;
    public ComboBox advantagesLvlComboBox;
    public Label advantagesLvlLabel;
    public TextField advantagesLvlText;
    public Button advantagesAdd;
    public Button advantagesRemove;
    public Button advantagesFull;
    public Label advantagesFinalCost;
    public TextField advantagesFinalCostText;
    public TableColumn<Addon, Boolean> advantagesActivate;
    public TableColumn<Addon, String> advantagesAddonName;
    public TableColumn<Addon, String> advantagesAddonNameEn;
    public TableColumn<Addon, String> advantagesAddonLevel;
    public TableColumn<Addon, String> advantagesAddonCost;

    public CheckBox advantage1CheckBox;
    public CheckBox advantage2CheckBox;
    public CheckBox advantage3CheckBox;
    public CheckBox advantage4CheckBox;
    public CheckBox advantage5CheckBox;

    public MenuButton advantagesSearchButton;
    public MenuItem advantagesSearchAll;
    public MenuItem advantagesSearchTitle;
    public MenuItem advantagesSearchTitleEn;
    public MenuItem advantagesSearchCost;
    public MenuItem advantagesSearchDescription;
    public TextField advantagesSearchText;
    
    // disadvantages part
    public TableView<Feature> disadvantagesView;
    public TableView<Addon> disadvantagesAddonsTableView;
    public TableColumn<Feature, String> disadvantagesTitle;
    public TableColumn<Feature, String> disadvantagesTitleEn;
    public TableColumn<Feature, String> disadvantagesType;
    public TableColumn<Feature, Integer> disadvantagesCost;
    public TableColumn<Feature, String> disadvantagesDescription;

    public AnchorPane disadvantagesBottomMenu;
    public ComboBox disadvantagesLvlComboBox;
    public Label disadvantagesLvlLabel;
    public TextField disadvantagesLvlText;
    public Button disadvantagesAdd;
    public Button disadvantagesRemove;
    public Button disadvantagesFull;
    public Label disadvantagesFinalCost;
    public TextField disadvantagesFinalCostText;
    public TableColumn<Addon, Boolean> disadvantagesActivate;
    public TableColumn<Addon, String> disadvantagesAddonName;
    public TableColumn<Addon, String> disadvantagesAddonNameEn;
    public TableColumn<Addon, String> disadvantagesAddonLevel;
    public TableColumn<Addon, String> disadvantagesAddonCost;

    public CheckBox disadvantage1CheckBox;
    public CheckBox disadvantage2CheckBox;
    public CheckBox disadvantage3CheckBox;
    public CheckBox disadvantage4CheckBox;
    public CheckBox disadvantage5CheckBox;

    public MenuButton disadvantagesSearchButton;
    public MenuItem disadvantagesSearchAll;
    public MenuItem disadvantagesSearchTitle;
    public MenuItem disadvantagesSearchTitleEn;
    public MenuItem disadvantagesSearchCost;
    public MenuItem disadvantagesSearchDescription;
    public TextField disadvantagesSearchText;

    //languages part
    public TableView<Language> languagesTableView;
    public TableColumn<Language, String> languagesNameColumn;
    public TableColumn<Language, String> languageSpokenColumn;
    public TableColumn<Language, String> languagesWrittenColumn;
    public TableColumn<Language, String> languagesCostColumn;
    public TableColumn<Language, Boolean> languagesUserColumn;
    public TableColumn<Language, Boolean> languagesDbColumn;

    public TextField languageNameText;
    public ChoiceBox languageSpokenChoiceBox;
    public ChoiceBox languageWrittenChoiceBox;
    public TextField languageCostText;
    public Button languageAddButton;

    //culturas part

    public TableView<Cultura> culturasTableView;
    public TableColumn<Cultura, String> culturasNameColumn;
    public TableColumn<Cultura, String> culturasCostColumn;
    public TableColumn<Cultura, Boolean> culturasUserColumn;
    public TableColumn<Cultura, Boolean> culturasDbColumn;

    public TextField culturaNameText;
    public TextField culturaCostText;
    public Button culturaAddButton;
}
