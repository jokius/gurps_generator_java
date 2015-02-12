package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.lib.FeatureTableCell;
import ru.gurps.generator.pojo.Feature;
import java.sql.ResultSet;
import java.util.HashMap;

public class AdvantagesController extends FeatureAbstractController {
    @FXML
    private void initialize() {
//        maxPoints.setText("9000");
        initData();

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        FeatureTableCell cell = new FeatureTableCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new FeatureEventHandler());
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
        description.setCellFactory(cellFactory);

        featureTableView.setItems(featuresData);

    }

    private void initData() {
        try {
            ResultSet advantages = Db.find_by("features", "advantage", "true");
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
                        "1",
                        advantages.getString("max_level"),
                        advantages.getString("psi"),
                        advantages.getString("cybernetic"),
                        false
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
