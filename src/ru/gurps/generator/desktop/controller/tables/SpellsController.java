package ru.gurps.generator.desktop.controller.tables;

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
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.full.info.SpellController;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.characters.CharactersSpell;
import ru.gurps.generator.desktop.models.rules.School;
import ru.gurps.generator.desktop.models.rules.Spell;
import ru.gurps.generator.desktop.singletons.SpellTypeSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SpellsController extends AbstractController {
    ru.gurps.generator.desktop.models.Character character = AbstractController.character;

    public TableView<Spell> tableView;
    public TableColumn<Spell, String> nameColumn;
    public TableColumn<Spell, String> nameEnColumn;
    public TableColumn<Spell, String> schoolColumn;
    public TableColumn<Spell, String> typeColumn;
    public TableColumn<Spell, String> complexityColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;

    public MenuButton typeList;
    public MenuButton schoolsList;

    public TextField level;
    public Label finalCost;
    public Button add;
    public Button remove;
    public Button full;


    public Label complexity;
    public Label needTime;
    public Label duration;
    public Label maintainingCost;
    public Label createCost;
    public Label thing;

    public AnchorPane bottomMenu;

    protected ArrayList<Integer> typeNumbers = new ArrayList<>();
    protected ArrayList<Integer> schoolNumbers = new ArrayList<>();

    private int lastId = -1;

    @FXML
    private void initialize() {
        setDefaultSpells();
        setCheckBox();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("school"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("spellType"));
        complexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        nameColumn.setCellFactory(column -> new TableCell<Spell, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getTableRow().getStyleClass().remove("isAdd");

                if (item != null || !empty) {
                    setText(item);
                    Spell spell = tableView.getItems().get(getTableRow().getIndex());
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("characterId", character.id);
                    params.put("spellId", spell.id);
                    CharactersSpell charactersSpell = (CharactersSpell) new CharactersSpell().find_by(params);
                    if (charactersSpell.id == null) getTableRow().getStyleClass().remove("isAdd");
                    else getTableRow().getStyleClass().add("isAdd");
                }

                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        tableView.setPlaceholder(new Label(Main.locale.getString("spells_not_found")));
        ObservableList<Spell> spells = new Spell().all();
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", character.id);

        for (Spell spell : spells) {
            params.put("spellId", spell.id);
            CharactersSpell charactersSpell = (CharactersSpell) new CharactersSpell().find_by(params);
            if (charactersSpell.level != null) {
                spell.finalCost = charactersSpell.cost;
                spell.level = charactersSpell.level;
                spell.add = true;
            }
        }

        tableView.setItems(spells);
        tableView.setRowFactory(tv -> {
            TableRow<Spell> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, new SpellEventHandler());
            return row;
        });

        localSearch(new Spell(), tableView, searchText, searchButton, searchAll, searchName, searchNameEn,
                searchDescription, reset);
    }

    private void setCheckBox() {
        for (Map.Entry<Integer, String> type : SpellTypeSingleton.getInstance().getList()) {
            CheckMenuItem checkBox = new CheckMenuItem(type.getValue());
            checkBox.setSelected(true);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) typeNumbers.add(type.getKey());
                else typeNumbers.remove(type.getKey());
                setSpells();
            });

            typeList.getItems().add(checkBox);
        }

        for (Object object : new School().all()) {
            School school = (School) object;
            CheckMenuItem checkBox = new CheckMenuItem(school.name);
            checkBox.setSelected(true);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) schoolNumbers.add(school.id);
                else schoolNumbers.remove(school.id);
                setSpells();
            });

            schoolsList.getItems().add(checkBox);
        }
    }

    private void setDefaultSpells() {
        typeNumbers.addAll(SpellTypeSingleton.getInstance().getList().stream().map(Map.Entry::getKey)
                .collect(Collectors.toList()));
        for (Object object : new School().all()) {
            School school = (School) object;
            schoolNumbers.add(school.id);
        }

        setSpells();
    }

    private void setSpells(){
        String query = "spellType like ";

        for (Integer tNumber : typeNumbers) {
            if (query.equals("spellType like ")) query += "'%" + tNumber + "%'";
            else query += " or spellType like '%" + tNumber + "%'";
        }

        if (query.equals("spellType like ")) query = "spellType='-1'";

        if (schoolNumbers.size() > 0) {
            query ="(" + query + ")" + " and (";
            for (Integer sNumber : schoolNumbers) {
                if (query.substring(query.length() - 1).equals("(")) query += "schoolId = '" + sNumber + "'";
                else query += " or schoolId = '" + sNumber + "'";
            }
            query += ")";
        } else {
            query += "and schoolId= '-1'";
        }

        tableView.setItems(new Spell().where(query));
    }

    class SpellEventHandler implements EventHandler<MouseEvent> {
        private Spell spell;
        private TableRow row;

        public SpellEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            row = (TableRow) t.getSource();
            spell = tableView.getItems().get(row.getIndex());
            if (spell.id == lastId) return;
            lastId = spell.id;
            bottomMenu();
        }

        void bottomMenu() {
            setupBottomMenu();
            defaultParams();

            if (spell.add) {
                add.setVisible(false);
                remove.setVisible(true);
            } else {
                add.setVisible(true);
                remove.setVisible(false);
            }

            level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (oldValue.equals(newValue) || newValue.equals("")) return;
                spell.level = Integer.parseInt(newValue);
                finalCost.setText(finalCost());
                spell.finalCost = Integer.parseInt(finalCost.getText());
            });
            setButtons();
        }

        void defaultParams() {
            level.setText(Integer.toString(spell.level));
            if (spell.complexity == 2) {
                if (spell.level < character.iq - 2) level.setText(Integer.toString(character.iq - 2));
            } else {
                if (spell.level < character.iq - 3) level.setText(Integer.toString(character.iq - 3));
            }

            if (spell.finalCost > 0) finalCost.setText(Integer.toString(spell.finalCost));
            else finalCost.setText(finalCost());
            complexity.setText(spell.getComplexity());
            needTime.setText(spell.needTime);
            duration.setText(spell.duration);
            maintainingCost.setText(spell.maintainingCost);
            createCost.setText(spell.createCost);
            thing.setText(spell.thing);
        }

        String finalCost() {
            if (spell.complexity == 2) {
                if (spell.level <= character.iq - 2) return "1";
                else if (spell.level == character.iq - 1) return "2";
                else if (spell.level == character.iq) return "4";
                else if (spell.level == character.iq + 1) return "8";
                else if (spell.level == character.iq + 2) return "12";
                else if (spell.level == character.iq + 3) return "16";
                else {
                    int i = spell.level;
                    int cost = 16;
                    while (i > character.iq + 3) {
                        cost += 4;
                        i--;
                    }
                    return Integer.toString(cost);
                }
            } else {
                if (spell.level <= character.iq - 3) return "1";
                else if (spell.level == character.iq - 2) return "2";
                else if (spell.level == character.iq - 1) return "4";
                else if (spell.level == character.iq) return "8";
                else if (spell.level == character.iq + 1) return "12";
                else if (spell.level == character.iq + 2) return "16";
                else if (spell.level == character.iq + 3) return "20";
                else {
                    int i = spell.level;
                    int cost = 20;
                    while (i == character.iq + 3) {
                        cost += 4;
                        i--;
                    }
                    return Integer.toString(cost);
                }
            }
        }

        void setButtons() {
            add.setOnAction(event -> {
                new CharactersSpell(character.id, spell.id, spell.level, spell.finalCost).create();
                setCurrentPoints(spell.finalCost + Integer.parseInt(character.currentPoints));
                spell.add = true;
                add.setVisible(false);
                remove.setVisible(true);
                row.getStyleClass().add("isAdd");
            });

            remove.setOnAction(event -> {
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("characterId", character.id);
                params1.put("spellId", spell.id);
                CharactersSpell charactersSpell = (CharactersSpell) new CharactersSpell().find_by(params1);
                setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersSpell.cost);
                charactersSpell.delete();
                spell.add = false;
                add.setVisible(true);
                remove.setVisible(false);
                row.getStyleClass().remove("isAdd");
            });

            full.setOnAction(actionEvent -> {
                Stage childrenStage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/spellFull.fxml"));
                SpellController controller = new SpellController(spell);
                loader.setController(controller);
                loader.setResources(Main.locale);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setResizable(false);
                    childrenStage.setScene(new Scene(childrenRoot, 635, 572));
                    childrenStage.setTitle("GURPSGenerator - " + spell.name + " (" + spell.nameEn + ")");
                    childrenStage.show();
                } catch (IOException e) {
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
