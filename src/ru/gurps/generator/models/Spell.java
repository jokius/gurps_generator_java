package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Spell extends Model {
    public Integer id;
    public String name;
    public Integer complexity;
    public Integer cost;
    @Ignore public Integer level;

    public Spell() {
    }

    public String getName() {
        return name;
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
