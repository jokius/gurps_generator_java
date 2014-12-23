package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.pojo.Addon;
import ru.gurps.generator.pojo.Feature;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FeatureAbstractController {
    protected ObservableList<Feature> featuresData = FXCollections.observableArrayList();
    protected ObservableList<Addon> addonsData;

    @FXML
    protected TableView<Feature> featureTableView;

    @FXML
    protected TableColumn title = new TableColumn("title");

    @FXML
    protected TableColumn titleEn = new TableColumn("titleEn");

    @FXML
    protected TableColumn type = new TableColumn("type");

    @FXML
    protected TableColumn cost = new TableColumn("cost");

    @FXML
    protected TableColumn<Feature, String> description = new TableColumn<Feature, String>("description");

    @FXML
    protected AnchorPane bottomMenu;

    @FXML
    protected Slider lvlSlider;

    @FXML
    protected MenuButton menuAddons;

    @FXML
    protected Label lvlLabel;

    @FXML
    protected TextField lvlText;

    @FXML
    protected Button add;

    @FXML
    protected Button full;

    @FXML
    protected Label finalCost;

    protected String lastId = null;

    class MyStringTableCell extends TableCell<Feature, String> {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

    class MyEventHandler implements EventHandler<MouseEvent> {
        private String id = null;
        private Feature features;
        private String currentLvl;
        private String labelName = lvlLabel+" ";

        @Override
        public void handle(MouseEvent t) {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            features = featuresData.get(index);
            id = features.getId();
            setupBottomMenu();
            setCurrentLvl();

            if (!id.equals(lastId)) {
                defaultParams(features);
                notAllAddons();

                if (addonsData.isEmpty()) {
                    menuAddons.setVisible(false);
                } else {
                    ArrayList<CheckMenuItem> elements = new ArrayList<CheckMenuItem>();
                    for (final Addon addon : addonsData) {
                        CheckMenuItem cm = new CheckMenuItem(addon.getTitle() + " " + addon.getCost() + "%");
                        cm.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                                addonCost(addon.getCost(), newValue);
                            }
                        });
                        elements.add(cm);
                    }
                    menuAddons.getItems().addAll(elements);
                    menuAddons.setVisible(true);
                }

                full.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String name = features.getTitle() + "(" + features.getTitleEn() + ")";
                        String cost = "Стоимость: " + features.getCost();
                        
                        Stage childrenStage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/views/feature_full.fxml"));
                        FeatureFullController controller = new FeatureFullController(name, cost, features.getType(), features.getDescription());
                        loader.setController(controller);
                        Parent childrenRoot;
                        try {
                            childrenRoot = (Parent) loader.load();
                            childrenStage.setScene(new Scene(childrenRoot, 650, 600));
                            childrenStage.setTitle("GURPSGenerator - " + name);
                            childrenStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        private void newCost(int changeCost) {
            String result = Integer.toString(Integer.parseInt(finalCost.getText()) + changeCost);
            finalCost.setText(result);
        }

        private void setupBottomMenu(){
            final double bottomMenuSize = 100.0;
            AnchorPane.setBottomAnchor(featureTableView, bottomMenuSize);
            bottomMenu.setVisible(true);
        }

        private void defaultParams(Feature features){
            lastId = features.getId();
            addonsData = FXCollections.observableArrayList();
            menuAddons.getItems().clear();
            lvlSlider.setValue(1);
            lvlSlider.setMinorTickCount(1);
            lvlText.setText("1");
            finalCost.setText(features.getCost());
        }

        private void notAllAddons(){
            try {
                ResultSet addons;
                addons = Db.connect.createStatement().executeQuery("SELECT * FROM addons WHERE features_id ="+lastId+
                        " and cost != 0 and max_level = 1");
                addonsData.removeAll();

                while (addons.next()) {
                    addonsData.add(new Addon(
                            addons.getString("id"),
                            addons.getString("features_id"),
                            addons.getString("title"),
                            addons.getString("title_en"),
                            addons.getString("cost"),
                            addons.getString("description"),
                            addons.getString("max_level")
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addonCost(String cost, boolean active){
            double addonCost = Double.parseDouble(cost) / 100.0;
            int result = (int) (Double.parseDouble(features.getCost()) * Double.parseDouble(currentLvl) * addonCost);

            if (active) {
                newCost(result);
            } else {
                newCost(-result);
            }
        }

        private void setCurrentLvl(){
            int maxLevel = Integer.parseInt(features.getMaxLevel());
            if (maxLevel == 0) {
                currentLvl = "1";
                lvlText.setText("1");
                lvlText.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if(!oldValue.equals(newValue)){
                            currentLvl = newValue;
                        }
                    }
                });

                lvlLabel.setVisible(true);
                lvlText.setVisible(true);
                lvlSlider.setVisible(false);
            } else if (maxLevel > 1) {
                lvlLabel.setVisible(true);
                lvlText.setVisible(false);
                lvlSlider.setVisible(true);
                lvlSlider.setMax(maxLevel);
                lvlSlider.setValue(1);
                lvlSlider.setMinorTickCount(maxLevel);
                lvlSlider.setMajorTickUnit(maxLevel - 1);
                lvlLabel.setText(labelName + Double.toString(lvlSlider.getValue()));
                lvlSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                        currentLvl = newVal.toString();
                        lvlLabel.setText(labelName + currentLvl);
                    }
                });
            } else {
                lvlText.setText("1");
                currentLvl = "1";
                lvlSlider.setMax(1);
                lvlSlider.setValue(1);
                lvlLabel.setVisible(false);
                lvlSlider.setVisible(false);
                lvlText.setVisible(false);
            }
        }
    }
}
