package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Addon extends Model {
    public Integer id;
    public Integer featureId;
    public String title;
    public String titleEn;
    public String cost;
    @Ignore public Integer resultCost = 0;
    public String description;
    public Integer maxLevel;
    @Ignore public String level = "1";
    @Ignore public Boolean active = false;

    public Addon() {
    }

    public Addon(Integer id, Integer featureId, String title, String titleEn, String cost, Integer resultCost, String description, Integer maxLevel, Boolean active, String level) {
        this.id = id;
        this.featureId = featureId;
        this.title = title;
        this.titleEn = titleEn;
        this.cost = cost;
        this.resultCost = resultCost;
        this.description = description;
        this.maxLevel = maxLevel;
        this.active = active;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getCost() {
        return cost;
    }

    public String getLevel() {
        return level;
    }
}
