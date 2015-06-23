package ru.gurps.generator.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Cultura;
import ru.gurps.generator.models.UserCultura;

public class CulturasController extends AbstractController {
    public TableView<Cultura> tableView;
    public TableColumn<Cultura, String> nameColumn;
    public TableColumn<Cultura, String> costColumn;
    public TableColumn<Cultura, Boolean> userColumn;
    public TableColumn<Cultura, Boolean> dbColumn;

    public TextField nameText;
    public TextField costText;
    public Button addButton;
    private ObservableList<Cultura> culturas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        for(Object object : new Cultura().all()){
            Cultura cultura = (Cultura) object;
            for(Cultura userCultura : user.cultures()){
                if(cultura.id == userCultura.id){
                    cultura.cost = userCultura.cost;
                    cultura.add = true;
                }
            }

            culturas.add(cultura);
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if(event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Cultura cultura = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(cultura.cost != Integer.parseInt(event.getNewValue()))
                cultura.cost = Integer.parseInt(event.getNewValue());
        });

        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        userColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        userColumn.setCellFactory(p -> new CulturasUserButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new CulturasDbButtonCell());

        tableView.setItems(culturas);
        tableView.setPlaceholder(new Label(Main.locale.getString("cultures_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            addButton.setDisable(false);
        });

        addButton.setOnAction(event -> {
            Cultura cultura = (Cultura) new Cultura(nameText.getText()).create();
            cultura.cost = Integer.parseInt(costText.getText());
            cultura.add = true;

            new UserCultura(user.id, cultura.id, cultura.cost).create();
            if(cultura.cost != 0) setCurrentPoints(Integer.parseInt(user.currentPoints) + cultura.cost);
            culturas.add(cultura);
            tableView.setItems(culturas);
            nameText.setText("");
            addButton.setDisable(true);
        });
    }

    private class CulturasUserButtonCell extends TableCell<Cultura, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        CulturasUserButtonCell() {
            addButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new UserCultura(user.id, cultura.id, cultura.cost).create();
                cultura.add = true;
                setCurrentPoints(Integer.parseInt(user.currentPoints) + cultura.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new UserCultura().find_by("culturaId", cultura.id).delete();

                cultura.add = false;
                setCurrentPoints(Integer.parseInt(user.currentPoints) - cultura.cost);
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
                cultura.delete();
                new UserCultura().delete_all(new UserCultura().where("culturaId", cultura.id));
                culturas.remove(cultura);
                tableView.setItems(culturas);
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
