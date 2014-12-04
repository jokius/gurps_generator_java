package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.pojo.Feature;

import java.sql.ResultSet;

public class DisadvantagesController {

    private ObservableList<Feature> advantagesData = FXCollections.observableArrayList();

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

    @FXML
    private void initialize(){
        initData();

        title.setCellValueFactory(new PropertyValueFactory<Feature, String>("title"));
        titleEn.setCellValueFactory(new PropertyValueFactory<Feature, String>("titleEn"));
        type.setCellValueFactory(new PropertyValueFactory<Feature, String>("type"));
        cost.setCellValueFactory(new PropertyValueFactory<Feature, Integer>("cost"));
        description.setCellValueFactory(new PropertyValueFactory<Feature, String>("description"));

        advantages.setItems(advantagesData);
    }

    private void initData(){
        new Db();
        try {
            ResultSet advantages;
            advantages = Db.connect.createStatement().executeQuery("SELECT * FROM features WHERE advantage = false");

            while (advantages.next()) {
                advantagesData.add(new Feature(
                        advantages.getInt("id"),
                        advantages.getString("title"),
                        advantages.getString("title_en"),
                        advantages.getString("type"),
                        advantages.getInt("cost"),
                        advantages.getString("description"),
                        advantages.getInt("max_level"),
                        advantages.getBoolean("psi"),
                        advantages.getBoolean("cybernetic")
                        ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
