package ru.gurps.generator.models;

import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

public class Modifier extends Model {
    public Integer id;
    public String name;
    public String nameEn;
    public Integer cost;
    public String description;
    public Integer maxLevel;
    public Boolean combat;
    public Boolean improving;
    @Ignore public Integer level;

    public Modifier() {
    }

    public String getMaxLevel() {
        return Integer.toString(maxLevel);
    }

    public String getCombat() {
        return combat ? Main.locale.getString("yes") : Main.locale.getString("no");
    }

    public String getImproving() {
        return improving ? Main.locale.getString("yes") : Main.locale.getString("no");
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return Integer.toString(cost);
    }
}
