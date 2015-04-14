package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gurps.generator.lib.UserParams;
import ru.gurps.generator.models.*;
import java.util.Random;

public class GenerateUserController extends AbstractController {
    public TextField name;
    public TextField params;
    public TextField advantages;
    public TextField disadvantages;
    public TextField spells;

    public Button generate;
    public Button back;

    private Stage stage;
    private Random random = new Random();

    public GenerateUserController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            generate.setDisable(newValue.equals(""));
        });

        params.textProperty().addListener((observable, oldValue, newValue) -> {
            generate.setDisable(name.getText().equals("") || newValue.equals("") || !newValue.matches("\\d+"));
        });

        advantages.textProperty().addListener((observable, oldValue, newValue) -> {
            generate.setDisable(name.getText().equals("") || newValue.equals("") || !newValue.matches("\\d+"));
        });

        disadvantages.textProperty().addListener((observable, oldValue, newValue) -> {
            generate.setDisable(name.getText().equals("") || newValue.equals("") || !newValue.matches("\\d+"));
        });

        spells.textProperty().addListener((observable, oldValue, newValue) -> {
            generate.setDisable(name.getText().equals("") || newValue.equals("") || !newValue.matches("\\d+"));
        });

        back.setOnAction(event -> {
            stage.close();
            usersStage(new Stage());
        });

        generate.setOnAction(event -> {
            generate();
            stage.close();
            createMainStage();
        });
    }

    private void generate() {
        int paramsInt = Integer.parseInt(params.getText());
        int advantagesInt = Integer.parseInt(advantages.getText());
        int disadvantagesInt = Math.abs(Integer.parseInt(disadvantages.getText()));
        int spellsInt = Integer.parseInt(spells.getText());
        user = (User) new User(name.getText(), Integer.toString(paramsInt + advantagesInt + disadvantagesInt + spellsInt)).create();
        if(paramsInt > 0) generateParams(paramsInt);
        if(advantagesInt > 0) generateAdvantages(advantagesInt);
        if(disadvantagesInt > 0) generateDisadvantages(disadvantagesInt);
        if(spellsInt > 0) generateSpells(spellsInt);
        user.save();
    }
    
    private void generateParams(int paramsInt){
        while(paramsInt > 0) {
            int cost = 0;
            int r = random.nextInt(100);
            if(r == 0) {
                user.st += 1;
                cost = 10;
                paramsInt -= cost;
                if(paramsInt < 0) {
                    user.st -= 1;
                    break;
                }
                user.hp += 1;

            } else if(r == 1) {
                user.dx += 1;
                cost = 20;
                paramsInt -= cost;
                if(paramsInt < 0) {
                    user.dx -= 1;
                    break;
                }

            } else if(r == 2) {
                user.iq += 1;
                cost = 20;
                paramsInt -= cost;
                if(paramsInt < 0) {
                    user.iq -= 1;
                    break;
                }
                user.will += 1;
                user.per += 1;

            } else if(r == 3) {
                user.ht += 1;
                cost = 10;
                paramsInt -= cost;
                if(paramsInt < 0) {
                    user.ht -= 1;
                    break;
                }
                user.fp += 1;
            }

            user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + cost);
        }

        user.bs = UserParams.defaultBs();
        user.move = user.bs.intValue();
    }

    private void generateAdvantages(int advantagesInt){
        ObservableList<Feature> advantages = new Feature().where("advantage = true and cost != 0");
        ObservableList<Integer> list = FXCollections.observableArrayList();
        while(advantagesInt > 0 || advantages.size() != list.size()){
            Feature feature = advantages.get(random.nextInt(advantages.size() - 1));
            boolean add = true;
            for(Integer id : list){
                if(id == feature.id) {
                    add = false;
                    break;
                }
            }
            if(add) {
                list.add(feature.id);
                int level = 1;
                if(feature.maxLevel > 1) level = random.nextInt(feature.maxLevel) + 1;
                advantagesInt -= feature.cost * level;
                if(advantagesInt < 0) break;
                user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + feature.cost);
                new UserFeature(user.id, feature.id, feature.cost, level).create();
            }
        }
    }

    private void generateDisadvantages(int advantagesInt){
        ObservableList<Feature> disadvantages = new Feature().where("advantage = false and cost != 0");
        ObservableList<Integer> list = FXCollections.observableArrayList();
        while(advantagesInt > 0 || disadvantages.size() != list.size()){
            Feature feature = disadvantages.get(random.nextInt(disadvantages.size() - 1));
            boolean add = true;
            for(Integer id : list){
                if(id == feature.id) {
                    add = false;
                    break;
                }
            }
            if(add) {
                list.add(feature.id);
                int level = 1;
                if(feature.maxLevel > 1) level = random.nextInt(feature.maxLevel) + 1;
                advantagesInt += feature.cost * level;
                if(advantagesInt < 0) break;
                user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + feature.cost);
                new UserFeature(user.id, feature.id, feature.cost, level).create();
            }

        }
    }

    private void generateSpells(int spellsInt){
        ObservableList<Spell> spells = new Spell().all();
        ObservableList<Integer> list = FXCollections.observableArrayList();
        int maxLvl = spellsInt / 10;
        while(spellsInt > 0 || spells.size() != list.size()){
            Spell spell = spells.get(random.nextInt(spells.size() - 1));
            boolean add = true;
            for(Integer id : list){
                if(id == spell.id) {
                    add = false;
                    break;
                }
            }

            if(add){
                System.out.println(add);
                list.add(spell.id);
                if(spell.complexity == 2){
                    spell.level = user.iq - 2;
                } else{
                    spell.level = user.iq - 3;
                }
                spell.level += random.nextInt(maxLvl);
                spell.cost = spellCost(spell);
                spellsInt -= spell.cost;
                if(spellsInt < 0) break;
                user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + spell.cost);
                new UserSpell(user.id, spell.id, spell.level, spell.cost).create();
            }
        }
    }


    private int spellCost(Spell spell){
        if(spell.complexity == 2){
            if(spell.level <= user.iq - 2) return 1;
            else if(spell.level == user.iq - 1) return 2;
            else if(spell.level == user.iq) return 4;
            else if(spell.level == user.iq + 1) return 8;
            else if(spell.level == user.iq + 2) return 12;
            else if(spell.level == user.iq + 3) return 16;
            else{
                int i = spell.level;
                int cost = 16;
                while(i > user.iq + 3){
                    cost += 4;
                    i --;
                }
                return cost;
            }
        } else {
            if(spell.level <= user.iq - 3) return 1;
            else if(spell.level == user.iq - 2) return 2;
            else if(spell.level == user.iq - 1) return 4;
            else if(spell.level == user.iq) return 8;
            else if(spell.level == user.iq + 1) return 12;
            else if(spell.level == user.iq + 2) return 16;
            else if(spell.level == user.iq + 3) return 20;
            else {
                int i = spell.level;
                int cost = 20;
                while(i == user.iq + 3) {
                    cost += 4;
                    i--;
                }
                return cost;
            }
        }
    }
}