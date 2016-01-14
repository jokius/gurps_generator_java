package ru.gurps.generator.desktop.controller.tables;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.full.info.SpellController;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.lib.CharacterParams;
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
    public TableColumn<Spell, Boolean> actionsColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;

    public MenuButton typeList;
    public MenuButton schoolsList;

    protected ArrayList<Integer> typeNumbers = new ArrayList<>();
    protected ArrayList<Integer> schoolNumbers = new ArrayList<>();

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

                this.prefWidth(500);
            }
        });

        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        actionsColumn.setCellFactory(p -> new ActionsCell());

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

        tableView.setRowFactory(tv -> {
            TableRow<Spell> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) openFullInfo(row.getItem());
            });

            return row ;
        });

        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) openFullInfo(tableView.getFocusModel().getFocusedItem());
        });

        tableView.setItems(spells);
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

    private void setSpells() {
        String query = "spellType like ";

        for (Integer tNumber : typeNumbers) {
            if (query.equals("spellType like ")) query += "'%" + tNumber + "%'";
            else query += " or spellType like '%" + tNumber + "%'";
        }

        if (query.equals("spellType like ")) query = "spellType='-1'";

        if (schoolNumbers.size() > 0) {
            query = "(" + query + ")" + " and (";
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


    private void openFullInfo(Spell spell){
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
    }

    private class ActionsCell extends TableCell<Spell, Boolean> {
        final HBox hBox = new HBox();
        final StackPane paddedButton = new StackPane();
        final Button addButton = new Button(Main.locale.getString("add"));
        final Button removeButton = new Button(Main.locale.getString("remove"));
        final Button fullButton = new Button(Main.locale.getString("full"));
        final TextField level = new TextField();
        final Label finalCost = new Label();
        private Spell spell;


        public ActionsCell() {
            setHBox();
        }

        private void setLevel() {
            level.setPrefWidth(62);
            level.setPrefHeight(27);
            level.setAlignment(Pos.CENTER);
            level.setText(Integer.toString(spell.level));

            level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (oldValue.equals(newValue) || newValue.equals("")) return;
                spell.level = Integer.parseInt(newValue);

                int cost = CharacterParams.spellCost(spell);
                finalCost.setText(Integer.toString(cost));
                spell.finalCost = cost;
            });
        }

        private void setFinalCost(){
            finalCost.setPrefWidth(62);
            finalCost.setPrefHeight(27);
            finalCost.setAlignment(Pos.CENTER);
        }

        private void setButtons() {
            addButton.setMinWidth(84);
            removeButton.setMinWidth(84);
            if (spell.add) {
                addButton.setVisible(false);
                removeButton.setVisible(true);
            } else {
                addButton.setVisible(true);
                removeButton.setVisible(false);
            }

            addButton.setOnAction(event -> {
                new CharactersSpell(character.id, spell.id, spell.level, spell.finalCost).create();
                setCurrentPoints(spell.finalCost + Integer.parseInt(character.currentPoints));
                spell.add = true;
                addButton.setVisible(false);
                removeButton.setVisible(true);
                getTableRow().getStyleClass().add("isAdd");
            });

            removeButton.setOnAction(event -> {
                HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", character.id);
                params.put("spellId", spell.id);
                CharactersSpell charactersSpell = (CharactersSpell) new CharactersSpell().find_by(params);
                setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersSpell.cost);
                charactersSpell.delete();
                spell.add = false;
                addButton.setVisible(true);
                removeButton.setVisible(false);
                getTableRow().getStyleClass().remove("isAdd");
            });

            fullButton.setOnAction(actionEvent -> openFullInfo((Spell) getTableRow().getItem()));
        }

        private void setHBox() {
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            paddedButton.getChildren().addAll(addButton, removeButton);
            hBox.getChildren().addAll(level, finalCost, paddedButton, fullButton);
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);

            if (empty) setGraphic(null);
            else {
                spell = (Spell) getTableRow().getItem();
                setFinalCost();
                setLevel();
                if (spell.finalCost > 0) finalCost.setText(Integer.toString(spell.finalCost));
                else finalCost.setText(Integer.toString(CharacterParams.spellCost(spell)));
                setButtons();
                setGraphic(hBox);
            }
        }
    }
}
