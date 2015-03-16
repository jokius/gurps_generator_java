package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class FeatureAddon extends Model {
    public Integer id;
    public Integer userFeatureId;
    public Integer addonId;
    public String cost;
    public String level;

    public FeatureAddon() {
    }

    public FeatureAddon(Integer userFeatureId, Integer addonId, String cost, String level) {
        this.userFeatureId = userFeatureId;
        this.addonId = addonId;
        this.cost = cost;
        this.level = level;
    }
}
