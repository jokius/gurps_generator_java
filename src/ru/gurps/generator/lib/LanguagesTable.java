package ru.gurps.generator.lib;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.models.*;


public class LanguagesTable {
    private User user = UsersController.user;
    private TableView<Language> languagesTableView;
    private TableColumn<Language, String> languagesNameColumn;
    private TableColumn<Language, String> languageSpokenColumn;
    private TableColumn<Language, String> languagesWrittenColumn;
    private TableColumn<Language, String> languagesCostColumn;
    private TableColumn<Language, Boolean> languagesUserColumn;
    private TableColumn<Language, Boolean> languagesDbColumn;

    private TextField languageNameText;
    private ChoiceBox languageSpokenChoiceBox;
    private ChoiceBox languageWrittenChoiceBox;
    private TextField languageCostText;
    private Button languageAddButton;

    private Label currentPoints;

    ObservableList<Language> languages = FXCollections.observableArrayList();

    public LanguagesTable(TableView<Language> languagesTableView, TableColumn<Language, String> languagesNameColumn,
                          TableColumn<Language, String> languageSpokenColumn, TableColumn<Language, String> languagesWrittenColumn,
                          TableColumn<Language, String> languagesCostColumn, TableColumn<Language, Boolean> languagesUserColumn,
                          TableColumn<Language, Boolean> languagesDbColumn, TextField languageNameText, ChoiceBox languageSpokenChoiceBox,
                          ChoiceBox languageWrittenChoiceBox, TextField languageCostText, Button languageAddButton, Label currentPoints) {
        this.languagesTableView = languagesTableView;
        this.languagesNameColumn = languagesNameColumn;
        this.languageSpokenColumn = languageSpokenColumn;
        this.languagesWrittenColumn = languagesWrittenColumn;
        this.languagesCostColumn = languagesCostColumn;
        this.languagesUserColumn = languagesUserColumn;
        this.languagesDbColumn = languagesDbColumn;
        this.languageNameText = languageNameText;
        this.languageSpokenChoiceBox = languageSpokenChoiceBox;
        this.languageWrittenChoiceBox = languageWrittenChoiceBox;
        this.languageCostText = languageCostText;
        this.languageAddButton = languageAddButton;
        this.currentPoints = currentPoints;
        run();
    }

    private void run() {
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
        ObservableList<String> spokenValues = FXCollections.observableArrayList("Отсутствует", "Ломаный", "Акцент", "Родной");
        ObservableList<String> writtenValues = FXCollections.observableArrayList("Неграмотность", "Полу-грамотность", "Грамотность");

        languagesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        languageSpokenColumn.setCellValueFactory(new PropertyValueFactory<>("spoken"));
        languageSpokenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(spokenValues));
        languageSpokenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!language.getSpoken().equals(event.getNewValue())) language.setSpoken(event.getNewValue());
        });

        languagesWrittenColumn.setCellValueFactory(new PropertyValueFactory<>("written"));
        languagesWrittenColumn.setCellFactory(ComboBoxTableCell.forTableColumn(writtenValues));
        languagesWrittenColumn.setOnEditCommit(event -> {
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(!language.getWritten().equals(event.getNewValue())) language.setWritten(event.getNewValue());
        });

        languagesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
                languagesCostColumn.setOnEditCommit(event -> {
                    if(event.getNewValue().equals("0") || "\\D".matches(event.getNewValue())) return;
            Language language = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(language.cost != Integer.parseInt(event.getNewValue()))
                language.cost = Integer.parseInt(event.getNewValue());
        });
        languagesCostColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        languagesUserColumn.setCellValueFactory(new PropertyValueFactory<>("add"));
        languagesUserColumn.setCellFactory(p -> new LanguagesUserButtonCell());

        languagesDbColumn.setCellValueFactory(p -> new SimpleBooleanProperty(true));
        languagesDbColumn.setCellFactory(p -> new LanguagesDbButtonCell());

        languagesTableView.setItems(languages);
        languagesTableView.setPlaceholder(new Label("Языков нет"));
        languagesTableView.setEditable(true);

        languageNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) return;
            languageAddButton.setDisable(false);
        });

        languageSpokenChoiceBox.setItems(spokenValues);
        languageSpokenChoiceBox.getSelectionModel().selectFirst();
        languageWrittenChoiceBox.setItems(writtenValues);
        languageWrittenChoiceBox.getSelectionModel().selectFirst();
        languageAddButton.setOnAction(event -> {
            Language language = (Language) new Language(languageNameText.getText()).create();
            language.spoken = languageSpokenChoiceBox.getSelectionModel().getSelectedIndex();
            language.written = languageWrittenChoiceBox.getSelectionModel().getSelectedIndex();
            language.cost = Integer.parseInt(languageCostText.getText());
            language.add = true;

            new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
            if(language.cost != 0){
                String points = Integer.toString(Integer.parseInt(user.currentPoints) + language.cost);
                currentPoints.setText(points);
                user.update_single("currentPoints", points);
            }
            languages.add(language);
            languagesTableView.setItems(languages);
        });
    }

    private class LanguagesUserButtonCell extends TableCell<Language, Boolean> {
        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");

        LanguagesUserButtonCell() {
            addButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage(user.id, language.id, language.spoken, language.written, language.cost).create();
                language.add = true;
                String points = Integer.toString(Integer.parseInt(user.currentPoints) + language.cost);
                currentPoints.setText(points);
                user.update_single("currentPoints", points);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage().find_by("languageId", language.id).delete();

                language.add = false;
                String points = Integer.toString(Integer.parseInt(user.currentPoints) - language.cost);
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
            Language language = (Language) getTableRow().getItem();
            if(language == null) return;
            setGraphic(language.add ? removeButton : addButton);
        }
    }

    private class LanguagesDbButtonCell extends TableCell<Language, Boolean> {
        Button removeButton = new Button("Удалить");

        LanguagesDbButtonCell() {
            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                language.delete();
                new UserLanguage().delete_all(new UserLanguage().where("languageId", language.id));
                languages.remove(language);
                languagesTableView.setItems(languages);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(getGraphic() != null && t == null) setGraphic(null);
            if(empty) return;
            Language language = (Language) getTableRow().getItem();
            if(language == null) return;
            setGraphic(removeButton);
        }
    }
}
