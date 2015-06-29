package ru.gurps.generator.controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;
import ru.gurps.generator.models.User;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

public class AbstractController extends Main {
    public static User user;
    public static Label globalCost;
    public static Stage stage = new Stage();
    public static URI urlToLastVersion;
    public static HttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost server = new HttpHost("generator-gurps.rhcloud.com", 80, "http");
    //public static HttpHost server = new HttpHost("localhost", 3000, "http");

    protected void createMainStage(){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/main.fxml"));
        loader.setResources(Main.locale);
        stage.setMinWidth(670);
        stage.setMinHeight(560);
        stage.setResizable(true);
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
                            MenuItem searchDescription, MenuItem reset){

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) {
                searchButton.setDisable(true);
                tableView.setItems(model.all());
            }
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

        reset.setOnAction(event -> {
            searchText.setText("");
            searchButton.setDisable(true);
            tableView.setItems(model.all());
        });
    }

    public void localSearch(Model model, TableView<?> tableView,  TextField searchText, MenuButton searchButton,
                            MenuItem searchAll, MenuItem searchName, MenuItem searchNameEn,
                            MenuItem searchDescription, MenuItem reset){

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) searchButton.setDisable(true);
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

        reset.setOnAction(event -> {
            searchText.setText("");
            searchButton.setDisable(true);
            tableView.setItems(model.all());
        });
    }

    public HashMap<String, Object> pages(JsonObject pagination){
        HashMap<String, Object> pages = new HashMap<>();
        pages.put("page", pagination.get("current_page").getAsInt());
        pages.put("next", pagination.get("current_page").getAsInt() <= pagination.get("total_pages").getAsInt());
        return pages;
    }

    public HashMap<String, Object> pages(JsonObject pagination){
        HashMap<String, Object> pages = new HashMap<>();
        pages.put("page", pagination.get("current_page").getAsInt());
        pages.put("next", pagination.get("current_page").getAsInt() <= pagination.get("total_pages").getAsInt());
        return pages;
    }
}
