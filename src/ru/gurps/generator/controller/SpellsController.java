package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.*;

import java.io.IOException;
import java.util.HashMap;

public class SpellsController {
    User user = AbstractController.user;

    public TableView<Spell> tableView;
    public TableColumn<Spell, String> nameColumn;
    public TableColumn<Spell, String> nameEnColumn;
    public TableColumn<Spell, String> schoolColumn;
    public TableColumn<Spell, String> typeColumn;
    public TableColumn<Spell, String> costColumn;
    public TableColumn<Spell, String> demandsColumn;
    public TableColumn<Spell, String> descriptionColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchCost;
    public MenuItem searchDescription;
    public TextField searchText;


    public CheckBox t0CheckBox;
    public CheckBox t1CheckBox;
    public CheckBox t2CheckBox;
    public CheckBox t3CheckBox;
    public CheckBox t4CheckBox;
    public CheckBox t5CheckBox;
    public CheckBox t6CheckBox;
    public CheckBox t7CheckBox;
    public CheckBox t8CheckBox;

    public CheckBox s0CheckBox;


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

    private int lastIndex = -1;
    private ObservableList<Spell> spells;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("school"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("spellType"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        demandsColumn.setCellValueFactory(new PropertyValueFactory<>("demands"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.setPlaceholder(new Label("Заклинаний нет"));
        spells = new Spell().all();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", user.id);

        for(Spell spell : spells){
            params.put("spellId", spell.id);
            UserSpell userSpell = (UserSpell) new UserSpell().find_by(params);
            if(userSpell.level != null) {
                spell.finalCost = userSpell.cost;
                spell.level = userSpell.level;
                spell.add = true;
            }
        }

        tableView.setItems(spells);

        tableView.setRowFactory(tv -> {
            TableRow<Spell> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> bottomMenu(tableView.getFocusModel().getFocusedIndex()));
            return row;
        });

        tableView.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            int index = -1;
            if(keyCode == KeyCode.UP) {
                if(tableView.getFocusModel().getFocusedIndex() == 0)
                    index = tableView.getFocusModel().getFocusedIndex();
                else index = tableView.getFocusModel().getFocusedIndex() - 1;
            }

            if(keyCode == KeyCode.DOWN) {
                if(tableView.getFocusModel().getFocusedIndex() == spells.size() - 1)
                    index = tableView.getFocusModel().getFocusedIndex();
                else index = tableView.getFocusModel().getFocusedIndex() + 1;
            }

            bottomMenu(index);
        });
    }

    private void bottomMenu(int index){
        if(index == lastIndex) return;
        lastIndex = index;
        setupBottomMenu();

        Spell spell = spells.get(index);
        defaultParams(spell);

        if(spell.add) {
            add.setVisible(false);
            remove.setVisible(true);
        } else {
            add.setVisible(true);
            remove.setVisible(false);
        }

        level.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!oldValue.equals(newValue) || !newValue.equals("")){
                    spell.level = Integer.parseInt(newValue);
                    finalCost.setText(finalCost(spell));
                    spell.finalCost = Integer.parseInt(finalCost.getText());
                }
            }
        });

        setButtons(spell);
        setCheckBox(spell);
    }

    private void setupBottomMenu() {
        final double bottomMenuSize = 134.0;
        AnchorPane.setBottomAnchor(tableView, bottomMenuSize);
        bottomMenu.setVisible(true);
    }

    private void defaultParams(Spell spell) {
        level.setText(Integer.toString(spell.level));
        if(spell.complexity == 2){
            if(spell.level < user.iq - 2) level.setText(Integer.toString(user.iq - 2));
        } else{
            if(spell.level < user.iq - 3) level.setText(Integer.toString(user.iq - 3));
        }

        if(spell.finalCost > 0) finalCost.setText(Integer.toString(spell.finalCost));
        else finalCost.setText(finalCost(spell));
        complexity.setText(spell.getComplexity());
        needTime.setText(spell.needTime);
        duration.setText(spell.duration);
        maintainingCost.setText(spell.maintainingCost);
        createCost.setText(spell.createCost);
        thing.setText(spell.thing);
    }

    private String finalCost(Spell spell){
        if(spell.complexity == 2){
            if(spell.level <= user.iq - 2) return "1";
            else if(spell.level == user.iq - 1) return "2";
            else if(spell.level == user.iq) return "4";
            else if(spell.level == user.iq + 1) return "8";
            else if(spell.level == user.iq + 2) return "12";
            else if(spell.level == user.iq + 3) return "16";
            else{
                int i = spell.level;
                int cost = 16;
                while(i > user.iq + 3){
                    cost += 4;
                    i --;
                }
                return Integer.toString(cost);
            }
        } else {
            if(spell.level <= user.iq - 3) return "1";
            else if(spell.level == user.iq - 2) return "2";
            else if(spell.level == user.iq - 1) return "4";
            else if(spell.level == user.iq) return "8";
            else if(spell.level == user.iq + 1) return "12";
            else if(spell.level == user.iq + 2) return "16";
            else if(spell.level == user.iq + 3) return "20";
            else {
                int i = spell.level;
                int cost = 20;
                while(i == user.iq + 3) {
                    cost += 4;
                    i--;
                }
                return Integer.toString(cost);
            }
        }
    }

    private void setCheckBox(Spell spell){

    }

    private void setButtons(Spell spell){
        add.setOnAction(event -> {
            new UserSpell(user.id, spell.id, spell.level, spell.finalCost).create();
            String setCurrentPoints = Integer.toString(spell.finalCost + Integer.parseInt(user.currentPoints));
            user.update_single("currentPoints", setCurrentPoints);

            AbstractController.globalCost.setText(setCurrentPoints);
            spell.add = true;
            add.setVisible(false);
            remove.setVisible(true);
        });

        remove.setOnAction(event -> {
            HashMap<String, Object> params1 = new HashMap<>();
            params1.put("userId", user.id);
            params1.put("spellId", spell.id);
            UserSpell userSpell = (UserSpell) new UserSpell().find_by(params1);

            String setCurrentPoints = Integer.toString(Integer.parseInt(user.currentPoints) - userSpell.cost);
            user.update_single("currentPoints", setCurrentPoints);
            AbstractController.globalCost.setText(setCurrentPoints);
            userSpell.delete();
            spell.add = false;
            add.setVisible(true);
            remove.setVisible(false);
        });

        full.setOnAction(actionEvent -> {
            String name = spell.name + "(" + spell.nameEn + ")";

            Stage childrenStage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/spellFull.fxml"));
            SpellFullController controller = new SpellFullController(spell);
            loader.setController(controller);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 525, 400));
                childrenStage.setTitle("GURPSGenerator - " + name);
                childrenStage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
