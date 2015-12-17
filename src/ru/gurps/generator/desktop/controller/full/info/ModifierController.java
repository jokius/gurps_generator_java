package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.rules.Modifier;

public class ModifierController extends AbstractController {
    private Modifier modifier;
    public Label name;
    public Label cost;
    public Label combat;
    public Label improving;
    public Text fullDescription;

    public ModifierController(Modifier modifier) {
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
