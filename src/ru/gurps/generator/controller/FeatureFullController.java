package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Feature;

public class FeatureFullController extends AbstractController {
    private Feature feature;
    public Label name;
    public Label cost;
    public Label type;
    public Text fullDescription;

    public FeatureFullController(Feature feature) {
        this.feature = feature;
    }

    @FXML
    private void initialize() {
        name.setText(feature.title + " (" + feature.titleEn + ")");
        cost.setText(Main.locale.getString("cost") +": " + feature.cost);
        type.setText(feature.getTypeFull());
        fullDescription.setText(feature.description);
    }
}
