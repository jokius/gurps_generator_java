package ru.gurps.generator.desktop.controller.tables;

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
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.controller.full.info.TechniqueController;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.lib.CharacterParams;
import ru.gurps.generator.desktop.models.Character;
import ru.gurps.generator.desktop.models.characters.CharactersTechnique;
import ru.gurps.generator.desktop.models.rules.Technique;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TechniquesController extends AbstractController {
    Character character = AbstractController.character;

    public TableView<Technique> tableView;
    public TableColumn<Technique, String> nameColumn;
    public TableColumn<Technique, String> nameEnColumn;
    public TableColumn<Technique, String> complexityColumn;
    public TableColumn<Technique, String> demandsColumn;
    public TableColumn<Technique, String> defaultUseColumn;

    public MenuButton searchButton;
    public MenuItem searchAll;
    public MenuItem searchName;
    public MenuItem searchNameEn;
    public MenuItem searchDescription;
    public MenuItem reset;
    public TextField searchText;


    public CheckMenuItem t0CheckBox;
    public CheckMenuItem t1CheckBox;
    public CheckMenuItem t2CheckBox;
    public CheckMenuItem t3CheckBox;
    public CheckMenuItem t4CheckBox;
    public CheckMenuItem t5CheckBox;
    public CheckMenuItem t6CheckBox;
    public CheckMenuItem t7CheckBox;

    public CheckMenuItem c0CheckBox;
    public CheckMenuItem c1CheckBox;
    public CheckMenuItem c2CheckBox;
    public CheckMenuItem c3CheckBox;


    public TextField level;
    public Label finalCost;
    public Button add;
    public Button remove;
    public Button full;

    public AnchorPane bottomMenu;

    protected ArrayList<Integer> complexityNumbers = new ArrayList<>();

    private int lastId = -1;

    @FXML
    private void initialize() {
        for(int i = 0; 3 >= i; i++) complexityNumbers.add(i);
        setCheckBox();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameEnColumn.setCellValueFactory(new PropertyValueFactory<>("nameEn"));
        complexityColumn.setCellValueFactory(new PropertyValueFactory<>("complexity"));
        demandsColumn.setCellValueFactory(new PropertyValueFactory<>("demands"));
        defaultUseColumn.setCellValueFactory(new PropertyValueFactory<>("defaultUse"));

        nameColumn.setCellFactory(column -> new TableCell<Technique, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            getTableRow().getStyleClass().remove("isAdd");

            if(item != null || !empty) {
                setText(item);
                Technique technique = tableView.getItems().get(getTableRow().getIndex());
                HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", character.id);
                params.put("techniqueId", technique.id);
                CharactersTechnique charactersTechnique = (CharactersTechnique) new CharactersTechnique().find_by(params);
                getTableRow().getStyleClass().remove("isAdd");
                if(charactersTechnique.id == null) getTableRow().getStyleClass().remove("isAdd");
                else getTableRow().getStyleClass().add("isAdd");
            }

            if (empty) {
                setText(null);
                setGraphic(null);
            }
        }
    });

        tableView.setPlaceholder(new Label(Main.locale.getString("techniques_not_found")));
        ObservableList<Technique> techniques = new Technique().all();
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", character.id);

        for(Technique technique : techniques){
            params.put("techniqueId", technique.id);
            CharactersTechnique charactersTechnique = (CharactersTechnique) new CharactersTechnique().find_by(params);
            if(charactersTechnique.level != null) {
                technique.cost = charactersTechnique.cost;
                technique.level = charactersTechnique.level;
                technique.add = true;
            }
        }

        tableView.setItems(techniques);
        tableView.getSortOrder().add(nameColumn);
        tableView.setRowFactory(tv -> {
            TableRow<Technique> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, new TechniqueEventHandler());
            return row;
        });

        localSearch(new Technique(), tableView, searchText, searchButton, searchAll, searchName, searchNameEn,
                searchDescription, reset);
    }

    private void setCheckBox(){
        Integer[] numbers = {0, 1, 2, 3};
        for(Integer number : numbers) {
            try {
                CheckMenuItem checkBox = (CheckMenuItem) this.getClass().getDeclaredField("c" + number + "CheckBox").get(this);
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String query = "complexity like ";
                    if(newValue) complexityNumbers.add(number);
                    else complexityNumbers.remove(number);

                    for(Integer sNumber : complexityNumbers) {
                        if(query.equals("complexity like ")) query += "'%" + sNumber + "%'";
                        else query += " or complexity like '%" + sNumber + "%'";
                    }
                    if(query.equals("complexity like ")) query = "complexity='-1'";
                    tableView.setItems(new Technique().where(query));
                });
            } catch(NoSuchFieldException | IllegalAccessException e) {
                return;
            }
        }
    }

    class TechniqueEventHandler implements EventHandler<MouseEvent> {
        private Technique technique;
        private TableRow row;

        public TechniqueEventHandler() {
        }

        @Override
        public void handle(MouseEvent t) {
            row = (TableRow) t.getSource();
            technique = tableView.getItems().get(row.getIndex());
            if(technique.id == lastId) return;
            lastId = technique.id;
            bottomMenu();
        }

        void bottomMenu(){
            setupBottomMenu();
            defaultParams();

            level.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if(oldValue.equals(newValue) || newValue.equals("")) return;
                technique.level = Integer.parseInt(newValue);
                int cost = CharacterParams.techniqueCost(technique);
                finalCost.setText(Integer.toString(cost));
                technique.cost = cost;
            });
            setButtons();
        }

        void defaultParams() {
            level.setText(Integer.toString(technique.level));
            if(technique.cost > 0) finalCost.setText(Integer.toString(technique.cost));
            else finalCost.setText(Integer.toString(CharacterParams.techniqueCost(technique)));
        }

        void setButtons(){
            add.setOnAction(event -> {
                new CharactersTechnique(character.id, technique.id, technique.cost, technique.level).create();
                setCurrentPoints(technique.cost + globalCost());
                technique.add = true;
                add.setVisible(false);
                remove.setVisible(true);
                row.getStyleClass().add("isAdd");
            });

            remove.setOnAction(event -> {
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("characterId", character.id);
                params1.put("techniqueId", technique.id);
                CharactersTechnique charactersTechnique = (CharactersTechnique) new CharactersTechnique().find_by(params1);
                setCurrentPoints(Integer.parseInt(character.currentPoints) - charactersTechnique.cost);
                charactersTechnique.delete();
                technique.add = false;
                add.setVisible(true);
                remove.setVisible(false);
                row.getStyleClass().remove("isAdd");
            });

            full.setOnAction(actionEvent -> {
                Stage childrenStage = new Stage();
                FXMLLoader loader;
                loader = new FXMLLoader(Main.class.getResource("resources/views/full/info/techniqueFull.fxml"));
                loader.setController(new TechniqueController(technique));

                loader.setResources(Main.locale);
                Parent childrenRoot;
                try {
                    childrenRoot = loader.load();
                    childrenStage.setResizable(false);
                    childrenStage.setScene(new Scene(childrenRoot, 635, 572));
                    childrenStage.setTitle("GURPSGenerator - " + technique.name + " (" + technique.nameEn + ")");
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
    }
}
