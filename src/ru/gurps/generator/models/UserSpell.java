package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserSpell extends Model {
    public Integer id;
    public Integer userId;
    public Integer spellId;
    public Integer cost;
    public Integer level;

    public UserSpell() {
    }

    public UserSpell(Integer userId, Integer spellId, Integer cost, Integer level) {
        this.userId = userId;
        this.spellId = spellId;
        this.cost = cost;
        this.level = level;
    }
}
