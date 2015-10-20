package ru.gurps.generator.models.rules;

import javafx.collections.ObservableList;
import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

public class Skill extends Model {
    public Integer id;
    public String name;
    public String nameEn;
    public Integer skillType;
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

    public String getSkillType() {
        switch(skillType){
            case 0: return Main.locale.getString("strength_short");
            case 1: return Main.locale.getString("dexterity_short");
            case 2: return Main.locale.getString("intellect_short");
            case 3: return Main.locale.getString("health_short");
            case 4: return Main.locale.getString("health_points_short");
            case 5: return Main.locale.getString("will_short");
            case 6: return Main.locale.getString("perception_short");
            case 7: return Main.locale.getString("fatigue_points_short");
        }
        return null;
    }

    public String getTypeFull() {
        switch(skillType){
            case 0: return Main.locale.getString("strength");
            case 1: return Main.locale.getString("dexterity");
            case 2: return Main.locale.getString("intellect");
            case 3: return Main.locale.getString("health");
            case 4: return Main.locale.getString("health_points");
            case 5: return Main.locale.getString("will");
            case 6: return Main.locale.getString("perception");
            case 7: return Main.locale.getString("fatigue_points");
        }
        return null;
    }

    public void setSkillType(String skillType) {
        if(skillType.equals(Main.locale.getString("strength_short"))) this.skillType = 0;
        else if(skillType.equals(Main.locale.getString("dexterity_short"))) this.skillType = 1;
        else if(skillType.equals(Main.locale.getString("intellect_short"))) this.skillType = 2;
        else if(skillType.equals(Main.locale.getString("health_short"))) this.skillType = 3;
        else if(skillType.equals(Main.locale.getString("health_points_short"))) this.skillType = 4;
        else if(skillType.equals(Main.locale.getString("will_short"))) this.skillType = 5;
        else if(skillType.equals(Main.locale.getString("perception_short"))) this.skillType = 6;
        else if(skillType.equals(Main.locale.getString("fatigue_points_short"))) this.skillType = 7;
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

    public String getTwoHands(){
        return twoHands ? Main.locale.getString("yes") : Main.locale.getString("no");
    }

    public String getParry(){
        return parry ? Main.locale.getString("yes") : Main.locale.getString("no");
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getLevel() {
        return level;
    }

    public String getDefaultUse() {
        if(defaultUse == null) return Main.locale.getString("no");
        return defaultUse;
    }

    public String getDemands() {
        if(demands == null) return Main.locale.getString("no");
        return demands;
    }

    public String getModifiers() {
        if(modifiers == null) return "";
        else return Main.locale.getString("modifiers")+": " + modifiers;
    }

    public ObservableList<Specialization> specializations(){
        return this.hasMany(new Specialization());
    }

    public String getTypeAndComplexity(){
        return getSkillType() + "|" + getComplexity();
    }
}
