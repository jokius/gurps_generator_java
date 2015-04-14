package ru.gurps.generator.lib;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.AbstractController;
import ru.gurps.generator.controller.FeatureFullController;
import ru.gurps.generator.models.*;

import java.io.IOException;
import java.util.HashMap;

public class FeatureEventHandler extends AbstractController implements EventHandler<MouseEvent> {
    private Feature feature;
    private String currentLvl;
    private String lastId;

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
    private ComboBox lvlComboBox;

    public FeatureEventHandler(ObservableList<Feature> featuresData, TableView<Feature> featureTableView, TableView<Addon> addonsTableView, AnchorPane bottomMenu,
                               Button add, Button remove, TableColumn tableActivate, TableColumn tableAddonName,
                               TableColumn<Addon, String> tableAddonNameEn, TableColumn tableAddonLevel, TableColumn tableAddonCost, Button full,
                               Label finalCost, Label lvlLabel, TextField lvlText, ComboBox lvlComboBox, TextField finalCostText) {

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
        this.lvlComboBox = lvlComboBox;
        this.finalCostText = finalCostText;
    }

    @Override
    public void handle(MouseEvent t) {
        TableRow row = (TableRow) t.getSource();
        int index = row.getIndex();
        feature = featuresData.get(index);
        String id = Integer.toString(feature.id);
        setupBottomMenu();
        setCurrentLvl();
        setCurrentCost();

        if(id.equals(lastId)) return;

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", user.id);
        params.put("featureId", id);
        UserFeature userFeature = (UserFeature) new UserFeature().find_by(params);

        feature.add = userFeature.id != null;

        if(feature.add) {
            add.setVisible(false);
            remove.setVisible(true);
        } else {
            add.setVisible(true);
            remove.setVisible(false);
        }

        defaultParams(feature);
        allAddons();

        if(!addonsArray.isEmpty()) {
            userAddons(userFeature.id);
            setCells();

            addonsTableView.setItems(addonsArray);
            addonsTableView.setEditable(true);
            addonsTableView.setVisible(true);
        } else {
            addonsTableView.setVisible(false);
        }

        buttonsActions();

        if(feature.add) finalCost.setText(Integer.toString(userFeature.cost));
    }

    private double currentAddonCost(int cost, int level) {
        return cost * level / 100.0;
    }

    private void addonCost(double cost, Addon addon) {
        int lastCost;
        int intFinalCost = intCost();
        int result = (int) (feature.getCost() * Double.parseDouble(currentLvl) * cost);
        int userCost = 0;

        if(addon.active) {
            lastCost = intFinalCost + result;
            if(feature.add) userCost = Integer.parseInt(globalCost.getText()) + result;
        } else {
            lastCost = intFinalCost - result;
            if(feature.add) userCost = Integer.parseInt(globalCost.getText()) - result;
        }

        if(feature.add) {
            user.update_single("currentPoints", Integer.toString(userCost));
            globalCost.setText(user.currentPoints);
        }

        newCost(lastCost);
    }

    private void userAddons(Integer id) {
        if(id == null) return;
        ObservableList<FeatureAddon> featureAddons = new FeatureAddon().where("userFeatureId", id);
        for(FeatureAddon featureAddon : featureAddons) {
            for(Addon addon : addonsArray) {
                if(addon.id == featureAddon.addonId) {
                    addon.active = true;
                    addon.cost = featureAddon.cost;
                    addon.level = featureAddon.level;
                }
            }
        }
    }

    private void setCells() {
        tableActivate.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Addon, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Addon, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        tableActivate.setCellFactory(p -> new ButtonCell());

        tableAddonName.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableAddonNameEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        tableAddonLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        tableAddonLevel.setEditable(true);
        tableAddonCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tableAddonCost.setEditable(true);

        tableAddonLevel.setCellFactory(TextFieldTableCell.forTableColumn());
        tableAddonLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Addon, String> event) {
                if("\\D".matches(event.getNewValue()) || event.getNewValue().equals("0")) return;
                Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                if(addon.maxLevel != 1) addon.level = event.getNewValue();
            }
        });

        tableAddonCost.setCellFactory(TextFieldTableCell.forTableColumn());
        tableAddonCost.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Addon, String> event) {
                if("\\D".matches(event.getNewValue()) || event.getNewValue().equals("0")) return;
                Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                if(addon.cost.equals("0")) addon.cost = event.getNewValue();
            }
        });
    }

    //Define the button cell
    private class ButtonCell extends TableCell<Addon, Boolean> {
        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");

        ButtonCell() {
            addButton.setOnAction(t -> {
                Addon addon = (Addon) getTableRow().getItem();
                new FeatureAddon().delete(addon.id);
                new FeatureAddon(feature.id, addon.id, addon.cost, addon.level).create();
                addon.active = true;
                double addonCost = currentAddonCost(Integer.parseInt(addon.cost), Integer.parseInt(addon.level));
                addonCost(addonCost, addon);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Addon addon = (Addon) getTableRow().getItem();
                new FeatureAddon().delete(addon.id);

                addon.active = false;
                double addonCost = currentAddonCost(Integer.parseInt(addon.cost), Integer.parseInt(addon.level));
                addonCost(addonCost, addon);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(empty) return;
            Addon addon = (Addon) getTableRow().getItem();
            if(addon == null) return;
            setGraphic(addon.active ? removeButton : addButton);
        }
    }

    private void allAddons() {
        ObservableList addons = new Addon().where("featureId", lastId);
        addonsArray.removeAll();
        addonsArray.addAll(addons);
    }

    private void setCurrentLvl() {
        if(feature.maxLevel == 0) {
            currentLvl = "1";
            lvlText.setText("1");
            lvlText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if(!oldValue.equals(newValue) || !newValue.equals("")) finalCost(Integer.parseInt(newValue));
                }
            });

            lvlLabel.setVisible(true);
            lvlText.setVisible(true);
            lvlComboBox.setVisible(false);
        } else if(feature.maxLevel > 1) {
            lvlLabel.setVisible(true);
            lvlText.setVisible(false);
            lvlComboBox.setVisible(true);
            ObservableList<Integer> levels = FXCollections.observableArrayList();
            for(int i = 1; feature.maxLevel >= i; i++) levels.add(i);
            lvlComboBox.setItems(levels);
            lvlComboBox.setValue(1);
            lvlComboBox.valueProperty().addListener((observable, oldValue, newValue) -> finalCost((Integer) newValue));
        } else {
            lvlText.setText("1");
            currentLvl = "1";
            lvlLabel.setVisible(false);
            lvlComboBox.setVisible(false);
            lvlText.setVisible(false);
        }
    }

    private void setCurrentCost() {
        int cost = feature.getCost();
        if(cost == 0) {
            finalCost.setVisible(false);
            finalCostText.setVisible(true);
            finalCostText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if(!oldValue.equals(newValue) || !newValue.equals("")) newCost(newValue);
                }
            });
        } else {
            finalCost.setVisible(true);
            finalCostText.setVisible(false);
        }
    }

    private void finalCost(int newLevel) {
        int finalCost = intCost() / feature.oldLevel * newLevel;
        feature.oldLevel = newLevel;
        newCost(finalCost);
    }

    private void newCost(int changeCost) {
        String result = Integer.toString(changeCost);
        finalCost.setText(result);
        finalCostText.setText(result);
    }

    private void newCost(String changeCost) {
        finalCost.setText(changeCost);
        finalCostText.setText(changeCost);
    }

    private int intCost() {
        return finalCost.isVisible() ? Integer.parseInt(finalCost.getText()) : Integer.parseInt(finalCostText.getText());
    }

    private void setupBottomMenu() {
        final double bottomMenuSize = 134.0;
        AnchorPane.setBottomAnchor(featureTableView, bottomMenuSize);
        bottomMenu.setVisible(true);
    }

    private void defaultParams(Feature features) {
        lastId = Integer.toString(features.id);
        addonsArray = FXCollections.observableArrayList();
        addonsArray.clear();
        lvlText.setText("1");
        finalCost.setText(Integer.toString(features.getCost()));
        finalCostText.setText(Integer.toString(features.getCost()));
    }

    private void buttonsActions() {
        full.setOnAction(actionEvent -> {
            String name = feature.getTitle() + "(" + feature.getTitleEn() + ")";
            String cost = "Стоимость: " + feature.getCost();

            Stage childrenStage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/feature_full.fxml"));
            FeatureFullController controller = new FeatureFullController(name, cost, feature.getType(), feature.getDescription());
            loader.setController(controller);
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 325, 400));
                childrenStage.setTitle("GURPSGenerator - " + name);
                childrenStage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });

        add.setOnAction(actionEvent -> {
            UserFeature user_feature = (UserFeature) new UserFeature(user.id, feature.id, intCost(), Integer.parseInt(currentLvl)).create();
            String setCurrentPoints = Integer.toString(intCost() + Integer.parseInt(user.currentPoints));
            user.update_single("currentPoints", setCurrentPoints);

            globalCost.setText(setCurrentPoints);
            feature.add = true;
            add.setVisible(false);
            remove.setVisible(true);

            if(!addonsTableView.isVisible()) return;
            addonsArray.stream().filter(addon -> addon.active).forEach(addon -> {
                new FeatureAddon().delete(addon.id);
                new FeatureAddon(user_feature.id, addon.id, addon.cost, addon.level).create();
            });
        });

        remove.setOnAction(actionEvent -> {
            HashMap<String, Object> params1 = new HashMap<>();
            params1.put("userId", Integer.toString(user.id));
            params1.put("featureId", feature.id);
            UserFeature user_feature = (UserFeature) new UserFeature().find_by(params1);

            String setCurrentPoints = Integer.toString(Integer.parseInt(user.currentPoints) - user_feature.cost);
            user.update_single("currentPoints", setCurrentPoints);
            globalCost.setText(setCurrentPoints);
            user_feature.delete();
            feature.add = false;
            add.setVisible(true);
            remove.setVisible(false);
            if(!addonsTableView.isVisible()) return;
            ObservableList<FeatureAddon> featureAddons = new FeatureAddon().where("userFeatureId", user_feature.id);
            for(FeatureAddon featureAddon : featureAddons) featureAddon.delete();
        });
    }
}