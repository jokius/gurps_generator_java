package ru.gurps.generator.controller.update;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.gurps.generator.controller.helpers.AbstractController;

public class NotHaveController extends AbstractController {
    public Button ok;

    @FXML
    private void initialize() throws Exception {
        ok.setOnAction(event -> ok.getScene().getWindow().hide());
    }
}
