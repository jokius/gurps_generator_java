package ru.gurps.generator.lib.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.models.*;
import ru.gurps.generator.lib.export.pojo.JsonUser;
import ru.gurps.generator.lib.export.pojo.JsonUserFeature;

import java.io.*;
import java.util.HashMap;

public class ExportToJson {
    public ExportToJson(File file) {
        Integer id = UsersController.user.id;
        JsonUser jsonUser = new JsonUser();
        jsonUser.user = UsersController.user;
        jsonUser.userCulturas = new UserCultura().where("userId", id);
        jsonUser.userLanguages = new UserLanguage().where("userId", id);
        jsonUser.userSkills = new UserSkill().where("userId", id);
        jsonUser.userSpells = new UserSpell().where("userId", id);

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", UsersController.user.id);

        for(Feature feature : UsersController.user.features()){
            JsonUserFeature jsonUserFeature = new JsonUserFeature();
            params.put("featureId", feature.id);
            jsonUserFeature.userFeature = (UserFeature) new UserFeature().find_by(params);
            jsonUserFeature.featureAddons = new FeatureAddon().where("userFeatureId", jsonUserFeature.userFeature.id);
            jsonUser.JsonUserFeatures.add(jsonUserFeature);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.print(gson.toJson(jsonUser));
            writer.close();
        } catch(FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
