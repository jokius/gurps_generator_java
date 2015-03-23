package ru.gurps.generator.lib;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.models.*;


public class CulturasTable {
    private User user = UsersController.user;
    private TableView<Cultura> culturasTableView;
    private TableColumn<Cultura, String> culturasNameColumn;
    private TableColumn<Cultura, String> culturasCostColumn;
    private TableColumn<Cultura, Boolean> culturasUserColumn;
    private TableColumn<Cultura, Boolean> culturasDbColumn;

    private TextField culturaNameText;
    private TextField culturaCostText;
    private Button culturaAddButton;

    private Label currentPoints;
    private ObservableList<Cultura> culturas = FXCollections.observableArrayList();

    public CulturasTable(TableView<Cultura> culturasTableView, TableColumn<Cultura, String> culturasNameColumn,
                         TableColumn<Cultura, String> culturasCostColumn, TableColumn<Cultura, Boolean> culturasUserColumn,
                         TableColumn<Cultura, Boolean> culturasDbColumn, TextField culturaNameText, TextField culturaCostText,
                         Button culturaAddButton, Label currentPoints) {
        this.culturasTableView = culturasTableView;
        this.culturasNameColumn = culturasNameColumn;
        this.culturasCostColumn = culturasCostColumn;
        this.culturasUserColumn = culturasUserColumn;
        this.culturasDbColumn = culturasDbColumn;
        this.culturaNameText = culturaNameText;
        this.culturaCostText = culturaCostText;
        this.culturaAddButton = culturaAddButton;
        this.currentPoints = currentPoints;
        run();
    }

    private void run() {
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

        culturasNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        culturasCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        culturasCostColumn.setOnEditCommit(event -> {
                    if(event.getNewValue().equals("0") || "\\D".matches(event.getNewValue())) return;
            Cultura cultura = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(cultura.cost != Integer.parseInt(event.getNewValue()))
                cultura.cost = Integer.parseInt(event.getNewValue());
        });

        culturasCostColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        culturasUserColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        culturasUserColumn.setCellFactory(p -> new CulturasUserButtonCell());

        culturasDbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        culturasDbColumn.setCellFactory(p -> new CulturasDbButtonCell());

        culturasTableView.setItems(culturas);
        culturasTableView.setPlaceholder(new Label("Культур нет"));
        culturasTableView.setEditable(true);

        culturaNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            culturaAddButton.setDisable(false);
        });

        culturaAddButton.setOnAction(event -> {
            Cultura cultura = (Cultura) new Cultura(culturaNameText.getText()).create();
            cultura.cost = Integer.parseInt(culturaCostText.getText());
            cultura.add = true;

            new UserCultura(user.id, cultura.id, cultura.cost).create();
            if(cultura.cost != 0){
                String points = Integer.toString(Integer.parseInt(user.currentPoints) + cultura.cost);
                currentPoints.setText(points);
                user.update_single("currentPoints", points);
            }
            culturas.add(cultura);
            culturasTableView.setItems(culturas);
            culturaNameText.setText("");
            culturaAddButton.setDisable(true);
        });
    }

    private class CulturasUserButtonCell extends TableCell<Cultura, Boolean> {
        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");

        CulturasUserButtonCell() {
            addButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new UserCultura(user.id, cultura.id, cultura.cost).create();
                cultura.add = true;
                String points = Integer.toString(Integer.parseInt(user.currentPoints) + cultura.cost);
                currentPoints.setText(points);
                user.update_single("currentPoints", points);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                new UserLanguage().find_by("culturaId", cultura.id).delete();

                cultura.add = false;
                String points = Integer.toString(Integer.parseInt(user.currentPoints) - cultura.cost);
                currentPoints.setText(points);
                user.update_single("currentPoints", points);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) return;
            Cultura cultura = (Cultura) getTableRow().getItem();
            if(cultura == null) return;
            setGraphic(cultura.add ? removeButton : addButton);
        }
    }

    private class CulturasDbButtonCell extends TableCell<Cultura, Boolean> {
        Button removeButton = new Button("Удалить");

        CulturasDbButtonCell() {
            removeButton.setOnAction(t -> {
                Cultura cultura = (Cultura) getTableRow().getItem();
                cultura.delete();
                new UserCultura().delete_all(new UserCultura().where("culturaId", cultura.id));
                culturas.remove(cultura);
                culturasTableView.setItems(culturas);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) return;
            Cultura cultura = (Cultura) getTableRow().getItem();
            if(cultura == null) return;
            setGraphic(removeButton);
        }
    }
}
