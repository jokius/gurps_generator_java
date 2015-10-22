package ru.gurps.generator.desktop.models.rules;

import ru.gurps.generator.desktop.config.Model;

public class Quirk extends Model {
    public Integer id;
    public String name;
    @Ignore public Integer cost = 0;
    @Ignore public Boolean add = false;

    public Quirk() {
    }

    public Quirk(String language) {
        this.name = language;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return Integer.toString(cost);
    }

    public Boolean getAdd() {
        return add;
    }
}
