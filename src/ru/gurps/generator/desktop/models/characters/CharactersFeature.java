package ru.gurps.generator.desktop.models.characters;

import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.config.Model;

import java.util.HashMap;

public class CharactersFeature extends Model {
    public Integer id;
    public Integer characterId;
    public Integer featureId;
    public Integer cost;
    public Integer level;

    public CharactersFeature() {
    }

    public CharactersFeature(Integer characterId, Integer featureId, Integer cost, Integer level) {
        this.characterId = characterId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }

    public boolean delete_all(ObservableList<Model> models){
        if(models.isEmpty()) return true;
        try {
        for(Model model : models){
            HashMap<String, Object> params = new HashMap<>();
                params.put("characterId", model.getClass().getDeclaredField("characterId").get(model));
                params.put("featureId", model.getClass().getDeclaredField("featureId").get(model));

            new CharactersAddon().delete_all(new CharactersAddon().where(params));
        }

            return super.delete_all(models);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return true;
    }
}
