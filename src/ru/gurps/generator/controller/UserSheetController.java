package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import ru.gurps.generator.lib.UserParams;
import ru.gurps.generator.lib.export.ExcelJokSheetFormat;
import ru.gurps.generator.lib.export.ExportToJson;
import ru.gurps.generator.models.*;

import java.io.File;
import java.lang.reflect.Field;

public class UserSheetController {
    private User user = AbstractController.user;
    private UserParams userParams;

    public Label name;
    public TextField player;
    public Label maxPoints;
    public Label remainingPoints;

    public TextField growth;
    public TextField weight;
    public TextField age;

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

    public TextField tl;
    public TextField tlCost;

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
    public TableColumn<Skill, String> skillsComplexityColumn;
    public TableColumn<Skill, Integer> skillsLevelColumn;

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
        userParams = new UserParams(stCost, dxCost, iqCost, htCost, hpCost, willCost, perCost, fpCost, null,
                null, bl, doge, thrust, swing, block, parry);

        name.setText(user.name);
        player.setText(user.player);
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

        userParams.setSt();
        userParams.setDx();
        userParams.setIq();
        userParams.setHt();

        userParams.setHp();
        userParams.setWill();
        userParams.setPer();
        userParams.setFp();

        userParams.setBg();
        userParams.setDoge();
        userParams.setDmg();

        tl.setText(Integer.toString(user.tl));
        tlCost.setText(Integer.toString(user.tlCost));

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

        advantagesNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        advantagesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesTableView.setItems(advantagesData);
        advantagesTableView.setPlaceholder(new Label("Преимуществ нет"));

        disadvantagesNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        disadvantagesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesTableView.setItems(disadvantagesData);
        disadvantagesTableView.setPlaceholder(new Label("Недостатков нет"));
    }

    private void initSkills() {
        ObservableList<Skill> skills = user.skills();
        skillsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        skillsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        skillsComplexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        skillsLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        skillsTableView.setItems(skills);
        skillsTableView.setPlaceholder(new Label("Навыков нет"));
        userParams.setParry(skills);
        userParams.setBlock(skills);
    }

    private void intSpells() {
        spellsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spellsComplexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        spellsCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        spellsTableView.setItems(user.spells());
        spellsTableView.setPlaceholder(new Label("Заклинаний нет"));
    }

    private void initLanguages() {
        languageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        writtenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        languageCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        languagesTableView.setItems(user.languages());
        languagesTableView.setPlaceholder(new Label("Языков нет"));
    }

    private void initCultures() {
        culturaNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        culturaCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        culturasTableView.setItems(user.cultures());
        culturasTableView.setPlaceholder(new Label("Культур нет"));
    }

    private void setTextProperty() {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(TextField.class.isAssignableFrom(field.getType()))
                try {
                    TextField textField = (TextField) field.get(this);
                    textField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if(newValue.equals("")) return;
                            try {
                                Field setField = user.getClass().getDeclaredField(field.getName());

                                if(field.getName().equals("tlCost")) {
                                    if("\\D".matches(newValue) || newValue.equals("-")) return;
                                    user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) +
                                            Integer.parseInt(newValue) - user.tlCost);

                                    AbstractController.globalCost.setText(user.currentPoints);
                                    setRemainingPoints();
                                }
                                if(Integer.class.isAssignableFrom(setField.getType()))
                                    setField.set(user, Integer.parseInt(newValue));
                                else setField.set(user, newValue);
                            } catch(IllegalAccessException | NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                            user.save();

                        }
                    });
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    private void setRemainingPoints() {
        int remaining = Integer.parseInt(user.maxPoints) - Integer.parseInt(user.currentPoints);
        remainingPoints.setText(Integer.toString(remaining));
        if(remaining > 0) remainingPoints.setTextFill(Color.GREEN);
        else remainingPoints.setTextFill(Color.RED);
    }

    private void exportButtons(){
        jokXlsxButton.setOnAction(event ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохронить лист персонажа");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(user.name);
            File file = fileChooser.showSaveDialog(new Stage());
            new ExcelJokSheetFormat(file);
        });

        jsonButton.setOnAction(event ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохронить лист персонажа");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(user.name);
            File file = fileChooser.showSaveDialog(new Stage());
            new ExportToJson(file);
        });
    }
}
