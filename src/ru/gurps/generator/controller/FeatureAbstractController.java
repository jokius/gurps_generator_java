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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.pojo.Addon;
import ru.gurps.generator.pojo.Feature;

import java.io.IOException;
import java.sql.ResultSet;

public class FeatureAbstractController {
    protected ObservableList<Feature> featuresData = FXCollections.observableArrayList();
    protected ObservableList<Addon> addonsArray = FXCollections.observableArrayList();

    @FXML
    protected TableView<Feature> featureTableView;

    @FXML
    protected TableView<Addon> addonsTableView;

    @FXML
    protected TableColumn title = new TableColumn("title");

    @FXML
    protected TableColumn titleEn = new TableColumn("titleEn");

    @FXML
    protected TableColumn type = new TableColumn("type");

    @FXML
    protected TableColumn cost = new TableColumn("cost");

    @FXML
    protected TableColumn description = new TableColumn("description");

    @FXML
    protected AnchorPane bottomMenu;

    @FXML
    protected Slider lvlSlider;

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
    
    @FXML
    protected TableColumn activate = new TableColumn<Addon, Boolean>("activate");
    
    @FXML
    protected TableColumn addonName = new TableColumn<Addon, String>("addonName");

    @FXML
    protected TableColumn addonNameEn = new TableColumn<Addon, String>("addonNameEn");

    @FXML
    protected TableColumn addonLevel= new TableColumn<Addon, String>("addonLevel");

    @FXML
    protected TableColumn addonCost;

    protected String lastId = null;
    

    class FeatureEventHandler implements EventHandler<MouseEvent> {
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
                class AddonEventHandler implements EventHandler<MouseEvent> {
                    @Override
                    public void handle(MouseEvent t) {
                        TableCell cell = (TableCell) t.getSource();
                        
                        int index = cell.getIndex();
                        Addon addon = addonsArray.get(index);
                        double addonCost = currentAddonCost(addon.getCost(), addon.getLevel());
                        if(addon.getActive()) {
                            cell.commitEdit(false);
                            addon.setActive(false);
                        }
                        else{
                            cell.commitEdit(true);
                            addon.setActive(true);
                        }
                        
                        addonCost(addonCost, addon);
                    }
                }
                
                defaultParams(features);
                allAddons();

                if (addonsArray.isEmpty()) {
                    addonsTableView.setVisible(false);
                } else {
                    activate.setCellValueFactory(new PropertyValueFactory("activate"));
                    activate.setCellFactory(new Callback<TableColumn<Addon, Boolean>, TableCell<Addon, Boolean>>() {
                        @Override
                        public TableCell<Addon, Boolean> call(TableColumn<Addon, Boolean> p) {
                            CheckBoxTableCell cb = new CheckBoxTableCell<Addon, Boolean>();
                            cb.addEventFilter(MouseEvent.MOUSE_CLICKED, new AddonEventHandler());
                            return cb;
                        }
                    });

                    addonName.setCellValueFactory(new PropertyValueFactory("title"));
                    addonNameEn.setCellValueFactory(new PropertyValueFactory("titleEn"));
                    addonLevel.setCellValueFactory(new PropertyValueFactory("level"));
                    addonCost.setCellValueFactory(new PropertyValueFactory("cost"));
                    addonLevel.setEditable(true);
                    addonCost.setEditable(true);

                    addonLevel.setCellFactory(TextFieldTableCell.forTableColumn());
                    addonLevel.setOnEditCommit(new EventHandler<CellEditEvent<Addon, String>>() {
                        @Override
                        public void handle(CellEditEvent<Addon, String> event) {
                            int level = Integer.parseInt(event.getNewValue());
                            if (level == 0) return;
                            
                            Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                            if(addon.getMaxLevel().equals("1")) return;
                            
                            int oldValue = Integer.parseInt(addon.getLevel());
                            double addonCost = currentAddonCost(addon.getCost(), event.getNewValue());

                            addon.setLevel(Integer.toString(level));
                            addon.setActive(true);

                            if(oldValue < level){
                                addonCost(addonCost, addon);
                            }
                            else if(oldValue > level) {
                                addonCost(addonCost, addon);
                            }
                        }
                    });

                    addonCost.setCellFactory(TextFieldTableCell.forTableColumn());
                    addonCost.setOnEditCommit(new EventHandler<CellEditEvent<Addon, String>>() {
                        @Override
                        public void handle(CellEditEvent<Addon, String> event) {
                            int cost = Integer.parseInt(event.getNewValue());
                            if (cost == 0) {
                                return;
                            }

                            Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                            int oldValue = Integer.parseInt(addon.getCost());
                            double addonCost = currentAddonCost(event.getNewValue(), addon.getLevel());

                            addon.setCost(event.getNewValue());
                            addon.setActive(true);

                            if(oldValue < cost){
                                addonCost(addonCost, addon);
                            }
                            else if(oldValue > cost){
                                addonCost(addonCost, addon);
                            }
                        }
                    });


                    addonsTableView.setItems(addonsArray);
                    addonsTableView.setEditable(true);
                    addonsTableView.setVisible(true);
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
                            childrenStage.setScene(new Scene(childrenRoot, 325, 400));
                            childrenStage.setTitle("GURPSGenerator - " + name);
                            childrenStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                add.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        
                    }
                });
            }
        }
        
        private double currentAddonCost(String cost, String level){
            return Integer.parseInt(cost) * Integer.parseInt(level) / 100.0;
        }

        private void addonCost(double cost, Addon addon){
            int resultCost = Integer.parseInt(addon.getResultCost());
            String lastCost;
            int intFinalCost = Integer.parseInt(finalCost.getText());
            int result = (int) (Double.parseDouble(features.getCost()) * Double.parseDouble(currentLvl) * cost);

            if (addon.getActive()) {
                lastCost = Integer.toString(intFinalCost - resultCost + result);
                addon.setResultCost(Integer.toString(result));
            } else {
                if (resultCost == result){
                    lastCost = Integer.toString(intFinalCost - result);
                }
                else{
                    lastCost = Integer.toString(intFinalCost + result);
                }
                
                addon.setResultCost("0");
            }
            finalCost.setText(lastCost);
        }

        private void allAddons(){
            try {
                ResultSet addons = Db.find_by("addons", "features_id", lastId);
                addonsArray.removeAll();

                while (addons.next()) {
                    addonsArray.add(new Addon(
                            addons.getString("id"),
                            addons.getString("features_id"),
                            addons.getString("title"),
                            addons.getString("title_en"),
                            addons.getString("cost"),
                            "0",
                            addons.getString("description"),
                            addons.getString("max_level"),
                            false,
                            "1"
                    ));
                }

            } catch (Exception e) {
                e.printStackTrace();
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
                            if(!newValue.equals("")){
                                int finalCost = finalCost(newValue);
                                newCost(finalCost);
                            }
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
                        int finalCost = finalCost(currentLvl);
                        newCost(finalCost);
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

        private int finalCost(String newLevel){
            int cost = finalCost.getText().equals("") ? Integer.parseInt(features.getCost()) :
                    Integer.parseInt(finalCost.getText());
            
            int finalCost = cost / Integer.parseInt(features.getOldLevel()) * Integer.parseInt(newLevel);
            features.setOldLevel(newLevel);
            newCost(finalCost);
            return finalCost;
        }
    }
    
    private void newCost(int changeCost) {
        String result = Integer.toString(changeCost);
        finalCost.setText(result);
    }

    private void setupBottomMenu(){
        final double bottomMenuSize = 134.0;
        AnchorPane.setBottomAnchor(featureTableView, bottomMenuSize);
        bottomMenu.setVisible(true);
    }

    private void defaultParams(Feature features){
        lastId = features.getId();
        addonsArray = FXCollections.observableArrayList();
        addonsArray.clear();
        lvlSlider.setValue(1);
        lvlSlider.setMinorTickCount(1);
        lvlText.setText("1");
        finalCost.setText(features.getCost());
    }
}
