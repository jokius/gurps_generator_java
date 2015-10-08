package ru.gurps.generator.models.rules;

import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

public class Technique extends Model {
    public Integer id;
    public String name;
    public String nameEn;
    public Integer complexity;
    public String defaultUse;
    public String demands;
    public String description;
    @Ignore public Integer cost = 1;
    @Ignore public Integer level = 1;
    @Ignore public Boolean add = false;

    public Technique() {
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
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
}
