package ru.gurps.generator.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class UsersController extends AbstractController {
    private Stage window;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    @FXML
    private Button newUser;

    @FXML
    private Button load;

    @FXML
    private Button remove;

    @FXML
    private TextField newName;

    @FXML
    private TextField points;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> name = new TableColumn<>("name");

    @FXML
    private TableColumn<User, String> tableCurrentPoints = new TableColumn<>("currentPoints");

    @FXML
    private TableColumn<User, String> tableMaxPoints = new TableColumn<>("maxPoints");

    private int index = -1;

    public UsersController(Stage window) {
        this.window = window;
    }

    @FXML
    private void initialize() {
        ResultSet users = new User().all();
        try {
            while (users.next()) {
                usersData.add(pojoUser(users));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCurrentPoints.setCellValueFactory(new PropertyValueFactory<>("currentPoints"));
        tableMaxPoints.setCellValueFactory(new PropertyValueFactory<>("maxPoints"));

        userTable.setPlaceholder(new Label("Персонажей не создано"));
        userTable.setItems(usersData);

        events();
    }
    
    User pojoUser(ResultSet user) throws SQLException {
        return new User(
                user.getInt("id"),
                user.getString("name"),
                user.getString("current_points"),
                user.getString("max_points"),
                user.getInt("st"),
                user.getInt("dx"),
                user.getInt("iq"),
                user.getInt("ht"),
                user.getInt("hp"),
                user.getInt("will"),
                user.getInt("per"),
                user.getInt("fp"),
                user.getDouble("bs"),
                user.getInt("move"),
                user.getInt("sm"),
                user.getBoolean("no_fine_manipulators")
        );
    }
    
    private void events(){
        newName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                newUser.setDisable(newValue.equals("") || points.getText().equals(""));

            }
        });

        points.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                newUser.setDisable(newValue.equals("") || newName.getText().equals(""));
            }
        });
        
        newUser.setOnAction(event ->{
            HashMap<String, String> params = new HashMap<>();
            params.put("name", newName.getText());
            params.put("max_points", points.getText());
            ResultSet createdUser = new User().create(params);

            try {
                createdUser.next();
                user = pojoUser(createdUser);
                Preferences userPrefs = Preferences.userRoot().node("user");
                userPrefs.putInt("id", pojoUser(createdUser).getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            window.close();
            createMainWindow();
        });
        
        load.setOnAction(event ->{
            window.close();
            createMainWindow();
        });
        
        remove.setOnAction(event ->{
            usersData.remove(index);
            new User().delete(user.getId());

            if (usersData.size() == 0){
                load.setDisable(true);
                remove.setDisable(true);
            }
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
                    createMainWindow();
                }
            });
            return row;
        });
    }

    private void createMainWindow(){
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/main.fxml"));
        Parent childrenRoot;
        try {
            childrenRoot = loader.load();
            childrenStage.setScene(new Scene(childrenRoot, 650, 500));
            childrenStage.setTitle("GURPSGenerator");
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
