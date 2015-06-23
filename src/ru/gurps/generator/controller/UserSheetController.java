package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.lib.Dmg;
import ru.gurps.generator.lib.UserParams;
import ru.gurps.generator.lib.export.ExcelJokSheetFormat;
import ru.gurps.generator.lib.export.ExportToJson;
import ru.gurps.generator.models.*;

import java.io.File;
import java.lang.reflect.Field;

public class UserSheetController extends AbstractController {
    private User user = AbstractController.user;
    public Label name;
    public Label player;
    public Label maxPoints;
    public Label remainingPoints;

    public Label growth;
    public Label weight;
    public Label age;

    public Label st;
    public Label stCost;
    public Label dx;
    public Label dxCost;
    public Label iq;
    public Label iqCost;
    public Label ht;
    public Label htCost;

    public Label hp;
    public Label hpCost;
    public Label will;
    public Label willCost;
    public Label per;
    public Label perCost;
    public Label fp;
    public Label fpCost;

    public Label bl;
    public Label bs;
    public Label doge;

    public Label move;
    public Label thrust;
    public Label swing;

    public Label tl;
    public Label tlCost;

    public TextField head;
    public TextField torse;
    public TextField arm;
    public TextField leg;
    public TextField hand;
    public TextField foot;

    public Label block;
    public Label parry;

    private ObservableList<Feature> advantagesData = FXCollections.observableArrayList();
    public TableView<Feature> advantagesTableView;
    public TableColumn<Feature, String> advantagesNameColumn;
    public TableColumn<Feature, String> advantagesCostColumn;

    private ObservableList<Feature> disadvantagesData = FXCollections.observableArrayList();
    public TableView<Feature> disadvantagesTableView;
    public TableColumn<Feature, String> disadvantagesNameColumn;
    public TableColumn<Feature, String> disadvantagesCostColumn;

    public TableView<Skill> skillsTableView;
    public TableColumn<Skill, String> skillsName;
    public TableColumn<Skill, String> skillsTypeColumn;
    public TableColumn<Skill, Integer> skillsLevelColumn;
    public TableColumn<Skill, Integer> skillsCostColumn;

    public TableView<Spell> spellsTableView;
    public TableColumn<Spell, String> spellsNameColumn;
    public TableColumn<Spell, String> spellsComplexityColumn;
    public TableColumn<Spell, Integer> spellsCostColumn;

    public TableView<Language> languagesTableView;
    public TableColumn<Language, String> languageNameColumn;
    public TableColumn<Language, String> spokenColumn;
    public TableColumn<Language, String> writtenColumn;
    public TableColumn<Language, Integer> languageCostColumn;

    public TableView<Cultura> culturasTableView;
    public TableColumn<Cultura, String> culturaNameColumn;
    public TableColumn<Cultura, Integer> culturaCostColumn;

    public Button jokXlsxButton;
    public Button jsonButton;

    @FXML
    private void initialize() {
        name.setText(user.name);
        player.setText(user.getPlayer());
        maxPoints.setText(user.maxPoints);
        setRemainingPoints();

        growth.setText(Integer.toString(user.growth));
        weight.setText(Integer.toString(user.weight));
        age.setText(Integer.toString(user.age));

        st.setText(Integer.toString(user.st));
        dx.setText(Integer.toString(user.dx));
        iq.setText(Integer.toString(user.iq));
        ht.setText(Integer.toString(user.ht));

        hp.setText(Integer.toString(user.hp));
        will.setText(Integer.toString(user.will));
        per.setText(Integer.toString(user.per));
        fp.setText(Integer.toString(user.fp));

        bs.setText(Double.toString(user.bs));
        move.setText(Integer.toString(user.move));

        tl.setText(Integer.toString(user.tl));
        tlCost.setText(Integer.toString(user.tlCost));

        stCost.setText(Integer.toString(UserParams.stCost()));
        dxCost.setText(Integer.toString(UserParams.dxCost()));
        iqCost.setText(Integer.toString(UserParams.iqCost()));
        htCost.setText(Integer.toString(UserParams.htCost()));

        hpCost.setText(Integer.toString(UserParams.hpCost()));
        willCost.setText(Integer.toString(UserParams.willCost()));
        perCost.setText(Integer.toString(UserParams.perCost()));
        fpCost.setText(Integer.toString(UserParams.fpCost()));

        bl.setText(Integer.toString(UserParams.bg()));
        doge.setText(Integer.toString(UserParams.doge()));

        thrust.setText(Dmg.thrust(user.st));
        swing.setText(Dmg.swing(user.st));

        head.setText(Integer.toString(user.head));
        torse.setText(Integer.toString(user.torse));
        arm.setText(Integer.toString(user.arm));
        leg.setText(Integer.toString(user.leg));
        hand.setText(Integer.toString(user.hand));
        foot.setText(Integer.toString(user.foot));

        initFeatures();
        initSkills();
        intSpells();
        initLanguages();
        initCultures();

        setTextProperty();

        exportButtons();
    }

    private void initFeatures() {
        ObservableList<Feature> features = user.features();
        for(Feature feature : features) {
            if(feature.advantage) advantagesData.add(feature);
            else disadvantagesData.add(feature);
        }

        advantagesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        advantagesNameColumn.setCellFactory(param -> new FeatureFullName());
        advantagesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesTableView.setItems(advantagesData);
        advantagesTableView.setPlaceholder(new Label(Main.locale.getString("advantages_not_found")));

        disadvantagesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        advantagesNameColumn.setCellFactory(param -> new FeatureFullName());
        disadvantagesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesTableView.setItems(disadvantagesData);
        disadvantagesTableView.setPlaceholder(new Label(Main.locale.getString("disadvantages_not_found")));
    }

    private void initSkills() {
        ObservableList<Skill> skills = user.skills();
        skillsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        skillsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeAndComplexity"));
        skillsLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        skillsCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        skillsTableView.setItems(skills);
        skillsTableView.setPlaceholder(new Label(Main.locale.getString("skills_not_found")));
        parry.setText(UserParams.getParry(skills));
        parry.setText(UserParams.getBlock(skills));
    }

    private void intSpells() {
        spellsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spellsComplexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        spellsCostColumn.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        spellsTableView.setItems(user.spells());
        spellsTableView.setPlaceholder(new Label(Main.locale.getString("spells_not_found")));
    }

    private void initLanguages() {
        languageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        writtenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        languageCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        languagesTableView.setItems(user.languages());
        languagesTableView.setPlaceholder(new Label(Main.locale.getString("languages_not_found")));
    }

    private void initCultures() {
        culturaNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        culturaCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        culturasTableView.setItems(user.cultures());
        culturasTableView.setPlaceholder(new Label(Main.locale.getString("cultures_not_found")));
    }

    private void setTextProperty() {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(TextField.class.isAssignableFrom(field.getType())) {
                try {
                    TextField textField = (TextField) field.get(this);
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.equals("")) return;
                        try {
                            Field setField = user.getClass().getDeclaredField(field.getName());

                            if(field.getName().equals("tlCost")) {
                                if(newValue.equals("") || newValue.equals("-")) return;
                                try {
                                    int intNewValue = Integer.parseInt(newValue);
                                    if(user.tlCost == intNewValue) return;
                                    setCurrentPoints(Integer.parseInt(user.currentPoints) + intNewValue - user.tlCost);
                                    user.tlCost = intNewValue;
                                    user.save();
                                    setRemainingPoints();
                                } catch(NumberFormatException e) {
                                    tlCost.setText(Integer.toString(user.tlCost));
                                }
                            }
                            if(Integer.class.isAssignableFrom(setField.getType()))
                                setField.set(user, Integer.parseInt(newValue));
                            else setField.set(user, newValue);
                        } catch(IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        user.save();

                    });
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setRemainingPoints() {
        int remaining = Integer.parseInt(user.maxPoints) - Integer.parseInt(user.currentPoints);
        remainingPoints.setText(Integer.toString(remaining));
        if(remaining > 0) remainingPoints.setTextFill(Color.GREEN);
        else remainingPoints.setTextFill(Color.RED);
    }

    private void exportButtons() {
        jokXlsxButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Main.locale.getString("save_sheet"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(user.name);
            File file = fileChooser.showSaveDialog(new Stage());
            new ExcelJokSheetFormat(file);
        });

        jsonButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Main.locale.getString("save_sheet"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(user.name);
            File file = fileChooser.showSaveDialog(new Stage());
            new ExportToJson(file);
        });
    }

    private class FeatureFullName extends TableCell<Feature, String> {
        public FeatureFullName() {
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(empty) return;
            Feature feature = (Feature) getTableRow().getItem();
            if (feature == null) return;
            setText(UserParams.featureFullNameRu(feature));
        }
    }
}