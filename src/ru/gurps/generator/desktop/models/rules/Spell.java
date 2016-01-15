package ru.gurps.generator.desktop.models.rules;

import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
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
    @Ignore public Integer finalCost;
    @Ignore public Boolean add = false;
    @Ignore public Button addButton;
    @Ignore public Button removeButton;
    @Ignore public TableRow row;

    public Spell() {
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
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

    public String getSchoolSingle() {
        return Main.locale.getString("school") + ": " + getSchool();
    }

    public String getResistanceSingle() {
        String res = resistance == null ? Main.locale.getString("no") : resistance;
        return Main.locale.getString("resistance") + ": " + res;
    }

    public String getTypeSingle() {
        return Main.locale.getString("type") + ": " + getSpellType();
    }

    public String getComplexitySingle() {
        return Main.locale.getString("complexity") + ": " + getComplexity();
    }

    public String getDemandsSingle() {
        String dem = demands == null ? Main.locale.getString("no") : demands;
        return Main.locale.getString("demands")+": " + dem;
    }

    public String getModifiersSingle() {
        if(modifiers == null) return "";
        else return Main.locale.getString("modifiers")+": " + modifiers;
    }

    public String getNeedTimeSingle() {
        return Main.locale.getString("need_time")+": " + needTime;
    }

    public String getCostSingle() {
        return Main.locale.getString("cost")+": " + cost;
    }

    public String getMaintainingCostSingle() {
        return Main.locale.getString("maintaining_cost")+": " + maintainingCost;
    }

    public String getDurationSingle() {
        return Main.locale.getString("duration")+": " + duration;
    }

    public String getThingSingle() {
        String thingS = thing == null ? Main.locale.getString("no") : thing;
        return Main.locale.getString("duration")+": " + thingS;
    }

    public String getCreateCostSingle() {
        String create = createCost == null ? Main.locale.getString("no") : createCost;
        return Main.locale.getString("duration")+": " + create;
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

    public void setAddAndColorRow(Boolean sAdd){
        this.add = sAdd;

        if (sAdd) {
            addButton.setVisible(false);
            removeButton.setVisible(true);
            row.getStyleClass().add("isAdd");
        } else {
            addButton.setVisible(true);
            removeButton.setVisible(false);
            row.getStyleClass().remove("isAdd");
        }
    }
}
