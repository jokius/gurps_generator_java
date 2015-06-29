package ru.gurps.generator.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Language;
import ru.gurps.generator.models.UserLanguage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguagesController extends AbstractController {
    public TableView<Language> tableView;
    public TableColumn<Language, String> nameColumn;
    public TableColumn<Language, String> spokenColumn;
    public TableColumn<Language, String> writtenColumn;
    public TableColumn<Language, String> costColumn;
    public TableColumn<Language, Boolean> userColumn;
    public TableColumn<Language, Boolean> dbColumn;

    public TextField nameText;
    public ChoiceBox<String> spokenChoiceBox;
    public ChoiceBox<String> writtenChoiceBox;
    public TextField costText;
    public Button addButton;

    public Button updateFromServer;
    public Button sedToServer;

    private Language language;

    @FXML
    private void initialize() {
        ObservableList<String> spokenValues = FXCollections.observableArrayList(
                Main.locale.getString("not_have"),
                Main.locale.getString("broken"),
                Main.locale.getString("accent"),
                Main.locale.getString("native"));
        ObservableList<String> writtenValues = FXCollections.observableArrayList(
                Main.locale.getString("illiteracy"),
                Main.locale.getString("semi-literate"),
                Main.locale.getString("literacy"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        spokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        spokenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(spokenValues));
        spokenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (!language.getSpoken().equals(event.getNewValue())) language.setSpoken(event.getNewValue());
        });

        writtenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        writtenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(writtenValues));
        writtenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (!language.getWritten().equals(event.getNewValue())) language.setWritten(event.getNewValue());
        });

        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if (event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (language.cost != Integer.parseInt(event.getNewValue()))
                language.cost = Integer.parseInt(event.getNewValue());
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        userColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        userColumn.setCellFactory(p -> new LanguagesUserButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new LanguagesDbButtonCell());

        setLanguages();
        tableView.setPlaceholder(new Label(Main.locale.getString("languages_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) return;
            addButton.setDisable(false);
        });

        spokenChoiceBox.setItems(spokenValues);
        spokenChoiceBox.getSelectionModel().selectFirst();
        writtenChoiceBox.setItems(writtenValues);
        writtenChoiceBox.getSelectionModel().selectFirst();
        addButton.setOnAction(event -> {
            String name = nameText.getText().substring(0, 1).toUpperCase() + nameText.getText().substring(1);
            language = (Language) new Language(name).create();
            language.spoken = spokenChoiceBox.getSelectionModel().getSelectedIndex();
            language.written = writtenChoiceBox.getSelectionModel().getSelectedIndex();
            language.cost = Integer.parseInt(costText.getText());
            language.add = true;

            new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
            if (language.cost != 0) setCurrentPoints(Integer.parseInt(user.currentPoints) + language.cost);
            setLanguages();
            nameText.setText("");
            addButton.setDisable(true);
            sedToServer.setDisable(false);
        });

        updateFromServer.setOnAction(event -> updateFromServer());
        sedToServer.setOnAction(event -> sedToServer(language.name));
    }

    private void updateFromServer() {
        String repose = getLanguages(1);
        if (repose.equals("")) return;
        JsonObject json = new JsonParser().parse(repose).getAsJsonObject();
        boolean next = newLanguages(json);
        HashMap<String, Object> pages = pages(json.getAsJsonObject("pagination"));

        if (next) {
            while (next && (Boolean) pages.get("next")) {
                json = new JsonParser().parse(getLanguages((int) pages.get("page") + 1)).getAsJsonObject();
                pages = pages(json.getAsJsonObject("pagination"));
                next = newLanguages(json);
            }
        }

        setLanguages();
    }

    private String getLanguages(int page) {
        try {
            HttpGet getRequest = new HttpGet("/api/languages?page=" + page);
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

    private void sedToServer(String name) {
        sedToServer.setDisable(true);

        try {
            HttpPost httpPost = new HttpPost("/api/languages");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("language", name));
            params.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse httpResponse = httpClient.execute(server, httpPost);
            HttpEntity entity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() == 204) return;
            BufferedReader br = new BufferedReader(new InputStreamReader((entity.getContent())));
            String response = br.readLine();
            JsonElement error = new JsonParser().parse(response).getAsJsonObject().get("error");
            System.out.println(error);
        } catch (HttpHostConnectException ignore) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLanguages() {
        ObservableList<Language> languages = FXCollections.observableArrayList();
        for (Object object : new Language().all()) {
            Language language = (Language) object;
            for (Language userLanguage : user.languages()) {
                if (language.id == userLanguage.id) {
                    language.written = userLanguage.written;
                    language.spoken = userLanguage.spoken;
                    language.cost = userLanguage.cost;
                    language.add = true;
                }
            }

            languages.add(language);
        }

        tableView.setItems(languages);
    }

    private boolean newLanguages(JsonObject json) {
        if (json.get("languages").getAsJsonArray().size() == 0) return false;
        for (JsonElement language : json.get("languages").getAsJsonArray()) {
            String name = language.getAsJsonObject().get("name").getAsString();
            if (((Language) new Language().find_by("name", name)).id != null) return false;
            new Language(name).create();
        }

        return true;
    }

    private class LanguagesUserButtonCell extends TableCell<Language, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        LanguagesUserButtonCell() {
            addButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
                language.add = true;
                setCurrentPoints(Integer.parseInt(user.currentPoints) + language.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage().find_by("languageId", language.id).delete();
                language.add = false;
                setCurrentPoints(Integer.parseInt(user.currentPoints) - language.cost);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (getGraphic() != null && t == null) setGraphic(null);
            if (empty) {
                setGraphic(null);
                return;
            }
            Language language = (Language) getTableRow().getItem();
            if (language == null) return;
            setGraphic(language.add ? removeButton : addButton);
        }
    }

    private class LanguagesDbButtonCell extends TableCell<Language, Boolean> {
        Button removeButton = new Button(Main.locale.getString("remove"));

        LanguagesDbButtonCell() {
            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage().delete_all(new UserLanguage().where("languageId", language.id));
                language.delete();
                setLanguages();
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (getGraphic() != null && t == null) setGraphic(null);
            if (empty) {
                setGraphic(null);
                return;
            }
            Language language = (Language) getTableRow().getItem();
            if (language == null) return;
            setGraphic(removeButton);
        }
    }
}
