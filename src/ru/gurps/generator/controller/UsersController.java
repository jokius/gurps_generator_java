package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.User;

import java.awt.*;
import java.io.IOException;

public class UsersController extends AbstractController {
    public Button newUser;
    public Button load;
    public Button remove;
    public Button generate;
    public TextField newName;
    public TextField points;
    public TableView<User> userTable;
    public TableColumn<User, String> name;
    public TableColumn<User, String> tableCurrentPoints;
    public TableColumn<User, String> tableMaxPoints;
    public Hyperlink lastVersionLink;

    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private int index = -1;

    @FXML
    private void initialize() {
        usersData.addAll(new User().all());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCurrentPoints.setCellValueFactory(new PropertyValueFactory<>("currentPoints"));
        tableCurrentPoints.setCellFactory(column -> new TableCell<User, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow().getItem() != null) {
                    setText(item);
                    User user = (User) getTableRow().getItem();
                    if(Integer.parseInt(user.currentPoints) < Integer.parseInt(user.maxPoints)) setTextFill(Color.GREEN);
                    else setTextFill(Color.RED);
                } else setText(null);
            }
        });
        tableMaxPoints.setCellValueFactory(new PropertyValueFactory<>("maxPoints"));

        userTable.setPlaceholder(new Label(Main.locale.getString("users_not_found")));
        userTable.setItems(usersData);

        events();
    }
    
    private void events(){
        newName.textProperty().addListener((observable, oldValue, newValue) -> {
            newUser.setDisable(newValue.equals("") || !newValue.matches("\\d+"));
        });

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            newUser.setDisable(newValue.equals("") || !newValue.matches("\\d+"));
        });
        
        newUser.setOnAction(event -> {
            user = (User) new User(newName.getText(), points.getText()).create();
            stage.close();
            createMainStage();
        });
        
        load.setOnAction(event ->{
            stage.close();
            createMainStage();
        });
        
        remove.setOnAction(event ->{
            usersData.remove(index);
            new User().delete(user.id);
            userTable.setItems(usersData);
            load.setDisable(true);
            remove.setDisable(true);
        });

        generate.setOnAction(event -> {
            stage.close();
            createGenerateStage();
        });

        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(row.isEmpty()) return;
                
                index = row.getIndex();
                user = usersData.get(index);
                if (event.getClickCount() == 1) {
                    load.setDisable(false);
                    remove.setDisable(false);
                }
                else {
                    stage.close();
                    createMainStage();
                }
            });
            return row;
        });

        if(urlToLastVersion != null){
            AnchorPane.setTopAnchor(userTable, 109.0);
            lastVersionLink.setVisible(true);
            lastVersionLink.setOnAction(event -> {
                try {
                    Desktop.getDesktop().browse(urlToLastVersion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
