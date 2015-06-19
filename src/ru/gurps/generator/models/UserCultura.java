package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserCultura extends Model {
    public Integer id;
    public Integer userId;
    public Integer culturaId;
    public Integer cost = 0;

    public UserCultura() {
    }

    public UserCultura(Integer userId, Integer culturaId, Integer cost) {
        this.userId = userId;
        this.culturaId = culturaId;
        this.cost = cost;
    }
}
