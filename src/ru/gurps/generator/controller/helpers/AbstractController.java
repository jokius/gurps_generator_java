package ru.gurps.generator.controller.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;
import ru.gurps.generator.models.Character;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class AbstractController extends Main {
    public static Character character;
    public static Label globalCost;
    public static Stage stage = new Stage();
    public static URI urlToLastVersion;
    public static HttpClient httpClient = HttpClientBuilder.create().build();
    public static HttpHost server = new HttpHost(serverAddress, serverPort, protocol);

    protected void createParentStage() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/parent.fxml"));
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

    protected void createGenerateStage() {
        stage.setResizable(false);
        stage.setMinWidth(516);
        stage.setMinHeight(466);
        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/characters/generate.fxml"));
        view.setResources(Main.locale);
        try {
            Parent childrenRoot = view.load();
            stage.setScene(new Scene(childrenRoot, 516, 466));
            stage.setTitle("GURPSGenerator");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void createResourcesUpdateStage() {
        String repose = getRequest("change_log/status?start=" + updateStart);
        if (repose.equals("")) return;
        if(new JsonParser().parse(repose).getAsJsonObject().get("present").getAsBoolean()) updateHave();
        else updateNotHave();
    }

    public void setCurrentPoints(int points) {
        String sPoints = Integer.toString(points);
        globalCost.setText(sPoints);
        character.update_single("currentPoints", sPoints);
        if (Integer.parseInt(character.maxPoints) >= points) globalCost.setTextFill(Color.GREEN);
        else globalCost.setTextFill(Color.RED);
    }

    public int globalCost() {
        return Integer.parseInt(globalCost.getText());
    }


    public void localSearch(Model model, TableView<?> tableView, TextField searchText, MenuButton searchButton,
                            MenuItem searchAll, MenuItem searchName, MenuItem searchNameEn, MenuItem searchCost,
                            MenuItem searchDescription, MenuItem reset) {

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                searchButton.setDisable(true);
                tableView.setItems(model.all());
            } else searchButton.setDisable(false);
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

    public void localSearch(Model model, TableView<?> tableView, TextField searchText, MenuButton searchButton,
                            MenuItem searchAll, MenuItem searchName, MenuItem searchNameEn,
                            MenuItem searchDescription, MenuItem reset) {

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

    public HashMap<String, Object> pages(JsonObject pagination) {
        HashMap<String, Object> pages = new HashMap<>();
        pages.put("page", pagination.get("current_page").getAsInt());
        pages.put("next", pagination.get("current_page").getAsInt() <= pagination.get("total_pages").getAsInt());
        return pages;
    }


    public String getPage(String what, int page) {
        try {
            HttpGet getRequest = new HttpGet("/api/" + what + "?page=" + page);
            HttpResponse httpResponse = httpClient.execute(server, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) return "";

            BufferedReader br = new BufferedReader(new InputStreamReader((entity.getContent())));
            return br.readLine();

        } catch (HttpHostConnectException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getRequest(String what) {
        try {
            HttpGet getRequest = new HttpGet("/api/" + what);
            HttpResponse httpResponse = httpClient.execute(server, getRequest);
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) return "";

            BufferedReader br = new BufferedReader(new InputStreamReader((entity.getContent())));
            return br.readLine();

        } catch (HttpHostConnectException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public HttpResponse sendRequest(String what, List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost("/api/" + what);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return httpClient.execute(server, httpPost);
        } catch (HttpHostConnectException ignore) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateHave(){
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/update/have.fxml"));
        loader.setResources(Main.locale);
        Parent childrenRoot;
        try {
            childrenRoot = loader.load();
            childrenStage.setResizable(false);
            childrenStage.setScene(new Scene(childrenRoot, 600, 400));
            childrenStage.setTitle(Main.locale.getString("check_updates"));
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateNotHave() {
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/update/notHave.fxml"));
        loader.setResources(Main.locale);
        Parent childrenRoot;
        try {
            childrenRoot = loader.load();
            childrenStage.setScene(new Scene(childrenRoot, 255, 98));
            childrenStage.setResizable(false);
            childrenStage.setTitle(Main.locale.getString("check_updates"));
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
