package ru.gurps.generator.desktop.controller.tables;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.full.info.AlchemyController;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.characters.CharactersAlchemy;
import ru.gurps.generator.desktop.models.rules.Alchemy;

import java.io.IOException;
import java.util.HashMap;

public class AlchemiesController extends AbstractController {
    public TableView<Alchemy> tableView;
    public TableColumn<Alchemy, String> nameColumn;
    public TableColumn<Alchemy, String> nameEnColumn;
    public TableColumn<Alchemy, String> alternativeNamesColumn;
    public TableColumn<Alchemy, String> costColumn;
    public TableColumn<Alchemy, Boolean> actionsColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        alternativeNamesColumn.setCellValueFactory(new PropertyValueFactory<>("alternativeNames"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        actionsColumn.setCellFactory(p -> new ActionsCell());

        nameColumn.setCellFactory(column -> new TableCell<Alchemy, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getTableRow().getStyleClass().remove("isAdd");

                if (item != null || !empty) {
                    setText(item);
                    if(tableView.getItems().get(getTableRow().getIndex()).add)
                        getTableRow().getStyleClass().add("isAdd");
                    else getTableRow().getStyleClass().remove("isAdd");
                }

                if (empty) {
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        tableView.setRowFactory(tv -> {
            TableRow<Alchemy> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) openFullInfo(row.getItem());
            });

            return row ;
        });

        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) openFullInfo(tableView.getFocusModel().getFocusedItem());
        });

        localSearch(new Alchemy(), tableView, searchText, searchButton, searchAll, searchName, searchNameEn,
                searchDescription, reset);

        setAlchemies();
        tableView.setPlaceholder(new Label(Main.locale.getString("alchemies_not_found")));
    }

    private void setAlchemies() {
        ObservableList<Alchemy> alchemies = new Alchemy().all();
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", character.id);

        for (Alchemy alchemy : alchemies) {
            params.put("alchemyId", alchemy.id);
            CharactersAlchemy charactersAlchemy = (CharactersAlchemy) new CharactersAlchemy().find_by(params);
            alchemy.add = charactersAlchemy.id != null;
        }

        tableView.setItems(alchemies);
    }

    private void openFullInfo(Alchemy alchemy){
        Stage childrenStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/alchemy.fxml"));
        AlchemyController controller = new AlchemyController(alchemy);
        loader.setController(controller);
        loader.setResources(Main.locale);
        Parent childrenRoot;
        try {
            childrenRoot = loader.load();
            childrenStage.setResizable(false);
            childrenStage.setScene(new Scene(childrenRoot, 635, 572));
            childrenStage.setTitle("GURPSGenerator - " + alchemy.name + " (" + alchemy.nameEn + ")");
            childrenStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ActionsCell extends TableCell<Alchemy, Boolean> {
        final HBox hBox = new HBox();
        final StackPane paddedButton = new StackPane();
        final Button addButton = new Button(Main.locale.getString("add"));
        final Button removeButton = new Button(Main.locale.getString("remove"));
        final Button fullButton = new Button(Main.locale.getString("full"));
        private Alchemy alchemy;

        public ActionsCell() {
            setHBox();
        }

        private void setButtons() {
            addButton.setMinWidth(84);
            removeButton.setMinWidth(84);

            alchemy.addButton = addButton;
            alchemy.removeButton = removeButton;
            alchemy.row = getTableRow();

            if (alchemy.add) {
                addButton.setVisible(false);
                removeButton.setVisible(true);
            } else {
                addButton.setVisible(true);
                removeButton.setVisible(false);
            }

            addButton.setOnAction(event -> {
                new CharactersAlchemy(character.id, alchemy.id).create();
                alchemy.setAddAndColorRow(true);
            });

            removeButton.setOnAction(event -> {
                HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", character.id);
                params.put("alchemyId", alchemy.id);
                CharactersAlchemy charactersAlchemy = (CharactersAlchemy) new CharactersAlchemy().find_by(params);
                charactersAlchemy.delete();
                alchemy.setAddAndColorRow(false);
            });

            fullButton.setOnAction(actionEvent -> openFullInfo(alchemy));
        }

        private void setHBox() {
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            paddedButton.getChildren().addAll(addButton, removeButton);
            hBox.getChildren().addAll(paddedButton, fullButton);
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);

            if (empty) setGraphic(null);
            else {
                alchemy = (Alchemy) getTableRow().getItem();
                if(alchemy == null) {
                    setGraphic(null);
                    return;
                }

                setButtons();
                setGraphic(hBox);
            }
        }
    }
}
