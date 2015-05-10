package ru.gurps.generator.models;

import ru.gurps.generator.Main;
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
            case 0: return Main.locale.getString("air");
        }
        return null;
    }

    public String getSpellType() {
        switch(spellType){
            case 0: return Main.locale.getString("usual");
            case 1: return Main.locale.getString("area");
            case 2: return Main.locale.getString("contact");
            case 3: return Main.locale.getString("throw");
            case 4: return Main.locale.getString("block_spell");
            case 5: return Main.locale.getString("resistance");
            case 6: return Main.locale.getString("information");
            case 7: return Main.locale.getString("charm");
            case 8: return Main.locale.getString("special");
        }
        return null;
    }

    public String getComplexity() {
        switch(complexity){
            case 0: return Main.locale.getString("easy_short");
            case 1: return Main.locale.getString("medium_short");
            case 2: return Main.locale.getString("hard_short");
            case 3: return Main.locale.getString("very_hard_short");
        }
        return null;
    }

    public String getComplexityFull() {
        switch(complexity){
            case 0: return Main.locale.getString("easy");
            case 1: return Main.locale.getString("medium");
            case 2: return Main.locale.getString("hard");
            case 3: return Main.locale.getString("very_hard");
        }
        return null;
    }

    public void setComplexity(String complexity) {
        if(complexity.equals(Main.locale.getString("easy_short"))) this.complexity = 0;
        else if(complexity.equals(Main.locale.getString("medium_short"))) this.complexity = 1;
        else if(complexity.equals(Main.locale.getString("hard_short"))) this.complexity = 2;
        else if(complexity.equals(Main.locale.getString("very_hard_short"))) this.complexity = 3;
    }
}
