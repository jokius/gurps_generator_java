package ru.gurps.generator.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.models.rules.Spell;



public class SpellFullController extends AbstractController {
    private Spell spell;

    public Label name;
    public Label cost;
    public Label type;
    public Label school;
    public Label complexity;
    public Label needTime;
    public Label duration;
    public Label maintainingCost;
    public Label thing;
    public Label demands;
    public Label createCost;
    public Text fullDescription;

    public SpellFullController(Spell spell) {
        this.spell = spell;
    }

    @FXML
    private void initialize() {
        name.setText(spell.name + " (" + spell.nameEn + " )");
        cost.setText(cost.getText() + spell.getCost());
        type.setText(type.getText() + spell.getSpellType());
        school.setText(school.getText() + spell.getSchool());
        complexity.setText(complexity.getText() + spell.getComplexityFull());
        needTime.setText(needTime.getText() + spell.needTime);
        duration.setText(duration.getText() + spell.demands);
        maintainingCost.setText(maintainingCost.getText() + spell.maintainingCost);
        thing.setText(thing.getText() + spell.thing);
        demands.setText(demands.getText() + spell.demands);
        createCost.setText(createCost.getText() + spell.createCost);
        fullDescription.setText(spell.description);
    }
}
