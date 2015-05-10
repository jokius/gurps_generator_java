package ru.gurps.generator.models;

import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

public class Feature extends Model {
    public Integer id;
    public Boolean advantage;
    public String title;
    public String titleEn;
    public String type;
    public Integer cost;
    public String description;
    @Ignore public Integer oldLevel;
    public Integer maxLevel;
    public Boolean psi;
    public Boolean cybernetic;
    @Ignore public Boolean add  = false;

    public Feature() {
    }

    public Feature(Integer id, Boolean advantage, String title, String titleEn, String type, Integer cost, String description, Integer oldLevel, Integer maxLevel, Boolean psi, Boolean cybernetic, Boolean add) {
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
        new_type = new_type.replace("1", Main.locale.getString("physical_short")+" ");
        new_type = new_type.replace("2", Main.locale.getString("social_short")+" ");
        new_type = new_type.replace("3", Main.locale.getString("mental_short")+" ");
        new_type = new_type.replace("4", Main.locale.getString("exotic_short")+" ");
        new_type = new_type.replace("5", Main.locale.getString("supernatural_short")+" ");
        return new_type;
    }

    public String getTypeFull() {
        String new_type = type;
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", Main.locale.getString("physical_single")+" ");
        new_type = new_type.replace("2", Main.locale.getString("social_single")+" ");
        new_type = new_type.replace("3", Main.locale.getString("mental_single")+" ");
        new_type = new_type.replace("4", Main.locale.getString("exotic_single")+" ");
        new_type = new_type.replace("5", Main.locale.getString("supernatural_single")+" ");
        return new_type;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public Integer getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}
