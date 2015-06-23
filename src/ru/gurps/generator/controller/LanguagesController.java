package ru.gurps.generator.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.gurps.generator.Main;
import ru.gurps.generator.models.Language;
import ru.gurps.generator.models.UserLanguage;

public class LanguagesController extends AbstractController {
    public TableView<Language> tableView;
    public TableColumn<Language, String> nameColumn;
    public TableColumn<Language, String> spokenColumn;
    public TableColumn<Language, String> writtenColumn;
    public TableColumn<Language, String> costColumn;
    public TableColumn<Language, Boolean> userColumn;
    public TableColumn<Language, Boolean> dbColumn;

    public TextField nameText;
    public ChoiceBox<String> spokenChoiceBox;
    public ChoiceBox<String> writtenChoiceBox;
    public TextField costText;
    public Button addButton;

    private ObservableList<Language> languages = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        for(Object object : new Language().all()){
            Language language = (Language) object;
            for(Language userLanguage : user.languages()){
                if(language.id == userLanguage.id){
                    language.written = userLanguage.written;
                    language.spoken = userLanguage.spoken;
                    language.cost = userLanguage.cost;
                    language.add = true;
                }
            }

            languages.add(language);
        }

        ObservableList<String> spokenValues = FXCollections.observableArrayList(
                Main.locale.getString("not_have"),
                Main.locale.getString("broken"),
                Main.locale.getString("accent"),
                Main.locale.getString("native"));
        ObservableList<String> writtenValues = FXCollections.observableArrayList(
                Main.locale.getString("illiteracy"),
                Main.locale.getString("semi-literate"),
                Main.locale.getString("literacy"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        spokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        spokenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(spokenValues));
        spokenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!language.getSpoken().equals(event.getNewValue())) language.setSpoken(event.getNewValue());
        });

        writtenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        writtenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(writtenValues));
        writtenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!language.getWritten().equals(event.getNewValue())) language.setWritten(event.getNewValue());
        });

        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costColumn.setOnEditCommit(event -> {
            if(event.getNewValue().equals("0") || !event.getNewValue().matches("\\d+")) return;
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(language.cost != Integer.parseInt(event.getNewValue()))
                language.cost = Integer.parseInt(event.getNewValue());
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        userColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        userColumn.setCellFactory(p -> new LanguagesUserButtonCell());

        dbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        dbColumn.setCellFactory(p -> new LanguagesDbButtonCell());

        tableView.setItems(languages);
        tableView.setPlaceholder(new Label(Main.locale.getString("languages_not_found")));
        tableView.setEditable(true);

        nameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            addButton.setDisable(false);
        });

        spokenChoiceBox.setItems(spokenValues);
        spokenChoiceBox.getSelectionModel().selectFirst();
        writtenChoiceBox.setItems(writtenValues);
        writtenChoiceBox.getSelectionModel().selectFirst();
        addButton.setOnAction(event -> {
            Language language = (Language) new Language(nameText.getText()).create();
            language.spoken = spokenChoiceBox.getSelectionModel().getSelectedIndex();
            language.written = writtenChoiceBox.getSelectionModel().getSelectedIndex();
            language.cost = Integer.parseInt(costText.getText());
            language.add = true;

            new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
            if(language.cost != 0) setCurrentPoints(Integer.parseInt(user.currentPoints) + language.cost);
            languages.add(language);
            tableView.setItems(languages);
            nameText.setText("");
            addButton.setDisable(true);
        });
    }

    private class LanguagesUserButtonCell extends TableCell<Language, Boolean> {
        Button addButton = new Button(Main.locale.getString("add"));
        Button removeButton = new Button(Main.locale.getString("remove"));

        LanguagesUserButtonCell() {
            addButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
                language.add = true;
                setCurrentPoints(Integer.parseInt(user.currentPoints) + language.cost);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage().find_by("languageId", language.id).delete();
                language.add = false;
                setCurrentPoints(Integer.parseInt(user.currentPoints) - language.cost);
                setGraphic(addButton);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) {
                setGraphic(null);
                return;
            }
            Language language = (Language) getTableRow().getItem();
            if(language == null) return;
            setGraphic(language.add ? removeButton : addButton);
        }
    }

    private class LanguagesDbButtonCell extends TableCell<Language, Boolean> {
        Button removeButton = new Button(Main.locale.getString("remove"));

        LanguagesDbButtonCell() {
            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                language.delete();
                new UserLanguage().delete_all(new UserLanguage().where("languageId", language.id));
                languages.remove(language);
                tableView.setItems(languages);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) {
                setGraphic(null);
                return;
            }
            Language language = (Language) getTableRow().getItem();
            if(language == null) return;
            setGraphic(removeButton);
        }
    }
}
