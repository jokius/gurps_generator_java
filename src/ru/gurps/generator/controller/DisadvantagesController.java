package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.lib.FeatureTableCell;
import ru.gurps.generator.pojo.Feature;

import java.sql.ResultSet;

public class DisadvantagesController extends FeatureAbstractController {
    @FXML
    private void initialize() {
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
        cost.setCellFactory(cellFactory);

        featureTableView.setItems(featuresData);

    }

    private void initData() {
        try {
            ResultSet disadvantages = Db.find_by("features", "advantage", "true");

            featuresData.removeAll();
            while (disadvantages.next()) {
                featuresData.add(new Feature(
                        disadvantages.getString("id"),
                        disadvantages.getString("advantage"),
                        disadvantages.getString("title"),
                        disadvantages.getString("title_en"),
                        disadvantages.getString("type"),
                        disadvantages.getString("cost"),
                        disadvantages.getString("description"),
                        "1",
                        disadvantages.getString("max_level"),
                        disadvantages.getString("psi"),
                        disadvantages.getString("cybernetic"),
                        false
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
