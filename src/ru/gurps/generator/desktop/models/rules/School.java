package ru.gurps.generator.desktop.models.rules;

import ru.gurps.generator.desktop.config.Model;

public class School extends Model {
    public Integer id;
    public String name;

    public School() {
    }

    public School(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
