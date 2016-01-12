package ru.gurps.generator.desktop.controller.characters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextField;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.lib.CharacterParams;
import ru.gurps.generator.desktop.models.Character;
import ru.gurps.generator.desktop.models.characters.*;
import ru.gurps.generator.desktop.models.rules.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class GenerateController extends AbstractController {
    public TextField name;
    public TextField params;

    // advantages
    public TextField advantages;
    public CheckMenuItem advantageCheckBox1;
    public CheckMenuItem advantageCheckBox2;
    public CheckMenuItem advantageCheckBox3;
    public CheckMenuItem advantageCheckBox4;
    public CheckMenuItem advantageCheckBox5;

    // disadvantages
    public TextField disadvantages;
    public CheckMenuItem disadvantageCheckBox1;
    public CheckMenuItem disadvantageCheckBox2;
    public CheckMenuItem disadvantageCheckBox3;
    public CheckMenuItem disadvantageCheckBox4;
    public CheckMenuItem disadvantageCheckBox5;

    // skills
    public TextField skills;
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

    // techniques
    public TextField techniques;
    public CheckMenuItem tc0CheckBox;
    public CheckMenuItem tc1CheckBox;
    public CheckMenuItem tc2CheckBox;
    public CheckMenuItem tc3CheckBox;

    // spells
    public TextField spells;
    public CheckMenuItem st0CheckBox;
    public CheckMenuItem st1CheckBox;
    public CheckMenuItem st2CheckBox;
    public CheckMenuItem st3CheckBox;
    public CheckMenuItem st4CheckBox;
    public CheckMenuItem st5CheckBox;
    public CheckMenuItem st6CheckBox;
    public CheckMenuItem st7CheckBox;
    public CheckMenuItem st8CheckBox;

    public CheckMenuItem s0CheckBox;

    // buttons
    public Button generate;
    public Button back;

    // support params
    private Random random = new Random();
    protected ArrayList<Integer> featuresNumbers;

    @FXML
    private void initialize() {
        for (TextField textField : textFields())
            setTextFieldParams(textField);
        setCheckBoxes();

        back.setOnAction(event -> {
            stage.close();
            charactersStage();
        });

        generate.setOnAction(event -> {
            generate();
            stage.close();
            createParentStage();
        });
    }

    private TextField[] textFields() {
        return new TextField[]{name, params, advantages, disadvantages, skills, techniques, spells};
    }

    private void setTextFieldParams(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                generate.setDisable(true);
                return;
            }
            if (name.getText().equals("")){
                generate.setDisable(true);
                return;
            }
            generate.setDisable(false);
        });
    }

    private void setCheckBoxes() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!CheckMenuItem.class.isAssignableFrom(field.getType())) continue;
            try {
                CheckMenuItem checkMenuItem = (CheckMenuItem) field.get(this);
                checkMenuItem.setSelected(true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void generate() {
        character = (ru.gurps.generator.desktop.models.Character) new Character(name.getText(), "0").create();
        int cost = 0;
        for (TextField textField : textFields()) {
            if (textField.getId().equals("name") || textField.getText().equals("")) continue;
            int setPoints = Math.abs(Integer.parseInt(textField.getText()));
            if (setPoints == 0) continue;

            try {
                Method generateMethod = this.getClass().getMethod(textField.getId() + "Generate", int.class);
                generateMethod.invoke(this, setPoints);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            cost += setPoints;
        }

        character.maxPoints = Integer.toString(cost);
        character.save();
    }

    public void paramsGenerate(int paramsInt) {
        while (paramsInt > 0) {
            int cost = 0;
            int r = random.nextInt(100);
            if (r == 0) {
                character.st += 1;
                cost = 10;
                paramsInt -= cost;
                if (paramsInt < 0) {
                    character.st -= 1;
                    break;
                }
                character.hp += 1;

            } else if (r == 1) {
                character.dx += 1;
                cost = 20;
                paramsInt -= cost;
                if (paramsInt < 0) {
                    character.dx -= 1;
                    break;
                }

            } else if (r == 2) {
                character.iq += 1;
                cost = 20;
                paramsInt -= cost;
                if (paramsInt < 0) {
                    character.iq -= 1;
                    break;
                }
                character.will += 1;
                character.per += 1;

            } else if (r == 3) {
                character.ht += 1;
                cost = 10;
                paramsInt -= cost;
                if (paramsInt < 0) {
                    character.ht -= 1;
                    break;
                }
                character.fp += 1;
            }

            character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + cost);
        }

        character.bs = CharacterParams.defaultBs();
        character.move = character.bs.intValue();
    }

    public void advantagesGenerate(int advantagesInt) {
        ObservableList<Feature> advantages = featureList(true);
        if (advantages.size() <= 1) return;
        ObservableList<Integer> list = FXCollections.observableArrayList();
        while (advantagesInt > 0 || advantages.size() != list.size()) {
            if(advantages.size() - list.size() == 1) break;
            Feature feature = advantages.get(random.nextInt(advantages.size() - 1));
            boolean add = true;
            for (Integer id : list) {
                if (id == feature.id) {
                    add = false;
                    break;
                }
            }
            if (add) {
                list.add(feature.id);
                int level = 1;
                if (feature.maxLevel > 1) level = random.nextInt(feature.maxLevel) + 1;
                advantagesInt -= feature.cost * level;
                if (advantagesInt < 0) break;
                character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + feature.cost);
                new CharactersFeature(character.id, feature.id, feature.cost, level).create();
            }
        }
    }

    public void disadvantagesGenerate(int advantagesInt) {
        ObservableList<Feature> disadvantages = featureList(false);
        if (disadvantages.size() <= 1) return;
        ObservableList<Integer> list = FXCollections.observableArrayList();
        while (advantagesInt > 0 || disadvantages.size() != list.size()) {
            if(disadvantages.size() - list.size() == 1) break;
            Feature feature = disadvantages.get(random.nextInt(disadvantages.size() - 1));
            boolean add = true;
            for (Integer id : list) {
                if (id == feature.id) {
                    add = false;
                    break;
                }
            }
            if (add) {
                list.add(feature.id);
                int level = 1;
                if (feature.maxLevel > 1) level = random.nextInt(feature.maxLevel) + 1;
                advantagesInt += feature.cost * level;
                if (advantagesInt < 0) break;
                character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + feature.cost);
                new CharactersFeature(character.id, feature.id, feature.cost, level).create();
            }

        }
    }

    public void skillsGenerate(int skillsInt) {
        ObservableList<Skill> skills = skillsList();
        if (skills.size() <= 1) return;
        ObservableList<Integer> list = FXCollections.observableArrayList();
        int maxLvl = skillsInt / 10;
        while (skillsInt > 0 || skills.size() != list.size()) {
            if(skills.size() - list.size() == 1) break;
            Skill skill = skills.get(random.nextInt(skills.size() - 1));
            if (skill.specializations().size() > 0) {
                Specialization specialization = skill.specializations().get(random.nextInt(skill.specializations().size() - 1));
                boolean add = true;
                for (Integer id : list) {
                    if (id == skill.id) {
                        add = false;
                        break;
                    }
                }

                if (add) {
                    list.add(skill.id);
                    specialization.level = CharacterParams.skillLevel(specialization);
                    specialization.level += random.nextInt(maxLvl);
                    specialization.cost = CharacterParams.skillCost(specialization);
                    skillsInt -= specialization.cost;
                    if (skillsInt < 0) break;
                    character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + specialization.cost);
                    new CharactersSpecialization(character.id, specialization.id, specialization.level, specialization.cost).create();
                }
            } else {
                boolean add = true;
                for (Integer id : list) {
                    if (id == skill.id) {
                        add = false;

                    }
                }

                if (add) {
                    list.add(skill.id);
                    skill.level = CharacterParams.skillLevel(skill);
                    skill.level += random.nextInt(maxLvl);
                    skill.cost = CharacterParams.skillCost(skill);
                    skillsInt -= skill.cost;
                    if (skillsInt < 0) break;
                    character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + skill.cost);
                    new CharactersSkill(character.id, skill.id, skill.level, skill.cost).create();
                }
            }
        }
    }


    public void techniquesGenerate(int techniquesInt) {
        ObservableList<Technique> techniques = techniquesList();
        if (techniques.size() <= 1) return;
        ObservableList<Integer> list = FXCollections.observableArrayList();
        int maxLvl = techniquesInt / 10;
        while (techniquesInt > 0 || techniques.size() != list.size()) {
            if(techniques.size() - list.size() == 1) break;
            Technique technique = techniques.get(random.nextInt(techniques.size() - 1));
            boolean add = true;
            for (Integer id : list) {
                if (id == technique.id) {
                    add = false;
                    break;
                }
            }

            if (add) {
                list.add(technique.id);
                technique.level += random.nextInt(maxLvl);
                technique.cost = CharacterParams.techniqueCost(technique);
                techniquesInt -= technique.cost;
                if (techniquesInt < 0) break;
                character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + technique.cost);
                new CharactersTechnique(character.id, technique.id, technique.level, technique.cost).create();
            }
        }
    }

    public void spellsGenerate(int spellsInt) {
        ObservableList<Spell> spells = spellsList();
        if (spells.size() <= 1) return;
        ObservableList<Integer> list = FXCollections.observableArrayList();
        int maxLvl = spellsInt / 10;
        while (spellsInt > 0 || spells.size() != list.size()) {
            if(spells.size() - list.size() == 1) break;
            Spell spell = spells.get(random.nextInt(spells.size() - 1));
            boolean add = true;
            for (Integer id : list) {
                if (id == spell.id) {
                    add = false;
                    break;
                }
            }

            if (add) {
                list.add(spell.id);
                if (spell.complexity == 2) {
                    spell.level = character.iq - 2;
                } else {
                    spell.level = character.iq - 3;
                }
                spell.level += random.nextInt(maxLvl);
                spell.finalCost = CharacterParams.spellCost(spell);
                spellsInt -= spell.finalCost;
                if (spellsInt < 0) break;
                character.currentPoints = Integer.toString(Integer.parseInt(character.currentPoints) + spell.finalCost);
                new CharactersSpell(character.id, spell.id, spell.level, spell.finalCost).create();
            }
        }
    }

    protected ObservableList featureList(boolean isAdvantage) {
        Integer[] numbers = {1, 2, 3, 4, 5};
        featuresNumbers = new ArrayList<>();
        CheckMenuItem checkBox;
        String query = "advantage=" + isAdvantage + " and cost != 0 and featureType like ";
        for (Integer number : numbers) {
            try {
                if (isAdvantage)
                    checkBox = (CheckMenuItem) this.getClass().getDeclaredField("advantageCheckBox" + number).get(this);
                else
                    checkBox = (CheckMenuItem) this.getClass().getDeclaredField("disadvantageCheckBox" + number).get(this);

                if (checkBox.isSelected()) {
                    if (query.equals("advantage=" + isAdvantage + " and cost != 0 and featureType like "))
                        query += "'%" + number + "%'";
                    else query += " or advantage=" + isAdvantage + " and cost != 0 and featureType like '%" + number + "%'";
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {

            }
        }

        if (query.equals("advantage=" + isAdvantage + " and featureType like "))
            query = "advantage=" + isAdvantage + " and featureType='6'";

        return new Feature().where(query);
    }

    protected ObservableList skillsList() {
        try {
            Integer[] tNumbers = {0, 1, 2, 3, 4, 5, 6, 7};
            Integer[] cNumbers = {0, 1, 2, 3};
            String tQuery = "skillType like ";
            String cQuery = " and complexity like ";
            CheckMenuItem checkBox;

            for (Integer number : tNumbers) {
                checkBox = (CheckMenuItem) this.getClass().getDeclaredField("t" + number + "CheckBox").get(this);
                if (checkBox.isSelected()) {
                    if (tQuery.equals("skillType like ")) tQuery += "'%" + number + "%'";
                    else tQuery += " or skillType like '%" + number + "%'";
                }
            }

            for (Integer number : cNumbers) {
                checkBox = (CheckMenuItem) this.getClass().getDeclaredField("c" + number + "CheckBox").get(this);
                if (checkBox.isSelected()) {
                    if (cQuery.equals(" and complexity like ")) cQuery += "'%" + number + "%'";
                    else cQuery += " or complexity like '%" + number + "%'";
                }
            }

            if (tQuery.equals("skillType like ")) tQuery = "skillType='-1'";
            if (cQuery.equals(" and complexity like ")) cQuery = " and complexity='-1'";
            return new Skill().where(tQuery + cQuery);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ObservableList techniquesList() {
        Integer[] numbers = {0, 1, 2, 3};
        CheckMenuItem checkBox;
        String query = "complexity like ";

        for (Integer number : numbers) {
            try {
                checkBox = (CheckMenuItem) this.getClass().getDeclaredField("tc" + number + "CheckBox").get(this);

                if (checkBox.isSelected()) {
                    if (query.equals("complexity like ")) query += "'%" + number + "%'";
                    else query += " or complexity like '%" + number + "%'";
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (query.equals("complexity like ")) query = "complexity='-1'";
        return new Technique().where(query);
    }

    private ObservableList spellsList() {
        try {
            Integer[] tNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8};
            Integer[] sNumbers = {0};
            CheckMenuItem checkBox;
            String stQuery = "spellType like ";
            String sQuery = " and school like ";
            for (Integer number : tNumbers) {
                checkBox = (CheckMenuItem) this.getClass().getDeclaredField("st" + number + "CheckBox").get(this);
                if (checkBox.isSelected()) {
                    if (stQuery.equals("spellType like ")) stQuery += "'%" + number + "%'";
                    else stQuery += " or spellType like '%" + number + "%'";
                }
            }

            for (Integer number : sNumbers) {
                checkBox = (CheckMenuItem) this.getClass().getDeclaredField("s" + number + "CheckBox").get(this);
                if (checkBox.isSelected()) {
                    if (sQuery.equals(" and school like ")) sQuery += "'%" + number + "%'";
                    else sQuery += " or school like '%" + number + "%'";
                }
            }

            if (stQuery.equals("spellType like ")) stQuery = "spellType='-1'";
            if (sQuery.equals(" and school like ")) sQuery = " and school='-1'";

            return new Spell().where(stQuery + sQuery);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}