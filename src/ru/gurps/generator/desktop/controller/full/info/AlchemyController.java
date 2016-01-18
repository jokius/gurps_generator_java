package ru.gurps.generator.desktop.controller.full.info;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;
import ru.gurps.generator.desktop.models.characters.CharactersAlchemy;
import ru.gurps.generator.desktop.models.rules.Alchemy;

import java.util.HashMap;


public class AlchemyController extends AbstractController {
    private Alchemy alchemy;

    public Label name;
    public TextFlow fullDescription;

    public Label finalCost;
    public TextField level;
    public Button addButton;
    public Button removeButton;

    public AlchemyController(Alchemy alchemy) {
        this.alchemy = alchemy;
    }

    @FXML
    private void initialize() {
        setButtons();
        name.setText(alchemy.name + " (" + alchemy.nameEn + " )");
        fullDescription.getChildren().addAll(alternativeNames(), duration(), form(), recipe(), cost(), description());
    }

    private Text alternativeNames() {
        Text alternativeNames = new Text(alchemy.getAlternativeNamesSingle() + "\r\n\r\n");
        alternativeNames.setId("alternativeNames");
        return alternativeNames;
    }

    private Text duration() {
        Text duration = new Text(alchemy.getDurationSingle() + "\r\n");
        duration.setId("duration");
        return duration;
    }

    private Text form() {
        Text form = new Text(alchemy.getFormSingle() + "\r\n");
        form.setId("form");
        return form;
    }

    private Text recipe() {
        Text complexity = new Text(alchemy.getRecipeSingle() + "\r\n");
        complexity.setId("complexity");
        return complexity;
    }

    private Text cost() {
        Text cost = new Text(alchemy.getCostSingle() + "\r\n\r\n");
        cost.setId("cost");
        return cost;
    }

    private Text description() {
        Text description = new Text(alchemy.description + "\r\n");
        description.setId("description");
        return description;
    }

    private void setButtons() {
        setVisibleButtons();

        addButton.setOnAction(event -> {
            new CharactersAlchemy(character.id, alchemy.id).create();
            alchemy.setAddAndColorRow(true);
            setVisibleButtons();
        });

        removeButton.setOnAction(event -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("characterId", character.id);
            params.put("alchemyId", alchemy.id);
            CharactersAlchemy charactersAlchemy = (CharactersAlchemy) new CharactersAlchemy().find_by(params);
            charactersAlchemy.delete();
            alchemy.setAddAndColorRow(false);
            setVisibleButtons();
        });
    }

    private void setVisibleButtons(){
        if (alchemy.add) {
            addButton.setVisible(false);
            removeButton.setVisible(true);
        } else {
            addButton.setVisible(true);
            removeButton.setVisible(false);
        }
    }
}
