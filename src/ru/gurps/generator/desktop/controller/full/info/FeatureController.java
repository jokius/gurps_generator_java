package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.rules.Feature;

public class FeatureController extends AbstractController {
    private Feature feature;
    public Label name;
    public Label cost;
    public Label type;
    public Text fullDescription;

    public FeatureController(Feature feature) {
        this.feature = feature;
    }

    @FXML
    private void initialize() {
        name.setText(feature.name + " (" + feature.nameEn + ")");
        cost.setText(Main.locale.getString("cost") +": " + feature.cost);
        type.setText(feature.getTypeFull());
        fullDescription.setText(feature.description);
    }
}
