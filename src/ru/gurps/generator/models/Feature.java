package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Feature extends Model {
    public int id;
    public boolean advantage;
    public String title;
    public String titleEn;
    public String type;
    public int cost;
    public String description;
    public int oldLevel;
    public int maxLevel;
    public boolean psi;
    public boolean cybernetic;
    public boolean add;

    public Feature() {
    }

    public Feature(int id, boolean advantage, String title, String titleEn, String type, int cost, String description, int oldLevel, int maxLevel, boolean psi, boolean cybernetic, boolean add) {
        this.id = id;
        this.advantage = advantage;
        this.title = title;
        this.titleEn = titleEn;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.oldLevel = oldLevel;
        this.maxLevel = maxLevel;
        this.psi = psi;
        this.cybernetic = cybernetic;
        this.add = add;
    }

    public String getType() { 
        String new_type = type;
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", "Ф ");
        new_type = new_type.replace("2", "Соц ");
        new_type = new_type.replace("3", "М ");
        new_type = new_type.replace("4", "Э ");
        new_type = new_type.replace("5", "С ");
        return new_type;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}
