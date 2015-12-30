package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.rules.Skill;


public class SkillController extends AbstractController {
    private Skill skill;

    public Label name;
    public Label type;
    public Label complexity;
    public Label defaultUse;
    public Label demands;
    public Label twoHands;
    public Label parry;
    public Label parryBonus;
    public Text fullDescription;
    public Text modifiers;

    public SkillController(Skill skill) {
        this.skill = skill;
    }

    @FXML
    private void initialize() {
        name.setText(skill.name + " (" + skill.nameEn + " )");
        type.setText(type.getText() + " " + skill.getTypeFull());
        complexity.setText(complexity.getText() + skill.getComplexityFull());
        defaultUse.setText(defaultUse.getText() + skill.getDefaultUse());
        demands.setText(demands.getText() + skill.getDemands());
        twoHands.setText(twoHands.getText() + skill.getTwoHands());
        parry.setText(parry.getText() + skill.getParry());
        parryBonus.setText(parryBonus.getText() + skill.parryBonus);
        fullDescription.setText(skill.description + "\n");
        modifiers.setText(skill.getModifiers());
    }
}
