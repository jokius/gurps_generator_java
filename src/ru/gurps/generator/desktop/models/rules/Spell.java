package ru.gurps.generator.desktop.models.rules;

import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.config.Model;

public class Spell extends Model {
    public Integer id;
    public Integer schoolId;
    public String name;
    public String nameEn;
    public String spellType;
    public String description;
    public Integer complexity;
    public String cost;
    public String needTime;
    public String duration;
    public String maintainingCost;
    public String thing;
    public String createCost;
    public String demands;
    public String resistance;
    public String modifiers;
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

    public Integer getLevel() {
        return level;
    }

    public String getSchool() {
        return ((School) new School().find(schoolId)).name;
    }

    public String getResistance() {
        return resistance;
    }

    public String getModifiers() {
        return modifiers;
    }

    public String getCost() {
        return cost;
    }

    public String getSpellType() {
//        switch(spellType){
//            case 0: return Main.locale.getString("usual");
//            case 1: return Main.locale.getString("area");
//            case 2: return Main.locale.getString("contact");
//            case 3: return Main.locale.getString("throw");
//            case 4: return Main.locale.getString("block_spell");
//            case 5: return Main.locale.getString("resistance");
//            case 6: return Main.locale.getString("information");
//            case 7: return Main.locale.getString("charm");
//            case 8: return Main.locale.getString("special");
//        }
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
