package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserModifier extends Model {
    public Integer id;
    public Integer userId;
    public Integer modifierId;
    public Integer featureId;
    public Integer cost;
    public Integer level;

    public UserModifier() {
    }

    public UserModifier(Integer userId, Integer modifierId, Integer featureId, Integer cost, Integer level) {
        this.userId = userId;
        this.modifierId = modifierId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }
}
