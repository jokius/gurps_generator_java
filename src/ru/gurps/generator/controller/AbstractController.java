package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.lib.*;
import ru.gurps.generator.models.*;
import java.io.IOException;
import java.util.ArrayList;

public class AbstractController extends ViewsAbstract {
    public static User user;
    public static Label globalCost;

    protected ObservableList<Feature> advantagesData = FXCollections.observableArrayList();
    protected ObservableList<Feature> disadvantagesData = FXCollections.observableArrayList();
    protected ArrayList<Integer> advantagesNumbers = new ArrayList<>();
    protected ArrayList<Integer> disadvantagesNumbers = new ArrayList<>();


    protected void textEvents() {
        advantagesSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) advantagesSearchButton.setDisable(true);
                else advantagesSearchButton.setDisable(false);
            }
        });

        disadvantagesSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")) disadvantagesSearchButton.setDisable(true);
                else disadvantagesSearchButton.setDisable(false);
            }
        });
    }

    protected void cellEvents() {
        initData();

        FeatureEventHandler advantagesFeatureEventHandler = new FeatureEventHandler(user, advantagesData, advantagesView,
                advantagesAddonsTableView, advantagesBottomMenu, advantagesAdd, advantagesRemove, advantagesActivate,
                advantagesAddonName, advantagesAddonNameEn, advantagesAddonLevel, advantagesAddonCost,
                advantagesFull, advantagesFinalCost, advantagesLvlLabel, advantagesLvlText, advantagesLvlComboBox, advantagesFinalCostText,
                currentPoints);

        FeatureEventHandler disadvantagesFeatureEventHandler = new FeatureEventHandler(user, disadvantagesData, disadvantagesView,
                disadvantagesAddonsTableView, disadvantagesBottomMenu, disadvantagesAdd, disadvantagesRemove, disadvantagesActivate,
                disadvantagesAddonName, disadvantagesAddonNameEn, disadvantagesAddonLevel, disadvantagesAddonCost,
                disadvantagesFull, disadvantagesFinalCost, disadvantagesLvlLabel, disadvantagesLvlText, disadvantagesLvlComboBox, disadvantagesFinalCostText,
                currentPoints);

        advantagesView.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, advantagesFeatureEventHandler);
            return row;
        });

        disadvantagesView.setRowFactory(tv -> {
            TableRow<Feature> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, disadvantagesFeatureEventHandler);
            return row;
        });

        setAdvantages();
        setDisadvantages();

        new LanguagesTable(languagesTableView, languagesNameColumn, languageSpokenColumn, languagesWrittenColumn,
                languagesCostColumn, languagesUserColumn, languagesDbColumn, languageNameText, languageSpokenChoiceBox,
                languageWrittenChoiceBox, languageCostText, languageAddButton, currentPoints);

        new CulturasTable(culturasTableView, culturasNameColumn, culturasCostColumn, culturasUserColumn, culturasDbColumn,
                culturaNameText, culturaCostText, culturaAddButton, currentPoints);
    }

    protected void buttonEvents() {
        userSheet.setOnAction(event -> {
            Stage childrenStage = new Stage();
            userSheet.setDisable(true);
            childrenStage.setOnCloseRequest(we -> userSheet.setDisable(false));
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/userSheet.fxml"));
            Parent childrenRoot;
            try {
                childrenRoot = loader.load();
                childrenStage.setScene(new Scene(childrenRoot, 700, 795));
                childrenStage.setTitle("GURPS Лист персонажа");
                childrenStage.show();
                childrenStage.setResizable(false);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected void SearchEvents(){
        advantagesSearchAll.setOnAction(event ->{
            String query = "advantage=true and UPPER(title) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(titleEn) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(cost) like UPPER('%" + advantagesSearchText.getText() + "%') or " +
                    "advantage=true and UPPER(description) like UPPER('%" + advantagesSearchText.getText() + "%')";
            advantagesView.setItems(new Feature().where(query));
        });

        for(String feature : new String[] {"Title", "TitleEn", "Cost", "Description"}){
            try {
                MenuItem menuItem = (MenuItem) ViewsAbstract.class.getDeclaredField("advantagesSearch" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage=true and UPPER("+ feature + ") like UPPER('%" + advantagesSearchText.getText() + "%')";
                    advantagesView.setItems(new Feature().where(query));
                });

                menuItem = (MenuItem) ViewsAbstract.class.getDeclaredField("disadvantagesSearch" + feature).get(this);
                menuItem.setOnAction(event ->{
                    String query = "advantage=false and "+ feature + " like '%" + disadvantagesSearchText.getText() + "%'";
                    disadvantagesView.setItems(new Feature().where(query));
                });
            } catch(IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkBoxEvents() {
        Integer[] numbers = {1, 2, 3, 4, 5};
        for(Integer number : numbers) {
            try {
                CheckBox checkBox = (CheckBox) ViewsAbstract.class.getDeclaredField("advantage" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=true and type like ";
                    if(newValue) advantagesNumbers.add(number);
                    else advantagesNumbers.remove(number);
                    for(Integer lNumber : advantagesNumbers) {
                        if(query.equals("advantage=true and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=true and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=true and type like ")) query = "advantage=true and type='6'";
                    advantagesView.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for(Integer number : numbers) {
            try {
                CheckBox checkBox = (CheckBox) ViewsAbstract.class.getDeclaredField("disadvantage" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "advantage=false and type like ";
                    if(newValue) disadvantagesNumbers.add(number);
                    else disadvantagesNumbers.remove(number);
                    for(Integer lNumber : disadvantagesNumbers) {
                        if(query.equals("advantage=false and type like ")) query += "'%" + lNumber + "%'";
                        else query += " or advantage=false and type like '%" + lNumber + "%'";
                    }
                    if(query.equals("advantage=false and type like ")) query = "advantage=false and type='6'";
                    disadvantagesView.setItems(new Feature().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAdvantages() {
        advantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        advantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        advantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        advantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        advantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        advantagesView.setPlaceholder(new Label("Преимуществ нет"));
        advantagesView.setItems(advantagesData);
    }

    private void setDisadvantages() {
        disadvantagesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        disadvantagesTitleEn.setCellValueFactory(new PropertyValueFactory<>("titleEn"));
        disadvantagesType.setCellValueFactory(new PropertyValueFactory<>("type"));
        disadvantagesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        disadvantagesDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        disadvantagesView.setPlaceholder(new Label("Недостатков нет"));
        disadvantagesView.setItems(disadvantagesData);
    }

    private void initData() {
        ObservableList<Feature> advantages = new Feature().where("advantage", true);
        ObservableList<Feature> disadvantages = new Feature().where("advantage", false);
        advantagesData.removeAll();
        advantagesData.addAll(advantages);

        disadvantagesData.removeAll();
        disadvantagesData.addAll(disadvantages);
    }

    private void currentPoints(Label cost, int oldStCost) {
        user.currentPoints = Integer.toString(Integer.parseInt(user.currentPoints) + Integer.parseInt(cost.getText()) - oldStCost);
        currentPoints.setText(user.currentPoints);
    }
}
