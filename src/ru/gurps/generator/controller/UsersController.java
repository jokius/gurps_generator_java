package ru.gurps.generator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.gurps.generator.config.Db;
import ru.gurps.generator.lib.UserTableCell;
import ru.gurps.generator.pojo.User;

import javax.xml.soap.Text;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class UsersController extends MainController {
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
    private TableColumn name = new TableColumn("name");
    
    @FXML
    private TableColumn currentPoints = new TableColumn("currentPoints");
    
    @FXML
    private TableColumn maxPoints = new TableColumn("maxPoints");
    
    private int index = -1;

    public UsersController(Stage window) {
        this.window = window;
    }

    @FXML
    private void initialize() {
        usersData();

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        UserTableCell cell = new UserTableCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new UserEventHandler());
                        return cell;
                    }
                };

        name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        name.setCellFactory(cellFactory);

        currentPoints.setCellValueFactory(new PropertyValueFactory<User, String>("currentPoints"));
        currentPoints.setCellFactory(cellFactory);

        maxPoints.setCellValueFactory(new PropertyValueFactory<User, String>("maxPoints"));
        maxPoints.setCellFactory(cellFactory);
        
        userTable.setPlaceholder(new Label("Персонажей не создано"));
        userTable.setItems(usersData);

        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (newName.getText().equals("") || points.getText().equals("")) {
                    return;
                }

                newUser();
                window.close();
                createMainWindow();
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (index == -1) {
                    return;
                }

                window.close();
                createMainWindow();
            }
        });
        
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (index == -1) {
                    return;
                }

                usersData.remove(index);
                Db.delete("users", user.getId());
            }
        });
    }

    private void newUser() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", newName.getText());
        params.put("max_points", points.getText());

        ResultSet createdUser = Db.create("users", params);
        try {
            createdUser.next();
            user = pojoUser(createdUser);
            Preferences userPrefs = Preferences.userRoot().node("user");
            userPrefs.putInt("id", pojoUser(createdUser).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void usersData() {
        ResultSet users = Db.all("users");
        try {
            while (users.next()) {
                usersData.add(pojoUser(users));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User pojoUser(ResultSet user) throws SQLException {
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
    
    private void createMainWindow(){
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/views/main.fxml"));
        Parent childrenRoot;
        try {
            childrenRoot = (Parent) loader.load();
            childrenStage.setScene(new Scene(childrenRoot, 650, 600));
            childrenStage.setTitle("GURPSGenerator");
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class UserEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            TableCell c = (TableCell) event.getSource();
            index = c.getIndex();
            user = usersData.get(index);

            if (event.getClickCount() == 2) {
                window.close();
                createMainWindow();
            }
        }
    }
}
