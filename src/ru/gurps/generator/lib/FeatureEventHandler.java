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
import org.h2.jdbc.JdbcSQLException;
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
    private ComboBox lvlComboBox;
    private Label currentPoints;

    public FeatureEventHandler(User user, ObservableList<Feature> featuresData, TableView<Feature> featureTableView, TableView<Addon> addonsTableView, AnchorPane bottomMenu,
                               Button add, Button remove, TableColumn tableActivate, TableColumn tableAddonName,
                               TableColumn<Addon, String> tableAddonNameEn, TableColumn tableAddonLevel, TableColumn tableAddonCost, Button full,
                               Label finalCost, Label lvlLabel, TextField lvlText, ComboBox lvlComboBox, TextField finalCostText,
                               Label currentPoints) {

        this.user = user;
        this.labelName = lvlLabel.getText() + " ";
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
        this.currentPoints = currentPoints;
    }

    @Override
    public void handle(MouseEvent t) {
        try {
            TableRow row = (TableRow) t.getSource();
            int index = row.getIndex();
            features = featuresData.get(index);
            String id = Integer.toString(features.getId());
            setupBottomMenu();
            setCurrentLvl();
            setCurrentCost();

            if (id.equals(lastId)) return;

            HashMap<String, String> params = new HashMap<>();
            params.put("userId", Integer.toString(user.id));
            params.put("featureId", id);
            ResultSet userFeature = new UserFeature().where(params);

            features.setAdd(new User().isAny(userFeature));

            if (features.isAdd()) {
                add.setVisible(false);
                remove.setVisible(true);
            } else {
                add.setVisible(true);
                remove.setVisible(false);
            }

            defaultParams(features);
            allAddons();

            if (!addonsArray.isEmpty()) {
                userAddons(userFeature);
                setCells();

                addonsTableView.setItems(addonsArray);
                addonsTableView.setEditable(true);
                addonsTableView.setVisible(true);
            } else {
                addonsTableView.setVisible(false);
            }

            buttonsActions();

            if(features.isAdd()) finalCost.setText(userFeature.getString("cost"));
        } catch (JdbcSQLException ignored) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double currentAddonCost(int cost, int level) {
        return cost * level / 100.0;
    }

    private void addonCost(double cost, Addon addon) {
        int lastCost;
        int intFinalCost = intCost();
        int result = (int) (features.getCost() * Double.parseDouble(currentLvl) * cost);
        int userCost = 0;

        System.out.println("addon.active" + addon.active);
        System.out.println("addon.resultCost" + addon.resultCost);
        System.out.println("intFinalCost" + intFinalCost);
        System.out.println("result" + result);

        if (addon.active) {
            lastCost = intFinalCost + result;
            if (features.isAdd()) userCost = Integer.parseInt(currentPoints.getText()) + result;
        } else {
            lastCost = intFinalCost - result;
            if (features.isAdd()) userCost = Integer.parseInt(currentPoints.getText()) - result;
        }

        System.out.println("userCost" + userCost);
        System.out.println("lastCost" + lastCost);
        if (features.isAdd()) {
            user.currentPoints = Integer.toString(userCost);
            currentPoints.setText(user.currentPoints);
            user.save();
        }

        newCost(lastCost);
    }

    private void userAddons(ResultSet userFeature) throws SQLException {
        if (!features.isAdd()) return;
        ResultSet featureAddons = new FeatureAddon().find_by("userFeatureId", userFeature.getString("id"));
        while (featureAddons.next()) {
            for (Addon addon : addonsArray) {
                int fId = featureAddons.getInt("addonId");
                if (addon.id == fId) {
                    addon.active = true;
                    addon.cost = featureAddons.getString("cost");
                    addon.level = featureAddons.getString("level");
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
        tableAddonCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tableAddonLevel.setEditable(true);
        tableAddonCost.setEditable(true);

        tableAddonLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        tableAddonLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Addon, String> event) {
                if ("\\D".matches(event.getNewValue()) || event.getNewValue().equals("0")) return;
                Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                if (addon.maxLevel != 1) addon.level = event.getNewValue();
            }
        });

        tableAddonCost.setCellFactory(TextFieldTableCell.forTableColumn());
        tableAddonCost.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Addon, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Addon, String> event) {
                if ("\\D".matches(event.getNewValue()) || event.getNewValue().equals("0")) return;
                Addon addon = event.getTableView().getItems().get(event.getTablePosition().getRow());
                if (addon.cost.equals("0")) addon.cost = event.getNewValue();
            }
        });
    }

    //Define the button cell
    private class ButtonCell extends TableCell<Addon, Boolean> {
        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");

        ButtonCell(){
            addButton.setOnAction(t -> {
                Addon addon = (Addon) getTableRow().getItem();
                new FeatureAddon().delete(addon.id);
                HashMap<String, String> addonParams = new HashMap<>();
                addonParams.put("userFeatureId", Integer.toString(features.getId()));
                addonParams.put("addonId", Integer.toString(addon.id));
                addonParams.put("level", addon.level);
                addonParams.put("cost", addon.cost);
                new FeatureAddon().create(addonParams);

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

    private void allAddons() throws SQLException {
        ResultSet addons = new Addon().find_by("featureId", lastId);
        addonsArray.removeAll();

        while (addons.next()) {
            addonsArray.add(new Addon(
                    addons.getInt("id"),
                    addons.getInt("featureId"),
                    addons.getString("title"),
                    addons.getString("titleEn"),
                    addons.getString("cost"),
                    0,
                    addons.getString("description"),
                    addons.getInt("maxLevel"),
                    false,
                    "1"
            ));
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
                    if (!oldValue.equals(newValue) || !newValue.equals("")) finalCost(Integer.parseInt(newValue));
                }
            });

            lvlLabel.setVisible(true);
            lvlText.setVisible(true);
            lvlComboBox.setVisible(false);
        } else if (maxLevel > 1) {
            lvlLabel.setVisible(true);
            lvlText.setVisible(false);
            lvlComboBox.setVisible(true);
            ObservableList<Integer> levels = FXCollections.observableArrayList();
            for(int i = 1; maxLevel >= i; i++) levels.add(i);
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
        int cost = features.getCost();
        if (cost == 0) {
            finalCost.setVisible(false);
            finalCostText.setVisible(true);
            finalCostText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if (!oldValue.equals(newValue) || !newValue.equals("")) newCost(newValue);
                }
            });
        } else {
            finalCost.setVisible(true);
            finalCostText.setVisible(false);
        }
    }

    private void finalCost(int newLevel) {
        int finalCost = intCost() / features.getOldLevel() * newLevel;
        features.setOldLevel(newLevel);
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

    private String stringCost() {
        return finalCost.isVisible() ? finalCost.getText() : finalCostText.getText();
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
        lvlText.setText("1");
        finalCost.setText(Integer.toString(features.getCost()));
        finalCostText.setText(Integer.toString(features.getCost()));
    }

    private void buttonsActions() {
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
                params1.put("userId", Integer.toString(user.id));
                params1.put("featureId", Integer.toString(features.getId()));
                params1.put("level", currentLvl);
                params1.put("cost", stringCost());
                ResultSet user_feature = new UserFeature().create(params1);

                String setCurrentPoints = Integer.toString(intCost() + Integer.parseInt(user.currentPoints));
                user.currentPoints = setCurrentPoints;

                HashMap<String, String> userParams = new HashMap<>();
                userParams.put("currentPoints", setCurrentPoints);
                new User().update(user.id, userParams);

                currentPoints.setText(setCurrentPoints);

                add.setVisible(false);
                remove.setVisible(true);

                if (!addonsTableView.isVisible()) return;

                user_feature.next();
                for (Addon addon : addonsArray) {
                    if (addon.active) {
                        new FeatureAddon().delete(addon.id);
                        params1.clear();
                        params1.put("userFeatureId", user_feature.getString("id"));
                        params1.put("addonId", Integer.toString(addon.id));
                        params1.put("level", addon.level);
                        params1.put("cost", addon.cost);
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
                params1.put("userId", Integer.toString(user.id));
                params1.put("featureId", Integer.toString(features.getId()));

                ResultSet user_feature = new UserFeature().where(params1);
                user_feature.next();
                int user_feature_id = user_feature.getInt("id");

                String setCurrentPoints = Integer.toString(Integer.parseInt(user.currentPoints) - user_feature.getInt("cost"));
                user.currentPoints = setCurrentPoints;
                HashMap<String, String> userParams = new HashMap<>();
                userParams.put("currentPoints", setCurrentPoints);
                new User().update(user.id, userParams);
                currentPoints.setText(setCurrentPoints);

                new UserFeature().delete(user_feature_id);
                add.setVisible(true);
                remove.setVisible(false);

                if (!addonsTableView.isVisible()) return;

                ResultSet feature_addons = new FeatureAddon().find_by("userFeatureId", Integer.toString(user_feature_id));
                while (feature_addons.next()) {
                    new FeatureAddon().delete(feature_addons.getInt("id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}