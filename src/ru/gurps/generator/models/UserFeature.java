package ru.gurps.generator.models;

import javafx.collections.ObservableList;
import ru.gurps.generator.config.Model;

import java.util.HashMap;

public class UserFeature extends Model {
    public Integer id;
    public Integer userId;
    public Integer featureId;
    public Integer cost;
    public Integer level;

    public UserFeature() {
    }

    public UserFeature(Integer userId, Integer featureId, Integer cost, Integer level) {
        this.userId = userId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }

    public boolean delete_all(ObservableList<Model> models){
        if(models.isEmpty()) return true;
        try {
        for(Model model : models){
            HashMap<String, Object> params = new HashMap<>();
                params.put("userId", model.getClass().getDeclaredField("userId").get(model));
                params.put("userFeatureId", model.getClass().getDeclaredField("id").get(model));

            new FeatureAddon().delete_all(new FeatureAddon().where(params));
        }

            return super.delete_all(models);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return true;
    }
}
