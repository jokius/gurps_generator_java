package ru.gurps.generator.models;

import javafx.beans.property.SimpleIntegerProperty;
import ru.gurps.generator.config.Model;

public class FeatureAddon extends Model {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty userFeatureId;
    private SimpleIntegerProperty addonId;
    private SimpleIntegerProperty cost;
    private SimpleIntegerProperty level;

    public FeatureAddon() {
    }

    public FeatureAddon(int id, int userFeatureId, int addonId, int cost, int level) {
        this.id = new SimpleIntegerProperty(id);
        this.userFeatureId = new SimpleIntegerProperty(userFeatureId);
        this.addonId = new SimpleIntegerProperty(addonId);
        this.cost = new SimpleIntegerProperty(cost);
        this.level = new SimpleIntegerProperty(level);
    }


    public int getId() { return id.get(); }

    public void setId(int sId) { id.set(sId); }

    public int getUserFeatureId() { return userFeatureId.get(); }
    
    public void setUserFeatureId(int sUserFeatureId) { userFeatureId.set(sUserFeatureId); }

    public int addonId() { return addonId.get(); }

    public void addonId(int sAddonId) { addonId.set(sAddonId); }

    public int getCost() { return cost.get(); }

    public void setCost(int sCost) { cost.set(sCost); }

    public int getLevel() { return level.get(); }

    public void seLevel(int sLevel) { level.set(sLevel); }
}
