package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.gurps.generator.models.User;

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

    private Stage window;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private int index = -1;

    public UsersController(Stage window) {
        this.window = window;
    }

    @FXML
    private void initialize() {
        usersData.addAll(new User().all());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCurrentPoints.setCellValueFactory(new PropertyValueFactory<>("currentPoints"));
        tableMaxPoints.setCellValueFactory(new PropertyValueFactory<>("maxPoints"));

        userTable.setPlaceholder(new Label("Персонажей не создано"));
        userTable.setItems(usersData);

        events();
    }
    
    private void events(){
        newName.textProperty().addListener((observable, oldValue, newValue) -> {
            newUser.setDisable(newValue.equals("") || points.getText().equals(""));
        });

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            newUser.setDisable(newValue.equals("") || newName.getText().equals(""));
        });
        
        newUser.setOnAction(event -> {
            user = (User) new User(newName.getText(), points.getText()).create();
            window.close();
            createMainStage();
        });
        
        load.setOnAction(event ->{
            window.close();
            createMainStage();
        });
        
        remove.setOnAction(event ->{
            usersData.remove(index);
            new User().delete(user.id);

            if (usersData.size() == 0){
                load.setDisable(true);
                remove.setDisable(true);
            }
        });

        generate.setOnAction(event -> {
            window.close();
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
                    window.close();
                    createMainStage();
                }
            });
            return row;
        });
    }
}
