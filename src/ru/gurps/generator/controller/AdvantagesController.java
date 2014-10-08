package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import ru.gurps.generator.pojo.Feature;

public class AdvantagesController {
    private ObservableList<Feature> featurData = FXCollections.observableArrayList();

    @FXML
    private TableView<Feature> advantages;

    @FXML
    private TableColumn<Feature, String> title;

    @FXML
    private TableColumn<Feature, String> titleEn;

    @FXML
    private TableColumn<Feature, String> type;

    @FXML
    private TableColumn<Feature, Integer> cost;

    @FXML
    private TableColumn<Feature, String> description;

    private void initialize(){
        initData();

        title.setCellValueFactory(new PropertyValueFactory<Feature, String>("title"));
        titleEn.setCellValueFactory(new PropertyValueFactory<Feature, String>("titleEn"));
        type.setCellValueFactory(new PropertyValueFactory<Feature, String>("type"));
        cost.setCellValueFactory(new PropertyValueFactory<Feature, Integer>("cost"));
        description.setCellValueFactory(new PropertyValueFactory<Feature, String>("description"));

        advantages.setItems(featurData);
    }

    private void initData(){

    }
}
