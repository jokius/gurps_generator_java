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
import ru.gurps.generator.desktop.models.rules.Cultura;
import ru.gurps.generator.desktop.models.characters.CharactersCultura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CulturasController extends AbstractController {
    public TableView<Cultura> tableView;
    public TableColumn<Cultura, String> nameColumn;
    public TableColumn<Cultura, String> costColumn;
    public TableColumn<Cultura, Boolean> characterColumn;
    public TableColumn<Cultura, Boolean> dbColumn;

    public TextField nameText;
    public TextField costText;
    public Button addButton;

    public Button updateFromServer;
    public Button sedToServer;

    private Cultura cultura;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if(event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Cultura cultura = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(cultura.cost != Integer.parseInt(event.getNewValue()))
                cultura.cost = Integer.parseInt(event.getNewValue());
        });

        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        characterColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        characterColumn.setCellFactory(p -> new CulturasCharacterButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new CulturasDbButtonCell());

        setCulturas();
        tableView.setPlaceholder(new Label(Main.locale.getString("cultures_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            addButton.setDisable(false);
        });

        addButton.setOnAction(event -> {
            String name = nameText.getText().substring(0, 1).toUpperCase() + nameText.getText().substring(1);
            cultura = (Cultura) new Cultura(name).create();
            cultura.cost = Integer.parseInt(costText.getText());
            cultura.add = true;

            new CharactersCultura(character.id, cultura.id, cultura.cost).create();
            if(cultura.cost != 0) setCurrentPoints(Integer.parseInt(character.currentPoints) + cultura.cost);
            setCulturas();
            nameText.setText("");
            addButton.setDisable(true);
            sedToServer.setDisable(false);
        });

        updateFromServer.setOnAction(event -> updateFromServer());
        sedToServer.setOnAction(event -> sedToServer(cultura.name));
    }

    private void updateFromServer() {
        String repose = getPage("data/culturas", 1);
        if (repose.equals("")) return;
        JsonObject json = new JsonParser().parse(repose).getAsJsonObject();
        boolean next = newCulturas(json);
        HashMap<String, Object> pages = pages(json.getAsJsonObject("pagination"));

        if (next) {
            while (next && (Boolean) pages.get("next")) {
                json = new JsonParser().parse(getPage("culturas", (int) pages.get("page") + 1)).getAsJsonObject();
                pages = pages(json.getAsJsonObject("pagination"));
                next = newCulturas(json);
            }
        }

        setCulturas();
    }

    private boolean newCulturas(JsonObject json) {
        if (json.get("culturas").getAsJsonArray().size() == 0) return false;

        for (JsonElement cultura : json.get("culturas").getAsJsonArray()) {
            String name = cultura.getAsJsonObject().get("name").getAsString();
            if (((Cultura) new Cultura().find_by("name", name)).id != null) return false;
            new Cultura(name).create();
        }

        return true;
    }

    private void sedToServer(String name) {
        sedToServer.setDisable(true);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cultura", name));
        HttpResponse httpResponse = sendRequest("data/culturas", params);
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

    private void setCulturas() {
        ObservableList<Cultura> culturas = FXCollections.observableArrayList();
        for (Object object : new Cultura().all()) {
            Cultura cultura = (Cultura) object;
            for (Cultura characterCultura : character.cultures()) {
                if (cultura.id == characterCultura.id) {
                    cultura.cost = characterCultura.cost;
                    characterCultura.add = true;
                }
            }

            culturas.add(cultura);
        }

        tableView.setItems(culturas);
    }

    private class CulturasCharacterButtonCell extends TableCell<Cultura, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        CulturasCharacterButtonCell() {
            addButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new CharactersCultura(character.id, cultura.id, cultura.cost).create();
                cultura.add = true;
                setCurrentPoints(Integer.parseInt(character.currentPoints) + cultura.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new CharactersCultura().find_by("culturaId", cultura.id).delete();

                cultura.add = false;
                setCurrentPoints(Integer.parseInt(character.currentPoints) - cultura.cost);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty){
                setGraphic(null);
                return;
            }
            Cultura cultura = (Cultura) getTableRow().getItem();
            if(cultura == null) return;
            setGraphic(cultura.add ? removeButton : addButton);
        }
    }

    private class CulturasDbButtonCell extends TableCell<Cultura, Boolean> {
        Button removeButton = new Button(Main.locale.getString("remove"));

        CulturasDbButtonCell() {
            removeButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new CharactersCultura().delete_all(new CharactersCultura().where("culturaId", cultura.id));
                cultura.delete();
                setCulturas();
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty){
                setGraphic(null);
                return;
            }
            Cultura cultura = (Cultura) getTableRow().getItem();
            if(cultura == null) return;
            setGraphic(removeButton);
        }
    }
}
