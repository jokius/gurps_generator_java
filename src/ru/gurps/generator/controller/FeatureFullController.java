package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class FeatureFullController {
    private String nameValue;
    private String costValue;
    private String typeValue;
    private String descriptionValue;
    @FXML
    private Label name;
    @FXML
    private Label cost;
    @FXML
    private Label type;
    @FXML
    private Text fullDescription;

    public FeatureFullController(String nameValue, String costValue, String typeValue, String descriptionValue) {
        this.nameValue = nameValue;
        this.costValue = costValue;
        this.typeValue = typeValue;
        this.descriptionValue = descriptionValue;
    }
    
    @FXML
    private void initialize() {
        name.setText(nameValue);
        cost.setText(costValue);
        type.setText(typeValue);
        fullDescription.setText(descriptionValue);
    }
}
