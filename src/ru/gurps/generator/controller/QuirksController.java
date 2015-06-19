package ru.gurps.generator.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Quirk;
import ru.gurps.generator.models.UserQuirk;

public class QuirksController extends AbstractController {
    public TableView<Quirk> tableView;
    public TableColumn<Quirk, String> nameColumn;
    public TableColumn<Quirk, String> costColumn;
    public TableColumn<Quirk, Boolean> userColumn;
    public TableColumn<Quirk, Boolean> dbColumn;

    public TextField nameText;
    public TextField costText;
    public Button addButton;

    private ObservableList<Quirk> quirks = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        for(Object object : new Quirk().all()){
            Quirk quirk = (Quirk) object;
            for(Quirk userQuirk : user.quirks()){
                if(quirk.id == userQuirk.id){
                    quirk.cost = userQuirk.cost;
                    quirk.add = true;
                }
            }

            quirks.add(quirk);
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if(event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Quirk quirk = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(quirk.cost != Integer.parseInt(event.getNewValue()))
                quirk.cost = Integer.parseInt(event.getNewValue());
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        userColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        userColumn.setCellFactory(p -> new QuirksUserButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new QuirksDbButtonCell());

        tableView.setItems(quirks);
        tableView.setPlaceholder(new Label(Main.locale.getString("quirks_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            addButton.setDisable(false);
        });

        addButton.setOnAction(event -> {
            Quirk quirk = (Quirk) new Quirk(nameText.getText()).create();
            quirk.cost = Integer.parseInt(costText.getText());
            quirk.add = true;

            new UserQuirk(user.id, quirk.id, quirk.cost).create();
            if(quirk.cost != 0) setCurrentPoints(Integer.parseInt(user.currentPoints) + quirk.cost);
            quirks.add(quirk);
            tableView.setItems(quirks);
            nameText.setText("");
            addButton.setDisable(true);
        });
    }

    private class QuirksUserButtonCell extends TableCell<Quirk, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        QuirksUserButtonCell() {
            addButton.setOnAction(t -> {
                Quirk quirk = (Quirk) getTableRow().getItem();
                new UserQuirk(user.id, quirk.id, quirk.cost).create();
                quirk.add = true;
                setCurrentPoints(Integer.parseInt(user.currentPoints) + quirk.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Quirk quirk = (Quirk) getTableRow().getItem();
                new UserQuirk().find_by("quirkId", quirk.id).delete();
                quirk.add = false;
                setCurrentPoints(Integer.parseInt(user.currentPoints) - quirk.cost);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) return;
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
                quirk.delete();
                new UserQuirk().delete_all(new UserQuirk().where("quirkId", quirk.id));
                quirks.remove(quirk);
                tableView.setItems(quirks);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) return;
            Quirk quirk = (Quirk) getTableRow().getItem();
            if(quirk == null) return;
            setGraphic(removeButton);
        }
    }
}
