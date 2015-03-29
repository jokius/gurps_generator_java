package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserSpell extends Model {
    public Integer id;
    public Integer userId;
    public Integer spellId;
    public Integer level;
    public Integer cost;

    public UserSpell() {
    }

    public UserSpell(Integer userId, Integer spellId, Integer level, Integer cost) {
        this.userId = userId;
        this.spellId = spellId;
        this.level = level;
        this.cost = cost;
    }
}
