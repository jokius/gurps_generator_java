package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Spell extends Model {
    public Integer id;
    public Integer school;
    public String name;
    public String nameEn;
    public Integer spellType;
    public String description;
    public Integer complexity;
    public Integer cost;
    public Integer maxCost;
    public String needTime;
    public String duration;
    public String maintainingCost;
    public String thing;
    public String createCost;
    public String demands;
    @Ignore public Integer level = 1;
    @Ignore public Integer finalCost = 0;
    @Ignore public Boolean add = false;

    public Spell() {
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNeedTime() {
        return needTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getMaintainingCost() {
        return maintainingCost;
    }

    public String getThing() {
        return thing;
    }

    public String getCreateCost() {
        return createCost;
    }

    public String getDemands() {
        return demands;
    }

    public String getDescription() {
        return description;
    }

    public Integer getFinalCost() {
        return finalCost;
    }

    public String getCost() {
        if(cost == maxCost) return Integer.toString(cost);
        else return cost + " - " + maxCost;
    }

    public Integer getLevel() {
        return level;
    }

    public String getSchool() {
        switch(school){
            case 0: return "Воздух";
        }
        return null;
    }

    public String getSpellType() {
        switch(spellType){
            case 0: return "Обычное";
            case 1: return "Областное";
            case 2: return "Касательное";
            case 3: return "Метательное";
            case 4: return "Блокирующие";
            case 5: return "Сопротивляемое";
            case 6: return "Информационное";
            case 7: return "Чары";
            case 8: return "Особое";
        }
        return null;
    }

    public String getComplexity() {
        switch(complexity){
            case 0: return "Л";
            case 1: return "С";
            case 2: return "Т";
            case 3: return "ОТ";
        }
        return null;
    }

    public String getComplexityFull() {
        switch(complexity){
            case 0: return "Ллегко";
            case 1: return "Средне";
            case 2: return "Трудно";
            case 3: return "Очень Трудно";
        }
        return null;
    }

    public void setСomplexity(String complexity) {
        switch(complexity){
            case "Л": this.complexity = 0;
            case "С": this.complexity = 1;
            case "Т": this.complexity = 2;
            case "ОТ": this.complexity = 3;
        }
    }
}
