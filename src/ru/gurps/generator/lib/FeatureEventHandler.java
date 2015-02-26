package ru.gurps.generator.lib;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.FeatureFullController;
import ru.gurps.generator.models.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class FeatureEventHandler implements EventHandler<MouseEvent> {
    private Feature features;
    private String currentLvl;
    private String lastId;

    private User user;
    private String labelName;
    private ObservableList<Feature> featuresData;
    private ObservableList<Addon> addonsArray;
    private TableView<Feature> featureTableView;
    private TableView<Addon> addonsTableView;
    private AnchorPane bottomMenu;
    private Button add;
    private Button remove;
    private TableColumn tableActivate;
    private TableColumn tableAddonName;
    private TableColumn tableAddonNameEn;
    private TableColumn tableAddonLevel;
    private TableColumn tableAddonCost;
    private Button full;
    private Label finalCost;
    private TextField finalCostText;
    private Label lvlLabel;
    private TextField lvlText;
    private Slider lvlSlider;

    public FeatureEventHandler(User user, ObservableList<Feature> featuresData, TableView<Feature> featureTableView, TableView<Addon> addonsTableView, AnchorPane bottomMenu,
                               Button add, Button remove, TableColumn tableActivate, TableColumn tableAddonName,
                               TableColumn<Addon, String> tableAddonNameEn, TableColumn tableAddonLevel, TableColumn tableAddonCost, Button full,
                               Label finalCost, Label lvlLabel, TextField lvlText, Slider lvlSlider, TextField finalCostText) {

        this.user = user;
        this.labelName = lvlLabel + " ";
        this.featuresData = featuresData;
        this.featureTableView = featureTableView;
        this.addonsTableView = addonsTableView;
        this.bottomMenu = bottomMenu;
        this.add = add;
        this.remove = remove;
        this.tableActivate = tableActivate;
        this.tableAddonName = tableAddonName;
        this.tableAddonNameEn = tableAddonNameEn;
        this.tableAddonLevel = tableAddonLevel;
        this.tableAddonCost = tableAddonCost;
        this.full = full;
        this.finalCost = finalCost;
        this.lvlLabel = lvlLabel;
        this.lvlText = lvlText;
        this.lvlSlider = lvlSlider;
        this.finalCostText = finalCostText;
    }

    @Override
    public void handle(MouseEvent t) {
        TableRow row = (TableRow) t.getSource();
        int index = row.getIndex();
        features = featuresData.get(index);
        String id = Integer.toString(features.getId());
        setupBottomMenu();
        setCurrentLvl();

        if (!id.equals(lastId)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", Integer.toString(user.getId()));
            params.put("feature_id", id);
            ResultSet userFeature = new UserFeature().where(params);
            features.setAdd(new User().isAny(userFeature));

            if (features.isAdd()) {
                add.setVisible(false);
                remove.setVisible(true);
            } else {
                add.setVisible(true);
                remove.setVisible(false);
            }

            class AddonEventHandler implements EventHandler<MouseEvent> {
                @Override
                public void handle(MouseEvent t) {
                    TableRow row = (TableRow) t.getSource();
                    int index = row.getIndex();
                    Addon addon = addonsArray.get(index);
                    double addonCost = currentAddonCost(Integer.parseInt(addon.getCost()), Integer.parseInt(addon.getLevel()));
                    row.commitEdit(!addon.getActive());
                    addon.setActive(!addon.getActive());

                    addonCost(addonCost, addon);
                }
            }

            defaultParams(features);
            allAddons();

            if (addonsArray.isEmpty()) {
                addonsTableView.setVisible(false);
            } else {
                if (features.isAdd()) {
                    try {
                        userFeature.next();
                        ResultSet featureAddons = new FeatureAddon().find_by("user_feature_id", userFeature.getString("id"));
                        if (new FeatureAddon().isAny(featureAddons)) {
                            while (featureAddons.next()) {
                                for (Addon addon : addonsArray) {
                                    int fId = featureAddons.getInt("id");
                                    if (addon.getId() == fId) {
                                        addon.setActive(true);
                                        addon.setCost(featureAddons.getString("cost"));
                                        addon.setLevel(featureAddons.getString("level"));
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                tableActivate.setCellValueFactory(new PropertyValueFactory<>("activate"));
                tableActivate.setCellFactory(cell -> {
                    CheckBoxTableCell<Addon, Boolean> cb = new CheckBoxTableCell<>();
                    cb.setOnMouseClicked(new AddonEventHandler());
                    return cb;
                });

                tableAddonName.setCellValueFactory(new PropertyValueFactory<>("title"));
                tableAddonNameEn.setCellValueFactory(new PropertyValueFactory<>("title_en"));
                tableAddonLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
                tableAddonCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
                tableAddonLevel.setEditable(true);
                tableAddonCost.setEditable(true);

                tableAddonLevel.setCellFactory(TextFieldTableCell.forTableColumn());
                tableAddonLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Addon, Integer> event) {
                        int level = event.getNewValue();
                        if (level == 0) return;

                        Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                        if (addon.getMaxLevel() == 1) return;
                        addon.setLevel(Integer.toString(level));
                    }
                });

                tableAddonCost.setCellFactory(TextFieldTableCell.forTableColumn());
                tableAddonCost.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Addon, Integer> event) {
                        int cost = event.getNewValue();
                        if (cost == 0) return;

                        Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                        if (addon.getCost().equals("0")) return;

                        addon.setCost(Integer.toString(cost));
                    }
                });

                addonsTableView.setItems(addonsArray);
                addonsTableView.setEditable(true);
                addonsTableView.setVisible(true);
            }

            full.setOnAction(actionEvent -> {
                String name = features.getTitle() + "(" + features.getTitleEn() + ")";
                String cost = "Стоимость: " + features.getCost();

                Stage childrenStage = new Stage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/feature_full.fxml"));
                FeatureFullController controller = new FeatureFullController(name, cost, features.getType(), features.getDescription());
                loader.setController(controller);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setScene(new Scene(childrenRoot, 325, 400));
                    childrenStage.setTitle("GURPSGenerator - " + name);
                    childrenStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            add.setOnAction(actionEvent -> {
                try {
                    HashMap<String, String> params1 = new HashMap<>();
                    params1.put("user_id", Integer.toString(user.getId()));
                    params1.put("feature_id", Integer.toString(features.getId()));
                    params1.put("level", currentLvl);
                    params1.put("cost", finalCost.getText());
                    ResultSet user_feature = new FeatureAddon().create(params1);

                    String setCurrentPoints = Integer.toString(Integer.parseInt(finalCost.getText()) + Integer.parseInt(user.getCurrentPoints()));
                    user.setCurrentPoints(setCurrentPoints);
                    HashMap<String, String> userParams = new HashMap<>();
                    userParams.put("points", setCurrentPoints);
                    new User().update(user.getId(), userParams);

                    add.setVisible(false);
                    remove.setVisible(true);

                    if (!addonsTableView.isVisible()) return;

                    user_feature.next();
                    for (Addon addon : addonsArray) {
                        if (addon.getActive()) {
                            params1.clear();
                            params1.put("user_feature_id", user_feature.getString("id"));
                            params1.put("addon_id", Integer.toString(addon.getId()));
                            params1.put("level", addon.getLevel());
                            params1.put("cost", Integer.toString(addon.getResultCost()));
                            new FeatureAddon().create(params1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            remove.setOnAction(actionEvent -> {
                try {
                    HashMap<String, String> params1 = new HashMap<>();
                    params1.put("user_id", Integer.toString(user.getId()));
                    params1.put("feature_id", Integer.toString(features.getId()));

                    ResultSet user_feature = new UserFeature().where(params1);
                    user_feature.next();
                    int user_feature_id = user_feature.getInt("id");

                    String currentPoints1 = Integer.toString(Integer.parseInt(user.getCurrentPoints()) - user_feature.getInt("cost"));
                    user.setCurrentPoints(currentPoints1);
                    HashMap<String, String> userParams = new HashMap<>();
                    userParams.put("points", currentPoints1);
                    new User().update(user.getId(), userParams);

                    new UserFeature().delete(user_feature_id);
                    add.setVisible(true);
                    remove.setVisible(false);

                    if (!addonsTableView.isVisible()) return;

                    ResultSet feature_addons = new FeatureAddon().find_by("user_feature_id", Integer.toString(user_feature_id));
                    while (feature_addons.next()) {
                        new FeatureAddon().delete(feature_addons.getInt("id"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private double currentAddonCost(int cost, int level) {
        return cost * level / 100.0;
    }

    private void addonCost(double cost, Addon addon) {
        int resultCost = addon.getResultCost();
        String lastCost;
        int intFinalCost = Integer.parseInt(finalCost.getText());
        int result = (int) (features.getCost() * Double.parseDouble(currentLvl) * cost);

        if (addon.getActive()) {
            lastCost = Integer.toString(intFinalCost - resultCost + result);
            addon.setResultCost(result);
        } else {
            lastCost = resultCost == result ? Integer.toString(intFinalCost - result) : Integer.toString(intFinalCost + result);
            addon.setResultCost(0);
        }
        finalCost.setText(lastCost);
    }

    private void allAddons() {
        try {
            ResultSet addons = new Addon().find_by("feature_id", lastId);
            addonsArray.removeAll();

            while (addons.next()) {
                addonsArray.add(new Addon(
                        addons.getInt("id"),
                        addons.getInt("feature_id"),
                        addons.getString("title"),
                        addons.getString("title_en"),
                        addons.getString("cost"),
                        0,
                        addons.getString("description"),
                        addons.getInt("max_level"),
                        false,
                        "1"
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCurrentLvl() {
        int maxLevel = features.getMaxLevel();
        if (maxLevel == 0) {
            currentLvl = "1";
            lvlText.setText("1");
            lvlText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if (!oldValue.equals(newValue)) {
                        if (!newValue.equals("")) {
                            int finalCost = finalCost(Integer.parseInt(newValue));
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
                    int finalCost = finalCost(Integer.parseInt(currentLvl));
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
    
    

    private int finalCost(int newLevel) {
        int cost = finalCost.getText().equals("") ? features.getCost() : Integer.parseInt(finalCost.getText());
        int finalCost = cost / features.getOldLevel() * newLevel;
        features.setOldLevel(newLevel);
        newCost(finalCost);
        return finalCost;
    }

    private void newCost(int changeCost) {
        String result = Integer.toString(changeCost);
        finalCost.setText(result);
    }

    private void setupBottomMenu() {
        final double bottomMenuSize = 134.0;
        AnchorPane.setBottomAnchor(featureTableView, bottomMenuSize);
        bottomMenu.setVisible(true);
    }

    private void defaultParams(Feature features) {
        lastId = Integer.toString(features.getId());
        addonsArray = FXCollections.observableArrayList();
        addonsArray.clear();
        lvlSlider.setValue(1);
        lvlSlider.setMinorTickCount(1);
        lvlText.setText("1");
        finalCost.setText(Integer.toString(features.getCost()));
        finalCostText.setText(Integer.toString(features.getCost()));
    }
}