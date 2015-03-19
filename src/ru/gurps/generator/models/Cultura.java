package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Cultura extends Model {
    public Integer id;
    public String name;
    @Ignore public Integer cost = 0;

    public Cultura() {
    }

    public Cultura(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getCost() {
        return cost;
    }
}
