package ru.gurps.generator.controller.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gurps.generator.Main;
import ru.gurps.generator.controller.full.info.ModifierController;
import ru.gurps.generator.controller.helpers.AbstractController;
import ru.gurps.generator.models.Character;
import ru.gurps.generator.models.characters.CharactersModifier;
import ru.gurps.generator.models.rules.Feature;
import ru.gurps.generator.models.rules.Modifier;

import java.io.IOException;
import java.util.HashMap;

public class ModifiersController extends AbstractController {
    Character character = AbstractController.character;

    public TableView<Modifier> tableView;
    public TableColumn<Modifier, String> modifiersNameColumn;
    public TableColumn<Modifier, String> modifiersNameEnColumn;
    public TableColumn<Modifier, String> costColumn;
    public TableColumn<Modifier, String> maxLevelColumn;
    public TableColumn<Modifier, String> combatColumn;
    public TableColumn<Modifier, String> descriptionColumn;

    public TableView<Feature> featuresTableView;
    public TableColumn<Feature, String> featuresNameColumn;
    public TableColumn<Feature, String> featuresNameEnColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchCost;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;


    public CheckMenuItem combatCheckBox;
    public CheckMenuItem improvingCheckBox;
    public CheckMenuItem limitationCheckBox;

    public TextField levelText;
    public Label levelLabel;
    public ComboBox<Integer> levelComboBox;
    public Label finalCost;
    public TextField finalCostText;

    public Button full;
    public Button add;
    public Button remove;

    public AnchorPane bottomMenu;

    private int lastId = -1;
    private int featureLastId = -1;

    @FXML
    private void initialize() {
        setCheckBox();

        modifiersNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifiersNameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        maxLevelColumn.setCellValueFactory(new PropertyValueFactory<>("maxLevel"));
        combatColumn.setCellValueFactory(new PropertyValueFactory<>("combat"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        modifiersNameColumn.setCellFactory(column -> new TableCell<Modifier, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            getTableRow().getStyleClass().remove("addOne");

            if(item != null || !empty) {
                setText(item);
                Modifier modifier = tableView.getItems().get(getTableRow().getIndex());
                HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", character.id);
                params.put("modifierId", modifier.id);
                CharactersModifier charactersModifier = (CharactersModifier) new CharactersModifier().find_by(params);
                if(charactersModifier.id == null) getTableRow().getStyleClass().remove("addOne");
                else getTableRow().getStyleClass().add("addOne");
            }

            if (empty) {
                setText(null);
                setGraphic(null);
            }
        }
    });

        tableView.setPlaceholder(new Label(Main.locale.getString("modifiers_not_found")));
        ObservableList<Modifier> modifiers = new Modifier().all();

        tableView.setItems(modifiers);
        tableView.getSortOrder().add(modifiersNameColumn);
        tableView.setRowFactory(tv -> {
            TableRow<Modifier> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, new ModifierEventHandler());
            return row;
        });

        localSearch(new Modifier(), tableView, searchText, searchButton, searchAll, searchName, searchNameEn,
                searchCost, searchDescription, reset);
    }

    private void setCheckBox() {
        combatCheckBox.setSelected(true);
        improvingCheckBox.setSelected(true);
        limitationCheckBox.setSelected(true);

        combatCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String query = "";
            query += newValue ? "combat=true" : "combat=false";
            if(improvingCheckBox.isSelected() && !limitationCheckBox.isSelected()) query += " and improving=true";
            else if(!improvingCheckBox.isSelected() && limitationCheckBox.isSelected()) query += " and improving=true";
            else if(!improvingCheckBox.isSelected()) query += " and id='-1'";
            tableView.setItems(new Modifier().where(query));
        });

        improvingCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String query = "";

            if(newValue && !limitationCheckBox.isSelected()) query += " and improving=true";
            else if(!newValue && limitationCheckBox.isSelected()) query += " and improving=true";
            else if(!newValue) query += " and id='-1'";

            query += improvingCheckBox.isSelected() ? " and combat=true" : " and combat=false";
            tableView.setItems(new Modifier().where(query));
        });

        limitationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String query = "";

            if(improvingCheckBox.isSelected() && !newValue) query += " and improving=true";
            else if(!improvingCheckBox.isSelected() && newValue) query += " and improving=true";
            else if(!improvingCheckBox.isSelected()) query += " and id='-1'";

            query += improvingCheckBox.isSelected() ? " and combat=true" : " and combat=false";
            tableView.setItems(new Modifier().where(query));
        });
    }

    class ModifierEventHandler implements EventHandler<MouseEvent> {
        private Modifier modifier;
        private TableRow row;

        public ModifierEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            row = (TableRow) t.getSource();
            modifier = tableView.getItems().get(row.getIndex());
            if(modifier.id == lastId) return;
            lastId = modifier.id;
            featureLastId = -1;
            bottomMenu();
        }

        void bottomMenu(){
            setupBottomMenu();
            defaultParams();
            setButtons();
        }

        void defaultParams() {
            levelLabel.setText("-");
            levelComboBox.setVisible(false);
            levelText.setVisible(false);
            levelLabel.setVisible(true);

            finalCost.setVisible(true);
            finalCostText.setVisible(false);
            finalCost.setText("-");
            setFeatures();
        }

        private void setFeatures() {
            ObservableList<Feature> features = FXCollections.observableArrayList();
            HashMap<String, Object> params = new HashMap<>();
            params.put("characterId", character.id);
            for(Object object : character.features()){
                Feature feature = (Feature) object;
                params.put("featureId", feature.id);
                CharactersModifier charactersModifier = (CharactersModifier) new CharactersModifier().find_by(params);
                feature.modifier = charactersModifier.id != null;
                features.add(feature);
            }

            featuresNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            featuresNameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));

            featuresNameColumn.setCellFactory(column -> new TableCell<Feature, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            getTableRow().getStyleClass().remove("addOne");

                            if (item != null || !empty) {
                                setText(item);
                                Feature feature = featuresTableView.getItems().get(getTableRow().getIndex());
                                if(feature.modifier) getTableRow().getStyleClass().add("isAdd");
                                else getTableRow().getStyleClass().remove("isAdd");
                            }

                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            }
                        }
                    });

                featuresTableView.setPlaceholder(new

                        Label(Main.locale.getString("features_not_found")

                ));
                featuresTableView.setItems(features);

            featuresTableView.setRowFactory(tv -> {
                        TableRow<Feature> newRow = new TableRow<>();
                        newRow.addEventFilter(MouseEvent.MOUSE_CLICKED, new FeatureEventHandler());
                        return newRow;
                    }

                );
            }

        void setButtons(){
            full.setOnAction(actionEvent -> {
                Stage childrenStage = new Stage();
                FXMLLoader loader;
                loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/modifierFull.fxml"));
                loader.setController(new ModifierController(modifier));

                loader.setResources(Main.locale);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setResizable(false);
                    childrenStage.setScene(new Scene(childrenRoot, 635, 572));
                    childrenStage.setTitle("GURPSGenerator - " + modifier.name + " (" + modifier.nameEn + ")");
                    childrenStage.show();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            });
        }

        void setupBottomMenu() {
            final double bottomMenuSize = 134.0;
            AnchorPane.setBottomAnchor(tableView, bottomMenuSize);
            bottomMenu.setVisible(true);
        }

        private class FeatureEventHandler implements EventHandler<MouseEvent> {
            private Feature feature;
            private TableRow featureRow;

            public FeatureEventHandler() {
            }

            @Override
            public void handle(MouseEvent t) {
                featureRow = (TableRow) t.getSource();
                feature = featuresTableView.getItems().get(featureRow.getIndex());
                if(feature.id == featureLastId) return;
                featureLastId = feature.id;

                setModifierCost();
                setModifierLevel();
                setActionButtons();
            }

            private void setActionButtons() {
                if(feature.modifier){
                    remove.setVisible(true);
                    add.setVisible(false);
                    add.setDisable(true);
                } else {
                    add.setDisable(false);
                    add.setVisible(true);
                    remove.setVisible(false);
                }

                add.setOnAction(t -> {
                    new CharactersModifier(character.id, modifier.id, feature.id, modifier.cost, currentLevel()).create();
                    feature.modifier = true;
                    featureCostUpdate();
                    setModifierCost();
                    setModifierLevel();

                    remove.setVisible(true);
                    add.setVisible(false);
                    add.setDisable(true);

                    row.getStyleClass().add("addOne");
                    featureRow.getStyleClass().add("isAdd");
                });

                remove.setOnAction(t -> {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("characterId", character.id);
                    params.put("featureId", feature.id);
                    params.put("modifierId", modifier.id);
                    new CharactersModifier().find_by(params).delete();
                    feature.modifier = false;
                    featureCostUpdate();
                    setModifierCost();
                    setModifierLevel();

                    add.setDisable(false);
                    add.setVisible(true);
                    remove.setVisible(false);

                    row.getStyleClass().remove("addOne");
                    featureRow.getStyleClass().remove("isAdd");
                });
            }

            private int currentLevel(){
                switch (modifier.maxLevel){
                    case 1 : return 1;
                    case 0 : return Integer.parseInt(levelText.getText());
                    default: return levelComboBox.getValue();
                }
            }

            private int currentCost() {
                return (int) (feature.cost * (modifier.cost * currentLevel() / 100.0));
            }

            private void featureCostUpdate(){
                int cost = currentCost();
                if(feature.modifier) setCurrentPoints(globalCost() + cost);
                else setCurrentPoints(globalCost() - cost);
            }

            private void setModifierCost() {
                if(modifier.cost == 0 && feature.modifier){
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("characterId", character.id);
                    params.put("featureId", feature.id);
                    params.put("modifierId", modifier.id);
                    CharactersModifier charactersModifier = (CharactersModifier) new CharactersModifier().find_by(params);

                    finalCostText.setVisible(false);
                    finalCost.setVisible(true);
                    modifier.cost = charactersModifier.cost;

                    finalCost.setText(Integer.toString(charactersModifier.cost));
                    return;
                }

                if(modifier.cost == 0){
                    finalCostText.setVisible(true);
                    finalCost.setVisible(false);

                    finalCostText.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.equals("")) return;
                        if(!newValue.matches("\\d+")) modifier.cost = Integer.parseInt(newValue);
                    });

                    finalCostText.setText(Integer.toString(modifier.cost));
                } else {
                    finalCostText.setVisible(false);
                    finalCost.setVisible(true);

                    finalCost.setText(Integer.toString(modifier.cost));
                }
            }

            private void setModifierLevel() {
                if(feature.modifier){
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("characterId", character.id);
                    params.put("featureId", feature.id);
                    params.put("modifierId", modifier.id);
                    CharactersModifier charactersModifier = (CharactersModifier) new CharactersModifier().find_by(params);

                    levelLabel.setVisible(true);
                    levelComboBox.setVisible(false);
                    levelText.setVisible(false);

                    levelLabel.setText(Integer.toString(charactersModifier.level));
                    return;
                }

                if(modifier.maxLevel == 1) {
                    levelLabel.setVisible(true);
                    levelComboBox.setVisible(false);
                    levelText.setVisible(false);

                    levelLabel.setText("1");
                } else if(modifier.maxLevel == 0) {
                    levelLabel.setVisible(false);
                    levelComboBox.setVisible(false);
                    levelText.setVisible(true);

                    levelText.setText("1");
                    levelText.textProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.equals("")) return;
                        if(!newValue.matches("\\d+")) {
                            finalCost.setText(Integer.toString(currentCost(newValue)));
                        }
                    });
                } else {
                    levelLabel.setVisible(false);
                    levelComboBox.setVisible(true);
                    levelText.setVisible(false);

                    ObservableList<Integer> levels = FXCollections.observableArrayList();
                    for(int i = 1; modifier.maxLevel >= i; i++) levels.add(i);
                    levelComboBox.setItems(levels);
                    levelComboBox.setValue(1);
                    levelComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                            finalCost.setText(Integer.toString(currentCost(newValue))));
                }
            }

            private int currentCost(int level) {
                return (int) (feature.cost * (modifier.cost * level / 100.0));
            }

            private int currentCost(String level) {
                return (int) (feature.cost * (modifier.cost * Integer.parseInt(level) / 100.0));
            }
        }
    }
}
