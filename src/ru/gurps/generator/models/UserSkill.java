package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserSkill extends Model {
    public Integer id;
    public Integer userId;
    public Integer skillId;
    public Integer cost;
    public Integer level;

    public UserSkill() {
    }

    public UserSkill(Integer userId, Integer skillId, Integer cost, Integer level) {
        this.userId = userId;
        this.skillId = skillId;
        this.cost = cost;
        this.level = level;
    }
}
