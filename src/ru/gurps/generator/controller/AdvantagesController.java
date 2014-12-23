package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.pojo.Feature;
import java.sql.ResultSet;

public class AdvantagesController extends FeatureObstractController {
    @FXML
    private void initialize() {
        initData();

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        MyStringTableCell cell = new MyStringTableCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };

        title.setCellValueFactory(new PropertyValueFactory<Feature, String>("title"));
        title.setCellFactory(cellFactory);

        titleEn.setCellValueFactory(new PropertyValueFactory<Feature, String>("titleEn"));
        titleEn.setCellFactory(cellFactory);

        type.setCellValueFactory(new PropertyValueFactory<Feature, String>("type"));
        type.setCellFactory(cellFactory);

        cost.setCellValueFactory(new PropertyValueFactory<Feature, String>("cost"));
        cost.setCellFactory(cellFactory);

        description.setCellValueFactory(new PropertyValueFactory<Feature, String>("description"));
        cost.setCellFactory(cellFactory);

        featureTableView.setItems(featuresData);

    }

    private void initData() {
        new Db();
        try {
            ResultSet advantages;
            advantages = Db.connect.createStatement().executeQuery("SELECT * FROM features WHERE advantage = TRUE");

            featuresData.removeAll();
            while (advantages.next()) {
                featuresData.add(new Feature(
                        advantages.getString("id"),
                        advantages.getString("advantage"),
                        advantages.getString("title"),
                        advantages.getString("title_en"),
                        advantages.getString("type"),
                        advantages.getString("cost"),
                        advantages.getString("description"),
                        advantages.getString("max_level"),
                        advantages.getString("psi"),
                        advantages.getString("cybernetic")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
