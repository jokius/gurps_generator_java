package ru.gurps.generator.lib.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.gurps.generator.controller.UsersController;
import ru.gurps.generator.lib.export.pojo.JsonUser;
import ru.gurps.generator.lib.export.pojo.JsonUserFeature;
import ru.gurps.generator.models.characters.*;
import ru.gurps.generator.models.rules.Feature;
import ru.gurps.generator.models.characters.CharactersAddon;

import java.io.*;
import java.util.HashMap;

public class ExportToJson {
    public ExportToJson(File file) {
        Integer id = UsersController.character.id;
        JsonUser jsonUser = new JsonUser();
        jsonUser.character = UsersController.character;
        jsonUser.userCulturas = new CharactersCultura().where("characterId", id);
        jsonUser.userLanguages = new CharactersLanguage().where("characterId", id);
        jsonUser.userSkills = new CharactersSkill().where("characterId", id);
        jsonUser.userSpells = new CharactersSpell().where("characterId", id);

        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", UsersController.character.id);

        for(Feature feature : UsersController.character.features()){
            JsonUserFeature jsonUserFeature = new JsonUserFeature();
            params.put("featureId", feature.id);
            jsonUserFeature.charactersFeature = (CharactersFeature) new CharactersFeature().find_by(params);
            jsonUserFeature.charactersAddons = new CharactersAddon().where("characterFeatureId", jsonUserFeature.charactersFeature.id);
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
