package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Modifier;

public class ModifierFullController extends AbstractController {
    private Modifier modifier;
    public Label name;
    public Label cost;
    public Label combat;
    public Label improving;
    public Text fullDescription;

    public ModifierFullController(Modifier modifier) {
        this.modifier = modifier;
    }

    @FXML
    private void initialize() {
        name.setText(modifier.name + " (" + modifier.nameEn + ")");
        cost.setText(Main.locale.getString("cost") +": " + modifier.cost);
        improving.setText(Main.locale.getString("improving") +": " + modifier.getImproving());
        combat.setText(Main.locale.getString("combat") +": " + modifier.getCombat());
        fullDescription.setText(modifier.description);
    }
}
