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
        String new_type = spellType;
        new_type = new_type.replace(",", "; ");
        new_type = new_type.replace(":", Main.locale.getString("or")+ " ");
        new_type = new_type.replace("0", Main.locale.getString("usual") + " ");
        new_type = new_type.replace("1", Main.locale.getString("area") + " ");
        new_type = new_type.replace("2", Main.locale.getString("contact") + " ");
        new_type = new_type.replace("3", Main.locale.getString("throw") + " ");
        new_type = new_type.replace("4", Main.locale.getString("block_spell") + " ");
        new_type = new_type.replace("5", Main.locale.getString("resistance") + " ");
        new_type = new_type.replace("6", Main.locale.getString("information") + " ");
        new_type = new_type.replace("7", Main.locale.getString("charm") + " ");
        new_type = new_type.replace("8", Main.locale.getString("special") + " ");
        return new_type;
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
