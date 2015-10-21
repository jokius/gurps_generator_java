package ru.gurps.generator.controller.tables;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.full.info.SkillController;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.lib.CharacterParams;
import ru.gurps.generator.models.Character;
import ru.gurps.generator.models.characters.CharactersSkill;
import ru.gurps.generator.models.characters.CharactersSpecialization;
import ru.gurps.generator.models.rules.Skill;
import ru.gurps.generator.models.rules.Specialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillsController extends AbstractController {
    Character character = AbstractController.character;

    public TableView<Skill> tableView;
    public TableColumn<Skill, String> nameColumn;
    public TableColumn<Skill, String> nameEnColumn;
    public TableColumn<Skill, String> complexityColumn;
    public TableColumn<Skill, String> typeColumn;
    public TableColumn<Skill, String> demandsColumn;
    public TableColumn<Skill, String> defaultUseColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;


    public CheckMenuItem t0CheckBox;
    public CheckMenuItem t1CheckBox;
    public CheckMenuItem t2CheckBox;
    public CheckMenuItem t3CheckBox;
    public CheckMenuItem t4CheckBox;
    public CheckMenuItem t5CheckBox;
    public CheckMenuItem t6CheckBox;
    public CheckMenuItem t7CheckBox;

    public CheckMenuItem c0CheckBox;
    public CheckMenuItem c1CheckBox;
    public CheckMenuItem c2CheckBox;
    public CheckMenuItem c3CheckBox;


    public TextField level;
    public Label finalCost;
    public Button add;
    public Button remove;
    public Button full;


    public Label complexity;
    public Label twoHands;
    public Label parry;
    public Label parryBonus;

    public Label specialization;

    public AnchorPane bottomMenu;

    protected ArrayList<Integer> typeNumbers = new ArrayList<>();
    protected ArrayList<Integer> complexityNumbers = new ArrayList<>();

    private int lastId = -1;

    @FXML
    private void initialize() {
        for(int i = 0; 7 >= i; i++) typeNumbers.add(i);
        for(int i = 0; 3 >= i; i++) complexityNumbers.add(i);
        setCheckBox();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        complexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("skillType"));
        demandsColumn.setCellValueFactory(new PropertyValueFactory<>("demands"));
        defaultUseColumn.setCellValueFactory(new PropertyValueFactory<>("defaultUse"));

        nameColumn.setCellFactory(column -> new TableCell<Skill, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            getTableRow().getStyleClass().remove("isAdd");

            if(item != null || !empty) {
                setText(item);
                Skill skill = tableView.getItems().get(getTableRow().getIndex());
                HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", character.id);
                params.put("skillId", skill.id);
                CharactersSkill charactersSkill = (CharactersSkill) new CharactersSkill().find_by(params);
                getTableRow().getStyleClass().remove("addOne");
                getTableRow().getStyleClass().remove("isAdd");
                if(charactersSkill.id == null) {
                    getTableRow().getStyleClass().remove("isAdd");

                    params.remove("skillId");
                    CharactersSpecialization charactersSpecialization = new CharactersSpecialization();
                    for(Specialization specialization : skill.specializations()) {
                        params.put("specializationId", specialization.id);
                        charactersSpecialization = (CharactersSpecialization) charactersSpecialization.find_by(params);
                        if(charactersSpecialization.id == null) getTableRow().getStyleClass().remove("addOne");
                        else {
                            getTableRow().getStyleClass().add("addOne");
                            return;
                        }
                    }
                }
                else getTableRow().getStyleClass().add("isAdd");
            }

            if (empty) {
                setText(null);
                setGraphic(null);
            }
        }
    });

        tableView.setPlaceholder(new Label(Main.locale.getString("skills_not_found")));
        ObservableList<Skill> skills = new Skill().all();
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", character.id);

        for(Skill skill : skills){
            params.put("skillId", skill.id);
            CharactersSkill charactersSkill = (CharactersSkill) new CharactersSkill().find_by(params);
            if(charactersSkill.level != null) {
                skill.cost = charactersSkill.cost;
                skill.level = charactersSkill.level;
                skill.add = true;
            }
        }

        tableView.setItems(skills);
        tableView.getSortOrder().add(nameColumn);
        tableView.setRowFactory(tv -> {
            TableRow<Skill> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, new SkillEventHandler());
            return row;
        });

        localSearch(new Skill(), tableView, searchText, searchButton, searchAll, searchName, searchNameEn,
                searchDescription, reset);
    }

    private void setCheckBox(){
        Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7};
        for(Integer number : numbers) {
            try {
                CheckMenuItem checkBox = (CheckMenuItem) this.getClass().getDeclaredField("t" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "skillType like ";
                    if(newValue) typeNumbers.add(number);
                    else typeNumbers.remove(number);

                    for(Integer tNumber : typeNumbers) {
                        if(query.equals("skillType like ")) query += "'%" + tNumber + "%'";
                        else query += " or skillType like '%" + tNumber + "%'";

                        for(Integer sNumber : complexityNumbers) query += " and complexity like '%" + sNumber + "%'";
                    }
                    if(query.equals("skillType like ")) query = "skillType='-1'";
                    tableView.setItems(new Skill().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for(Integer number : numbers) {
            try {
                CheckMenuItem checkBox = (CheckMenuItem) this.getClass().getDeclaredField("c" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "complexity like ";
                    if(newValue) complexityNumbers.add(number);
                    else complexityNumbers.remove(number);

                    for(Integer sNumber : complexityNumbers) {
                        if(query.equals("complexity like ")) query += "'%" + sNumber + "%'";
                        else query += " or complexity like '%" + sNumber + "%'";

                        for(Integer tNumber : typeNumbers) query += " and skillType like '%" + tNumber + "%'";
                    }
                    if(query.equals("complexity like ")) query = "complexity='-1'";
                    tableView.setItems(new Skill().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                return;
            }
        }
    }

    class SkillEventHandler implements EventHandler<MouseEvent> {
        private Skill skill;
        private TableRow row;

        public SkillEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            row = (TableRow) t.getSource();
            skill = tableView.getItems().get(row.getIndex());
            if(skill.id == lastId) return;
            lastId = skill.id;
            bottomMenu();
        }

        void bottomMenu(){
            setupBottomMenu();
            defaultParams();

            if(skill.specializations().isEmpty()) {
                specialization.setVisible(false);

                if(skill.add) {
                    add.setVisible(false);
                    remove.setVisible(true);
                } else {
                    add.setVisible(true);
                    remove.setVisible(false);
                }
                level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    if(oldValue.equals(newValue) || newValue.equals("")) return;
                    skill.level = Integer.parseInt(newValue);
                    int cost = CharacterParams.skillCost(skill);
                    finalCost.setText(Integer.toString(cost));
                    skill.cost = cost;
                });
                setButtons();
            }
            else{
                add.setVisible(false);
                remove.setVisible(false);
                specialization.setVisible(true);
            }

            level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(oldValue.equals(newValue) || newValue.equals("")) return;
                skill.level = Integer.parseInt(newValue);
                int cost = CharacterParams.skillCost(skill);
                finalCost.setText(Integer.toString(cost));
                skill.cost = cost;
            });
            setButtons();
        }

        void defaultParams() {
            int defaultLevel = CharacterParams.skillLevel(skill);
            if(skill.level < defaultLevel) skill.level = defaultLevel;

            level.setText(Integer.toString(skill.level));
            if(skill.cost > 0) finalCost.setText(Integer.toString(skill.cost));
            else finalCost.setText(Integer.toString(CharacterParams.skillCost(skill)));

            complexity.setText(skill.getComplexity());
            twoHands.setText(skill.getTwoHands());
            parry.setText(skill.parry ? Main.locale.getString("yes") : Main.locale.getString("no"));
            parryBonus.setText(Integer.toString(skill.parryBonus));
        }

        void setButtons(){
            add.setOnAction(event -> {
                new CharactersSkill(character.id, skill.id, skill.cost, skill.level).create();
                setCurrentPoints(skill.cost + Integer.parseInt(character.currentPoints));
                skill.add = true;
                add.setVisible(false);
                remove.setVisible(true);
                row.getStyleClass().add("isAdd");
            });

            remove.setOnAction(event -> {
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("characterId", character.id);
                params1.put("skillId", skill.id);
                CharactersSkill charactersSkill = (CharactersSkill) new CharactersSkill().find_by(params1);
                setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersSkill.cost);
                charactersSkill.delete();
                skill.add = false;
                add.setVisible(true);
                remove.setVisible(false);
                row.getStyleClass().remove("isAdd");
            });

            full.setOnAction(actionEvent -> {
                Stage childrenStage = new Stage();
                FXMLLoader loader;
                if(skill.specializations().isEmpty()) {
                    loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/skillFull.fxml"));
                    loader.setController(new SkillController(skill));
                }
                else{
                    loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/skillSpecialization.fxml"));
                    loader.setController(new SkillSpecializationsController(skill, row));
                }

                loader.setResources(Main.locale);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setResizable(false);
                    childrenStage.setScene(new Scene(childrenRoot, 635, 572));
                    childrenStage.setTitle("GURPSGenerator - " + skill.name + " (" + skill.nameEn + ")");
                    childrenStage.show();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            });
        }

        void setupBottomMenu() {
            final double bottomMenuSize = 134.0;
            AnchorPane.setBottomAnchor(tableView, bottomMenuSize);
            bottomMenu.setVisible(true);
        }
    }
}
