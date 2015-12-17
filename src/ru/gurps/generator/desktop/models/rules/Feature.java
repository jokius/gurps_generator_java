package ru.gurps.generator.desktop.models.rules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.config.Model;
import ru.gurps.generator.desktop.models.characters.CharactersModifier;

import java.util.HashMap;

public class Feature extends Model {
    public Integer id;
    public Boolean advantage;
    public String name;
    public String nameEn;
    public String featureType;
    public Integer cost;
    public String description;
    @Ignore public Integer oldLevel;
    public Integer maxLevel;
    public Boolean psi;
    public Boolean cybernetic;
    @Ignore public Boolean add  = false;
    @Ignore public Boolean modifier  = false;

    public Feature() {
    }

    public Feature(Integer id, Boolean advantage, String name, String nameEn, String featureType, Integer cost, String description, Integer oldLevel, Integer maxLevel, Boolean psi, Boolean cybernetic, Boolean add) {
        this.id = id;
        this.advantage = advantage;
        this.name = name;
        this.nameEn = nameEn;
        this.featureType = featureType;
        this.cost = cost;
        this.description = description;
        this.oldLevel = oldLevel;
        this.maxLevel = maxLevel;
        this.psi = psi;
        this.cybernetic = cybernetic;
        this.add = add;
    }

    public String getFeatureType() {
        String new_type = featureType;
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
        String new_type = featureType;
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

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
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

    public ObservableList<Modifier> modifiers(int characterId){
        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", characterId);
        params.put("featureId", id);
        ObservableList<CharactersModifier> charactersModifiers = new CharactersModifier().where(params);
        ObservableList<Modifier> modifiers = FXCollections.observableArrayList();
        for(CharactersModifier charactersModifier : charactersModifiers){
            Modifier modifier = (Modifier) new Modifier().find(charactersModifier.modifierId);
            modifier.level = charactersModifier.level;
            modifier.cost = charactersModifier.cost;
            modifiers.add(modifier);
        }
        return modifiers;
    }
}
