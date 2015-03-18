package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Skill extends Model {
    public Integer id;
    public String name;
    public Integer type;
    public Integer complexity;
    @Ignore public Integer cost = 1;
    @Ignore public Integer level = 1;
    @Ignore public Boolean add = false;

    public Skill() {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        switch(type){
            case 0: return "CЛ";
            case 1: return "ЛВ";
            case 2: return "ИН";
            case 3: return "ЗД";
            case 4: return "Вол";
            case 5: return "Восп";
        }
        return null;
    }

    public void setType(String type) {
        switch(type){
            case "CЛ": this.type = 0;
            case "ЛВ": this.type = 1;
            case "ИН": this.type = 2;
            case "ЗД": this.type = 3;
            case "Вол": this.type = 3;
            case "Восп": this.type = 3;
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

    public void setСomplexity(String complexity) {
        switch(complexity){
            case "Л": this.complexity = 0;
            case "С": this.complexity = 1;
            case "Т": this.complexity = 2;
            case "ОТ": this.complexity = 3;
        }
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getLevel() {
        return level;
    }
}
