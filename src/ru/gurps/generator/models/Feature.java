package ru.gurps.generator.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

import java.util.HashMap;

public class Feature extends Model {
    public Integer id;
    public Boolean advantage;
    public String title;
    public String titleEn;
    public String type;
    public Integer cost;
    public String description;
    @Ignore public Integer oldLevel;
    public Integer maxLevel;
    public Boolean psi;
    public Boolean cybernetic;
    @Ignore public Boolean add  = false;
    @Ignore public Boolean modifier  = false;
//    @Ignore public String fullTitle;

    public Feature() {
    }

    public Feature(Integer id, Boolean advantage, String title, String titleEn, String type, Integer cost, String description, Integer oldLevel, Integer maxLevel, Boolean psi, Boolean cybernetic, Boolean add) {
        this.id = id;
        this.advantage = advantage;
        this.title = title;
        this.titleEn = titleEn;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.oldLevel = oldLevel;
        this.maxLevel = maxLevel;
        this.psi = psi;
        this.cybernetic = cybernetic;
        this.add = add;
    }

    public String getType() {
        String new_type = type;
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", Main.locale.getString("physical_short")+" ");
        new_type = new_type.replace("2", Main.locale.getString("social_short")+" ");
        new_type = new_type.replace("3", Main.locale.getString("mental_short")+" ");
        new_type = new_type.replace("4", Main.locale.getString("exotic_short")+" ");
        new_type = new_type.replace("5", Main.locale.getString("supernatural_short")+" ");
        return new_type;
    }

    public String getTypeFull() {
        String new_type = type;
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", Main.locale.getString("physical_single")+" ");
        new_type = new_type.replace("2", Main.locale.getString("social_single")+" ");
        new_type = new_type.replace("3", Main.locale.getString("mental_single")+" ");
        new_type = new_type.replace("4", Main.locale.getString("exotic_single")+" ");
        new_type = new_type.replace("5", Main.locale.getString("supernatural_single")+" ");
        return new_type;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public Integer getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getModifier() {
        return modifier;
    }

    public ObservableList<Modifier> modifiers(int userId){
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("featureId", id);
        ObservableList<UserModifier> userModifiers = new UserModifier().where(params);
        ObservableList<Modifier> modifiers = FXCollections.observableArrayList();
        for(UserModifier userModifier : userModifiers){
            Modifier modifier = (Modifier) new Modifier().find(userModifier.modifierId);
            modifier.level = userModifier.level;
            modifier.cost = userModifier.cost;
            modifiers.add(modifier);
        }
        return modifiers;
    }
}
