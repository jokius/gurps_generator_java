package ru.gurps.generator.controller.tables;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.lib.CharacterParams;
import ru.gurps.generator.models.rules.Skill;
import ru.gurps.generator.models.rules.Specialization;
import ru.gurps.generator.models.characters.CharactersSpecialization;

import java.util.HashMap;


public class SkillSpecializationsController extends AbstractController {
    private Skill skill;
    private TableRow skillRow;


    public Label name;
    public Label defaultUse;
    public Label demands;
    public Label twoHands;
    public Label finalCost;
    public TextField level;

    public Button add;
    public Button remove;

    public TableView<Specialization> tableView;
    public TableColumn<Specialization, String> nameColumn;
    public TableColumn<Specialization, String> nameEnColumn;
    public TableColumn<Specialization, String> typeColumn;
    public TableColumn<Specialization, String> complexityColumn;
    public TableColumn<Specialization, String> parryColumn;
    public TableColumn<Specialization, String> parryBonusColumn;

    public Text fullDescription;
    public Text modifiers;

    int lastId;

    public SkillSpecializationsController(Skill skill, TableRow skillRow) {
        this.skill = skill;
        this.skillRow = skillRow;
    }

    @FXML
    private void initialize() {
        setDefaultParams();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        complexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("skillType"));
        parryColumn.setCellValueFactory(new PropertyValueFactory<>("parry"));
        parryBonusColumn.setCellValueFactory(new PropertyValueFactory<>("parryBonus"));

        nameColumn.setCellFactory(column -> new TableCell<Specialization, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getTableRow().getStyleClass().remove("isAdd");

                if(item != null || !empty) {
                    setText(item);
                    Specialization specialization = tableView.getItems().get(getTableRow().getIndex());
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("characterId", character.id);
                    params.put("specializationId", specialization.id);
                    CharactersSpecialization charactersSpecialization = (CharactersSpecialization) new CharactersSpecialization().find_by(params);
                    if(charactersSpecialization.id == null) getTableRow().getStyleClass().remove("isAdd");
                    else getTableRow().getStyleClass().add("isAdd");
                }

                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        tableView.setPlaceholder(new Label(Main.locale.getString("skill_specializations_not_found")));
        ObservableList<Specialization> specializations = skill.specializations();
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", character.id);

        for(Specialization specialization : specializations){
            params.put("specializationId", specialization.id);
            CharactersSpecialization charactersSpecialization = (CharactersSpecialization) new CharactersSpecialization().find_by(params);
            if(charactersSpecialization.level != null) {
                specialization.cost = charactersSpecialization.cost;
                specialization.level = charactersSpecialization.level;
                specialization.add = true;
            }
        }

        tableView.setItems(specializations);
        tableView.getSortOrder().add(nameColumn);
        tableView.setRowFactory(tv -> {
            TableRow<Specialization> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, new SpecializationEventHandler());
            return row;
        });
    }

    class SpecializationEventHandler implements EventHandler<MouseEvent> {
        private Specialization specialization;
        private TableRow row;

        public SpecializationEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            row = (TableRow) t.getSource();
            if(row.getIndex() >= tableView.getItems().size()) return;
            specialization = tableView.getItems().get(row.getIndex());
            if(specialization.id == lastId) return;
            lastId = specialization.id;
            setPrams();

            if(specialization.add) {
                add.setVisible(false);
                remove.setVisible(true);
            } else {
                add.setDisable(false);
                add.setVisible(true);
                remove.setVisible(false);
            }

            level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(oldValue.equals(newValue) || newValue.equals("")) return;
                specialization.level = Integer.parseInt(newValue);
                int cost = CharacterParams.skillCost(specialization);
                finalCost.setText(Integer.toString(cost));
                specialization.cost = cost;
            });
            setButtons();
        }

        void setButtons(){
            add.setOnAction(event -> {
                new CharactersSpecialization(character.id, specialization.id, specialization.cost, specialization.level).create();
                setCurrentPoints(skill.cost + Integer.parseInt(character.currentPoints));
                specialization.add = true;
                add.setVisible(false);
                remove.setVisible(true);
                row.getStyleClass().add("isAdd");
                setSkillRow();
            });

            remove.setOnAction(event -> {
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("characterId", character.id);
                params1.put("specializationId", specialization.id);
                CharactersSpecialization charactersSpecialization = (CharactersSpecialization) new CharactersSpecialization().find_by(params1);
                setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersSpecialization.cost);
                charactersSpecialization.delete();
                specialization.add = false;
                add.setVisible(true);
                remove.setVisible(false);
                row.getStyleClass().remove("isAdd");
                setSkillRow();
            });
        }

        private void setSkillRow(){
            HashMap<String, Object> params = new HashMap<>();
            params.put("characterId", character.id);
            CharactersSpecialization charactersSpecialization = new CharactersSpecialization();
            for(Specialization specialization : skill.specializations()) {
                params.put("specializationId", specialization.id);
                charactersSpecialization = (CharactersSpecialization) charactersSpecialization.find_by(params);
                if(charactersSpecialization.id == null) skillRow.getStyleClass().remove("addOne");
                else {
                    skillRow.getStyleClass().add("addOne");
                    return;
                }
            }
        }

        private void setPrams() {
            name.setText(specialization.name + " (" + specialization.nameEn + " )");
            defaultUse.setText(specialization.defaultUse == null ? skill.getDefaultUse() : specialization.defaultUse);
            demands.setText(specialization.demands == null ? skill.getDemands() : specialization.demands);
            fullDescription.setText(specialization.description + "\n");
            modifiers.setText(specialization.getModifiers());
            finalCost.setText(Integer.toString(CharacterParams.skillCost(specialization)));
            level.setText(Integer.toString(CharacterParams.skillLevel(specialization)));
        }
    }

    private void setDefaultParams() {
        name.setText(skill.name + " (" + skill.nameEn + " )");
        defaultUse.setText(skill.getDefaultUse());
        demands.setText(skill.getDemands());
        twoHands.setText(skill.getTwoHands());
        fullDescription.setText(skill.description + "\n");
        modifiers.setText(skill.getModifiers());
        finalCost.setText(Integer.toString(CharacterParams.skillCost(skill)));
        level.setText(Integer.toString(CharacterParams.skillLevel(skill)));
    }
}
