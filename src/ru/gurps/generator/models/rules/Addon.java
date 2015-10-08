package ru.gurps.generator.models.rules;

import ru.gurps.generator.config.Model;

public class Addon extends Model {
    public Integer id;
    public Integer featureId;
    public String name;
    public String nameEn;
    public String cost;
    @Ignore public Integer resultCost = 0;
    public String description;
    public Integer maxLevel;
    @Ignore public String level = "1";
    @Ignore public Boolean active = false;

    public Addon() {
    }

    public Addon(Integer id, Integer featureId, String name, String nameEn, String cost, Integer resultCost, String description, Integer maxLevel, Boolean active, String level) {
        this.id = id;
        this.featureId = featureId;
        this.name = name;
        this.nameEn = nameEn;
        this.cost = cost;
        this.resultCost = resultCost;
        this.description = description;
        this.maxLevel = maxLevel;
        this.active = active;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getCost() {
        return cost;
    }

    public String getLevel() {
        return level;
    }
}
