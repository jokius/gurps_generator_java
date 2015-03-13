package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserFeature extends Model {
    public int id;
    public int userId;
    public int featureId;
    public int cost;
    public int level;

    public UserFeature() {
    }

    public UserFeature(int id, int userId, int featureId, int cost, int level) {
        this.id = id;
        this.userId = userId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }
}
