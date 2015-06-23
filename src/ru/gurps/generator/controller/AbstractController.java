package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;
import ru.gurps.generator.models.User;

import java.io.IOException;
import java.net.URI;

public class AbstractController extends Main {
    public static User user;
    public static Label globalCost;
    public static Stage stage = new Stage();
    public static URI urlToLastVersion;

    protected void createMainStage(){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/main.fxml"));
        loader.setResources(Main.locale);
        stage.setMinWidth(670);
        stage.setMinHeight(560);
        try {
            Parent childrenRoot = loader.load();
            stage.setScene(new Scene(childrenRoot, 670, 540));
            stage.setTitle("GURPSGenerator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createGenerateStage(){
        stage.setResizable(false);
        stage.setMinWidth(516);
        stage.setMinHeight(466);
        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/generateUser.fxml"));
        view.setResources(Main.locale);
        try {
            Parent childrenRoot = view.load();
            stage.setScene(new Scene(childrenRoot, 516, 466));
            stage.setTitle("GURPSGenerator");
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPoints(int points){
        String sPoints = Integer.toString(points);
        globalCost.setText(sPoints);
        user.update_single("currentPoints", sPoints);
        if(Integer.parseInt(user.maxPoints) >= points) globalCost.setTextFill(Color.GREEN);
        else globalCost.setTextFill(Color.RED);
    }

    public int globalCost(){
        return Integer.parseInt(globalCost.getText());
    }


    public void localSearch(Model model, TableView<?> tableView, TextField searchText, MenuButton searchButton,
                            MenuItem searchAll, MenuItem searchName, MenuItem searchNameEn, MenuItem searchCost,
                            MenuItem searchDescription){

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) searchButton.setDisable(true);
            else searchButton.setDisable(false);
        });

        searchAll.setOnAction(event -> {
            String query = "UPPER(name) like UPPER('%" + searchText.getText() + "%') or " +
                    "UPPER(nameEn) like UPPER('%" + searchText.getText() + "%') or " +
                    "UPPER(cost) like UPPER('%" + searchText.getText() + "%') or " +
                    "UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchName.setOnAction(event -> {
            String query = "UPPER(name) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchNameEn.setOnAction(event -> {
            String query = "UPPER(nameEn) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchCost.setOnAction(event -> {
            String query = "UPPER(cost) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchDescription.setOnAction(event -> {
            String query = "UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });
    }

    public void localSearch(Model model, TableView<?> tableView, TextField searchText, MenuButton searchButton,
                            MenuItem searchAll, MenuItem searchName, MenuItem searchNameEn,
                            MenuItem searchDescription){

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) searchButton.setDisable(true);
            else searchButton.setDisable(false);
        });

        searchAll.setOnAction(event -> {
            String query = "UPPER(name) like UPPER('%" + searchText.getText() + "%') or " +
                    "UPPER(nameEn) like UPPER('%" + searchText.getText() + "%') or " +
                    "UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchName.setOnAction(event -> {
            String query = "UPPER(name) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchNameEn.setOnAction(event -> {
            String query = "UPPER(nameEn) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });

        searchDescription.setOnAction(event -> {
            String query = "UPPER(description) like UPPER('%" + searchText.getText() + "%')";
            tableView.setItems(model.where(query));
        });
    }
}
