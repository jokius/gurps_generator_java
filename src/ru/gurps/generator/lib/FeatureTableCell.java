package ru.gurps.generator.lib;

import javafx.scene.control.TableCell;
import ru.gurps.generator.pojo.Feature;

public class FeatureTableCell extends TableCell<Feature, String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : getString());
        setGraphic(null);
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}