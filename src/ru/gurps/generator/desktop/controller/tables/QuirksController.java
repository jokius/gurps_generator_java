package ru.gurps.generator.desktop.controller.tables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.characters.CharactersQuirk;
import ru.gurps.generator.desktop.models.rules.Quirk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuirksController extends AbstractController {
    public TableView<Quirk> tableView;
    public TableColumn<Quirk, String> nameColumn;
    public TableColumn<Quirk, String> costColumn;
    public TableColumn<Quirk, Boolean> characterColumn;
    public TableColumn<Quirk, Boolean> dbColumn;

    public TextField nameText;
    public TextField costText;
    public Button addButton;

    public Button updateFromServer;
    public Button sedToServer;

    private Quirk quirk;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if (event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Quirk quirk = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (quirk.cost != Integer.parseInt(event.getNewValue()))
                quirk.cost = Integer.parseInt(event.getNewValue());
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        characterColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        characterColumn.setCellFactory(p -> new QuirksUserButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new QuirksDbButtonCell());

        setQuirks();
        tableView.setPlaceholder(new Label(Main.locale.getString("quirks_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) return;
            addButton.setDisable(false);
        });

        addButton.setOnAction(event -> {
            String name = nameText.getText().substring(0, 1).toUpperCase() + nameText.getText().substring(1);
            quirk = (Quirk) new Quirk(name).create();
            quirk.cost = Integer.parseInt(costText.getText());
            quirk.add = true;

            new CharactersQuirk(character.id, quirk.id, quirk.cost).create();
            if (quirk.cost != 0) setCurrentPoints(Integer.parseInt(character.currentPoints) + quirk.cost);
            setQuirks();
            nameText.setText("");
            addButton.setDisable(true);
            sedToServer.setDisable(false);
        });

        updateFromServer.setOnAction(event -> updateFromServer());
        sedToServer.setOnAction(event -> sedToServer(quirk.name));
    }

    private void updateFromServer() {
        String repose = getPage("quirks", 1);
        if (repose.equals("")) return;
        JsonObject json = new JsonParser().parse(repose).getAsJsonObject();
        boolean next = newQuirks(json);
        HashMap<String, Object> pages = pages(json.getAsJsonObject("pagination"));

        if (next) {
            while (next && (Boolean) pages.get("next")) {
                json = new JsonParser().parse(getPage("quirks", (int) pages.get("page") + 1)).getAsJsonObject();
                pages = pages(json.getAsJsonObject("pagination"));
                next = newQuirks(json);
            }
        }

        setQuirks();
    }

    private boolean newQuirks(JsonObject json) {
        if (json.get("quirks").getAsJsonArray().size() == 0) return false;

        for (JsonElement quirk : json.get("quirks").getAsJsonArray()) {
            String name = quirk.getAsJsonObject().get("name").getAsString();
            if (((Quirk) new Quirk().find_by("name", name)).id != null) return false;
            new Quirk(name).create();
        }

        return true;
    }

    private void sedToServer(String name) {
        sedToServer.setDisable(true);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("quirk", name));
        HttpResponse httpResponse = sendRequest("quirks", params);
        if(httpResponse == null) return;
        if (httpResponse.getStatusLine().getStatusCode() == 204) return;

        try {
            HttpEntity entity = httpResponse.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader((entity.getContent())));
            String response = br.readLine();
            JsonElement error = new JsonParser().parse(response).getAsJsonObject().get("error");
            System.out.println(error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setQuirks() {
        ObservableList<Quirk> quirks = FXCollections.observableArrayList();
        for (Object object : new Quirk().all()) {
            Quirk quirk = (Quirk) object;
            for (Quirk characterQuirk : character.quirks()) {
                if (quirk.id == characterQuirk.id) {
                    quirk.cost = characterQuirk.cost;
                    characterQuirk.add = true;
                }
            }

            quirks.add(quirk);
        }

        tableView.setItems(quirks);
    }

    private class QuirksUserButtonCell extends TableCell<Quirk, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        QuirksUserButtonCell() {
            addButton.setOnAction(t -> {
                Quirk quirk = (Quirk) getTableRow().getItem();
                new CharactersQuirk(character.id, quirk.id, quirk.cost).create();
                quirk.add = true;
                setCurrentPoints(Integer.parseInt(character.currentPoints) + quirk.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Quirk quirk = (Quirk) getTableRow().getItem();
                new CharactersQuirk().find_by("quirkId", quirk.id).delete();
                quirk.add = false;
                setCurrentPoints(Integer.parseInt(character.currentPoints) - quirk.cost);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) {
                setGraphic(null);
                return;
            }
            Quirk quirk = (Quirk) getTableRow().getItem();
            if(quirk == null) return;
            setGraphic(quirk.add ? removeButton : addButton);
        }
    }

    private class QuirksDbButtonCell extends TableCell<Quirk, Boolean> {
        Button removeButton = new Button(Main.locale.getString("remove"));

        QuirksDbButtonCell() {
            removeButton.setOnAction(t -> {
                Quirk quirk = (Quirk) getTableRow().getItem();
                new CharactersQuirk().delete_all(new CharactersQuirk().where("quirkId", quirk.id));
                quirk.delete();
                setQuirks();
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) {
                setGraphic(null);
                return;
            }
            Quirk quirk = (Quirk) getTableRow().getItem();
            if(quirk == null) return;
            setGraphic(removeButton);
        }
    }
}
