package ru.gurps.generator.models;

import javafx.beans.property.SimpleIntegerProperty;
import ru.gurps.generator.config.Model;

public class UserFeature extends Model {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty userId;
    private SimpleIntegerProperty featureId;
    private SimpleIntegerProperty cost;
    private SimpleIntegerProperty level;

    public UserFeature() {
    }

    public UserFeature(int id, int userId, int featureId, int cost, int level) {
        this.id = new SimpleIntegerProperty(id);
        this.userId = new SimpleIntegerProperty(userId);
        this.featureId = new SimpleIntegerProperty(featureId);
        this.cost = new SimpleIntegerProperty(cost);
        this.level = new SimpleIntegerProperty(level);
    }


    public int getId() { return id.get(); }

    public void setId(int sId) { id.set(sId); }

    public int getFeatureId() { return featureId.get(); }

    public void setFeatureId(int sFeatureId) { featureId.set(sFeatureId); }

    public int getUserId() { return userId.get(); }
    
    public void setUserId(int sUserId) { userId.set(sUserId); }

    public int getCost() { return cost.get(); }

    public void setCost(int sCost) { cost.set(sCost); }

    public int getLevel() { return level.get(); }

    public void seLevel(int sLevel) { level.set(sLevel); }
}
