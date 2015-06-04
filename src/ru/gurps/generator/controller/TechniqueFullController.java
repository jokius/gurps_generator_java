package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.gurps.generator.models.Technique;

public class TechniqueFullController {
    private Technique technique;

    public Label name;
    public Label complexity;
    public Label defaultUse;
    public Label demands;
    public Text fullDescription;

    public TechniqueFullController(Technique technique) {
        this.technique = technique;
    }

    @FXML
    private void initialize() {
        name.setText(technique.name + " (" + technique.nameEn + " )");
        complexity.setText(complexity.getText() + technique.getComplexityFull());
        defaultUse.setText(defaultUse.getText() + technique.getDefaultUse());
        demands.setText(demands.getText() + technique.getDemands());
        fullDescription.setText(technique.description + "\n");
    }
}
