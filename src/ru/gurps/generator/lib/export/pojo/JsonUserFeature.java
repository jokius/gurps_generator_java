package ru.gurps.generator.lib.export.pojo;

import javafx.collections.ObservableList;
import ru.gurps.generator.models.rules.FeatureAddon;
import ru.gurps.generator.models.characters.UserFeature;

public class JsonUserFeature {
    public UserFeature userFeature;
    public ObservableList<FeatureAddon> featureAddons;

    public JsonUserFeature() {
    }
}
