package ru.gurps.generator.lib;

import javafx.scene.control.*;
import ru.gurps.generator.models.Cultura;
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
