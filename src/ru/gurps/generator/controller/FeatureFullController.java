package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FeatureFullController {
    private String testText;

    public FeatureFullController(String testText) {
        this.testText = testText;
    }

    @FXML
    private TextField test;

    @FXML
    private void initialize() {
        test.setText(testText);
    }

}
