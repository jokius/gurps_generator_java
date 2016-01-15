package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.lib.CharacterParams;
import ru.gurps.generator.desktop.models.characters.CharactersSpell;
import ru.gurps.generator.desktop.models.rules.Spell;

import java.util.HashMap;


public class SpellController extends AbstractController {
    private Spell spell;

    public Label name;
    public TextFlow fullDescription;

    public Label finalCost;
    public TextField level;
    public Button addButton;
    public Button removeButton;

    public SpellController(Spell spell) {
        this.spell = spell;
    }

    @FXML
    private void initialize() {
        setButtons();
        setLevelAndFinalCost();
        name.setText(spell.name + " (" + spell.nameEn + " )");
        fullDescription.getChildren().addAll(resistance(), school(), type(), complexity(), demands(), description(),
                modifiers(), needTime(), cost(), maintainingCost(), duration(), thing(), createCost());
    }

    private Text resistance() {
        Text resistance = new Text(spell.getResistanceSingle() + "\r\n");
        resistance.setId("resistance");
        return resistance;
    }

    private Text school() {
        Text school = new Text(spell.getSchoolSingle() + "\r\n");
        school.setId("school");
        return school;
    }

    private Text type() {
        Text type = new Text(spell.getTypeSingle() + "\r\n");
        type.setId("type");
        return type;
    }

    private Text complexity() {
        Text complexity = new Text(spell.getComplexitySingle() + "\r\n");
        complexity.setId("complexity");
        return complexity;
    }

    private Text demands() {
        Text demands = new Text(spell.getDemandsSingle() + "\r\n\r\n");
        demands.setId("demands");
        return demands;
    }

    private Text description() {
        Text description = new Text(spell.description + "\r\n");
        description.setId("description");
        return description;
    }

    private Text modifiers() {
        Text modifiers = new Text(spell.getModifiersSingle() + "\r\n\r\n");
        modifiers.setId("modifiers");
        return modifiers;
    }

    private Text needTime() {
        Text needTime = new Text(spell.getNeedTimeSingle() + "\r\n");
        needTime.setId("needTime");
        return needTime;
    }

    private Text cost() {
        Text cost = new Text(spell.getCostSingle() + "\r\n");
        cost.setId("costSpell");
        return cost;
    }

    private Text maintainingCost() {
        Text maintainingCost = new Text(spell.getMaintainingCostSingle() + "\r\n");
        maintainingCost.setId("maintainingCost");
        return maintainingCost;
    }

    private Text duration() {
        Text duration = new Text(spell.getDurationSingle() + "\r\n\r\n");
        duration.setId("duration");
        return duration;
    }

    private Text thing() {
        Text thing = new Text(spell.getThingSingle() + "\r\n");
        thing.setId("thing");
        return thing;
    }

    private Text createCost() {
        Text createCost = new Text(spell.getCreateCostSingle() + "\r\n");
        createCost.setId("createCost");
        return createCost;
    }

    private void setButtons() {
        setVisibleButtons();

        addButton.setOnAction(event -> {
            new CharactersSpell(character.id, spell.id, spell.level, spell.finalCost).create();
            setCurrentPoints(spell.finalCost + Integer.parseInt(character.currentPoints));
            spell.setAddAndColorRow(true);
            setVisibleButtons();
        });

        removeButton.setOnAction(event -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("characterId", character.id);
            params.put("spellId", spell.id);
            CharactersSpell charactersSpell = (CharactersSpell) new CharactersSpell().find_by(params);
            setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersSpell.cost);
            charactersSpell.delete();
            spell.setAddAndColorRow(false);
            setVisibleButtons();
        });
    }

    private void setLevelAndFinalCost() {
        level.setText(Integer.toString(spell.level));
        finalCost.setText(Integer.toString(CharacterParams.spellCost(spell)));

        level.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue.equals(newValue) || newValue.equals("")) return;
            spell.level = Integer.parseInt(newValue);

            int cost = CharacterParams.spellCost(spell);
            finalCost.setText(Integer.toString(cost));
            spell.finalCost = cost;
        });
    }

    private void setVisibleButtons(){
        if (spell.add) {
            addButton.setVisible(false);
            removeButton.setVisible(true);
        } else {
            addButton.setVisible(true);
            removeButton.setVisible(false);
        }
    }
}
