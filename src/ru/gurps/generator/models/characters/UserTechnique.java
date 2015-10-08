package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class UserTechnique extends Model {
    public Integer id;
    public Integer userId;
    public Integer techniqueId;
    public Integer cost;
    public Integer level;

    public UserTechnique() {
    }

    public UserTechnique(Integer userId, Integer techniqueId, Integer cost, Integer level) {
        this.userId = userId;
        this.techniqueId = techniqueId;
        this.cost = cost;
        this.level = level;
    }
}
