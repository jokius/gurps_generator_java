package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import ru.gurps.generator.pojo.User;


public class MainController {
    protected static User user;

    @FXML
    protected TextField sm;

    @FXML
    protected CheckBox noFineManipulators;

    @FXML
    protected TextField st;

    @FXML
    protected Label stCost;

    @FXML
    protected TextField dx;

    @FXML
    protected Label dxCost;

    @FXML
    protected TextField iq;

    @FXML
    protected Label iqCost;

    @FXML
    protected TextField ht;

    @FXML
    protected Label htCost;

    @FXML
    protected TextField hp;

    @FXML
    protected Label hpCost;

    @FXML
    protected TextField will;

    @FXML
    protected Label willCost;

    @FXML
    protected TextField per;

    @FXML
    protected Label perCost;

    @FXML
    protected TextField fp;

    @FXML
    protected Label fpCost;

    @FXML
    protected TextField bs;

    @FXML
    protected Label bsCost;

    @FXML
    protected Label bg;

    @FXML
    protected TextField move;

    @FXML
    protected Label moveCost;

    @FXML
    protected Label doge;

    @FXML
    protected Label thrust;

    @FXML
    protected Label swing;

    @FXML
    protected TextField maxPoints;

    @FXML
    protected Label currentPoints;
}
