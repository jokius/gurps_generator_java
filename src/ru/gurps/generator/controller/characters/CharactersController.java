package ru.gurps.generator.controller.characters;

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
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.models.Character;

import java.awt.*;
import java.io.IOException;

public class CharactersController extends AbstractController {
    public Button newUser;
    public Button load;
    public Button remove;
    public Button generate;
    public TextField newName;
    public TextField points;
    public TableView<Character> userTable;
    public TableColumn<Character, String> name;
    public TableColumn<Character, String> tableCurrentPoints;
    public TableColumn<Character, String> tableMaxPoints;
    public Hyperlink lastVersionLink;

    private ObservableList<Character> usersData = FXCollections.observableArrayList();
    private int index = -1;

    @FXML
    private void initialize() {
        usersData.addAll(new Character().all());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableCurrentPoints.setCellValueFactory(new PropertyValueFactory<>("currentPoints"));
        tableCurrentPoints.setCellFactory(column -> new TableCell<Character, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow().getItem() != null) {
                    setText(item);
                    Character character = (Character) getTableRow().getItem();
                    if(Integer.parseInt(character.currentPoints) < Integer.parseInt(character.maxPoints)) setTextFill(Color.GREEN);
                    else setTextFill(Color.RED);
                } else setText(null);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
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
            character = (Character) new Character(newName.getText(), points.getText()).create();
            stage.close();
            createMainStage();
        });
        
        load.setOnAction(event ->{
            stage.close();
            createMainStage();
        });
        
        remove.setOnAction(event ->{
            usersData.remove(index);
            character.delete();
            userTable.setItems(usersData);
            load.setDisable(true);
            remove.setDisable(true);
        });

        generate.setOnAction(event -> {
            stage.close();
            createGenerateStage();
        });

        userTable.setRowFactory(tv -> {
            TableRow<Character> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(row.isEmpty()) return;
                
                index = row.getIndex();
                character = usersData.get(index);
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
