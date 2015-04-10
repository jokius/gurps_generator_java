package ru.gurps.generator.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    public ChoiceBox spokenChoiceBox;
    public ChoiceBox writtenChoiceBox;
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

        ObservableList<String> spokenValues = FXCollections.observableArrayList("Отсутствует", "Ломаный", "Акцент", "Родной");
        ObservableList<String> writtenValues = FXCollections.observableArrayList("Неграмотность", "Полу-грамотность", "Грамотность");

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
            if(event.getNewValue().equals("0") || "\\D".matches(event.getNewValue())) return;
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
        tableView.setPlaceholder(new Label("Языков нет"));
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
            if(language.cost != 0){
                String points = Integer.toString(Integer.parseInt(user.currentPoints) + language.cost);
                globalCost.setText(points);
                user.update_single("currentPoints", points);
            }

            languages.add(language);
            tableView.setItems(languages);
            nameText.setText("");
            addButton.setDisable(true);
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
                globalCost.setText(points);
                user.update_single("currentPoints", points);
                setGraphic(removeButton);
            });

            removeButton.setOnAction(t -> {
                Language language = (Language) getTableRow().getItem();
                new UserLanguage().find_by("languageId", language.id).delete();

                language.add = false;
                String points = Integer.toString(Integer.parseInt(user.currentPoints) - language.cost);
                globalCost.setText(points);
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
                tableView.setItems(languages);
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
