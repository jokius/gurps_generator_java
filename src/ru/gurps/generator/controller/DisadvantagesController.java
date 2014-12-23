package ru.gurps.generator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.pojo.Feature;

import java.sql.ResultSet;

public class DisadvantagesController extends FeatureObstractController {
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
            ResultSet disadvantages;
            disadvantages = Db.connect.createStatement().executeQuery("SELECT * FROM features WHERE advantage = FALSE");

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
                        disadvantages.getString("max_level"),
                        disadvantages.getString("psi"),
                        disadvantages.getString("cybernetic")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
