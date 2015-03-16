package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserFeature extends Model {
    public Integer id;
    public Integer userId;
    public Integer featureId;
    public Integer cost;
    public Integer level;

    public UserFeature() {
    }

    public UserFeature(Integer userId, Integer featureId, Integer cost, Integer level) {
        this.userId = userId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }
}
