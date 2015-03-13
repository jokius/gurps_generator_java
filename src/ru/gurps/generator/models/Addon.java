package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Addon extends Model {
    public int id;
    public int featuresId;
    public String title;
    public String titleEn;
    public String cost;
    public int resultCost;
    public String description;
    public int maxLevel;
    public boolean active;
    public String level;

    public Addon() {
    }

    public Addon(int id, int featuresId, String title, String titleEn, String cost, int resultCost, String description, int maxLevel, boolean active, String level) {
        this.id = id;
        this.featuresId = featuresId;
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
