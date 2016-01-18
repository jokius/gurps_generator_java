package ru.gurps.generator.desktop.models.rules;

import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import ru.gurps.generator.desktop.config.Model;

public class Alchemy extends Model {
    public Integer id;
    public String name;
    public String nameEn;
    public String alternativeNames;
    public String description;
    public String duration;
    public String form;
    public String cost;
    public String recipe;
    @Ignore public Boolean add = false;
    @Ignore public Button addButton;
    @Ignore public Button removeButton;
    @Ignore public TableRow row;

    public Alchemy() {
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getAlternativeNames() {
        return alternativeNames;
    }

    public String getCost() {
        return cost;
    }

    public void setAddAndColorRow(Boolean sAdd){
        this.add = sAdd;

        if (sAdd) {
            addButton.setVisible(false);
            removeButton.setVisible(true);
            row.getStyleClass().add("isAdd");
        } else {
            addButton.setVisible(true);
            removeButton.setVisible(false);
            row.getStyleClass().remove("isAdd");
        }
    }
}
