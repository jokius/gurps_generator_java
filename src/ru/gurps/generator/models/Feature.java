package ru.gurps.generator.models;

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
        new_type = new_type.replace("1", "Ф ");
        new_type = new_type.replace("2", "Соц ");
        new_type = new_type.replace("3", "М ");
        new_type = new_type.replace("4", "Э ");
        new_type = new_type.replace("5", "С ");
        return new_type;
    }

    public String getTypeFull() {
        String new_type = type;
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", "Физическая ");
        new_type = new_type.replace("2", "Социальная ");
        new_type = new_type.replace("3", "Ментальная ");
        new_type = new_type.replace("4", "Экзотическая ");
        new_type = new_type.replace("5", "Сверхъестественная ");
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
