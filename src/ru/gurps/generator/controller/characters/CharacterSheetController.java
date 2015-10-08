package ru.gurps.generator.controller.characters;

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
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.lib.Dmg;
import ru.gurps.generator.lib.CharacterParams;
import ru.gurps.generator.lib.export.ExcelJokSheetFormat;
import ru.gurps.generator.lib.export.ExportToJson;
import ru.gurps.generator.models.Character;
import ru.gurps.generator.models.rules.*;

import java.io.File;
import java.lang.reflect.Field;

public class CharacterSheetController extends AbstractController {
    private Character character = AbstractController.character;
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
        name.setText(character.name);
        player.setText(character.getPlayer());
        maxPoints.setText(character.maxPoints);
        setRemainingPoints();

        growth.setText(Integer.toString(character.growth));
        weight.setText(Integer.toString(character.weight));
        age.setText(Integer.toString(character.age));

        st.setText(Integer.toString(character.st));
        dx.setText(Integer.toString(character.dx));
        iq.setText(Integer.toString(character.iq));
        ht.setText(Integer.toString(character.ht));

        hp.setText(Integer.toString(character.hp));
        will.setText(Integer.toString(character.will));
        per.setText(Integer.toString(character.per));
        fp.setText(Integer.toString(character.fp));

        bs.setText(Double.toString(character.bs));
        move.setText(Integer.toString(character.move));

        tl.setText(Integer.toString(character.tl));
        tlCost.setText(Integer.toString(character.tlCost));

        stCost.setText(Integer.toString(CharacterParams.stCost()));
        dxCost.setText(Integer.toString(CharacterParams.dxCost()));
        iqCost.setText(Integer.toString(CharacterParams.iqCost()));
        htCost.setText(Integer.toString(CharacterParams.htCost()));

        hpCost.setText(Integer.toString(CharacterParams.hpCost()));
        willCost.setText(Integer.toString(CharacterParams.willCost()));
        perCost.setText(Integer.toString(CharacterParams.perCost()));
        fpCost.setText(Integer.toString(CharacterParams.fpCost()));

        bl.setText(Integer.toString(CharacterParams.bg()));
        doge.setText(Integer.toString(CharacterParams.doge()));

        thrust.setText(Dmg.thrust(character.st));
        swing.setText(Dmg.swing(character.st));

        head.setText(Integer.toString(character.head));
        torse.setText(Integer.toString(character.torse));
        arm.setText(Integer.toString(character.arm));
        leg.setText(Integer.toString(character.leg));
        hand.setText(Integer.toString(character.hand));
        foot.setText(Integer.toString(character.foot));

        initFeatures();
        initSkills();
        intSpells();
        initLanguages();
        initCultures();

        setTextProperty();

        exportButtons();
    }

    private void initFeatures() {
        ObservableList<Feature> features = character.features();
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
        ObservableList<Skill> skills = character.skills();
        skillsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        skillsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("typeAndComplexity"));
        skillsLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        skillsCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        skillsTableView.setItems(skills);
        skillsTableView.setPlaceholder(new Label(Main.locale.getString("skills_not_found")));
        parry.setText(CharacterParams.getParry(skills));
        block.setText(CharacterParams.getBlock(skills));
    }

    private void intSpells() {
        spellsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spellsComplexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        spellsCostColumn.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        spellsTableView.setItems(character.spells());
        spellsTableView.setPlaceholder(new Label(Main.locale.getString("spells_not_found")));
    }

    private void initLanguages() {
        languageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        writtenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        languageCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        languagesTableView.setItems(character.languages());
        languagesTableView.setPlaceholder(new Label(Main.locale.getString("languages_not_found")));
    }

    private void initCultures() {
        culturaNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        culturaCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        culturasTableView.setItems(character.cultures());
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
                            Field setField = character.getClass().getDeclaredField(field.getName());

                            if(field.getName().equals("tlCost")) {
                                if(newValue.equals("") || newValue.equals("-")) return;
                                try {
                                    int intNewValue = Integer.parseInt(newValue);
                                    if(character.tlCost == intNewValue) return;
                                    setCurrentPoints(Integer.parseInt(character.currentPoints) + intNewValue - character.tlCost);
                                    character.tlCost = intNewValue;
                                    character.save();
                                    setRemainingPoints();
                                } catch(NumberFormatException e) {
                                    tlCost.setText(Integer.toString(character.tlCost));
                                }
                            }
                            if(Integer.class.isAssignableFrom(setField.getType()))
                                setField.set(character, Integer.parseInt(newValue));
                            else setField.set(character, newValue);
                        } catch(IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        character.save();

                    });
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setRemainingPoints() {
        int remaining = Integer.parseInt(character.maxPoints) - Integer.parseInt(character.currentPoints);
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
            fileChooser.setInitialFileName(character.name);
            File file = fileChooser.showSaveDialog(new Stage());
            new ExcelJokSheetFormat(file);
        });

        jsonButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Main.locale.getString("save_sheet"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(character.name);
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
            if(empty){
                setText(null);
                setGraphic(null);
                return;
            }
            Feature feature = (Feature) getTableRow().getItem();
            if (feature == null) return;
            setText(CharacterParams.featureFullNameRu(feature));
        }
    }
}