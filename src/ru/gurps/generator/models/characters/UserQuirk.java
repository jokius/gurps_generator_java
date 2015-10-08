package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class UserQuirk extends Model {
    public Integer id;
    public Integer userId;
    public Integer quirkId;
    public Integer cost = 0;

    public UserQuirk() {
    }

    public UserQuirk(Integer userId, Integer quirkId, Integer cost) {
        this.userId = userId;
        this.quirkId = quirkId;
        this.cost = cost;
    }
}
