package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class FeatureAddon extends Model {
    public int id;
    public int userFeatureId;
    public int addonId;
    public String cost;
    public int level;

    public FeatureAddon() {
    }

    public FeatureAddon(int userFeatureId, int addonId, String cost, int level) {
        this.userFeatureId = userFeatureId;
        this.addonId = addonId;
        this.cost = cost;
        this.level = level;
    }
}
