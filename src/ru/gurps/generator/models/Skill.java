package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Skill extends Model {
    public Integer id;
    public String name;
    public String nameEn;
    public Integer type;
    public Integer complexity;
    public String defaultUse;
    public String demands;
    public String description;
    public String modifiers;
    public Boolean twoHands = false;
    public Boolean parry = false;
    public Integer parryBonus = 0;
    @Ignore public Integer cost = 1;
    @Ignore public Integer level = 1;
    @Ignore public Boolean add = false;

    public Skill() {
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getType() {
        switch(type){
            case 0: return "CЛ";
            case 1: return "ЛВ";
            case 2: return "ИН";
            case 3: return "ЗД";
            case 4: return "ОЗ";
            case 5: return "Вол";
            case 6: return "Восп";
            case 7: return "ОУ";
        }
        return null;
    }

    public String getTypeFull() {
        switch(type){
            case 0: return "Сила";
            case 1: return "Ловкость";
            case 2: return "Интелект";
            case 3: return "Здоровье";
            case 4: return "Очки Здаровья";
            case 5: return "Воля";
            case 6: return "Восприятие";
            case 7: return "Очки Усталости";
        }
        return null;
    }

    public void setType(String type) {
        switch(type){
            case "CЛ": this.type = 0;
            case "ЛВ": this.type = 1;
            case "ИН": this.type = 2;
            case "ЗД": this.type = 3;
            case "ОЗ": this.type = 4;
            case "Вол": this.type = 5;
            case "Восп": this.type = 6;
            case "ОУ": this.type = 7;
        }
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

    public String getTwoHands(){
        return twoHands ? "да" : "нет";
    }

    public String getParry(){
        return parry ? "да" : "нет";
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getLevel() {
        return level;
    }

    public String getDefaultUse() {
        if(defaultUse == null) return "нет";
        return defaultUse;
    }

    public String getDemands() {
        if(demands == null) return "нет";
        return demands;
    }

    public String getModifiers() {
        if(modifiers == null) return "";
        else return "Модификатры: " + modifiers;
    }
}
