package ru.gurps.generator.desktop.lib.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.gurps.generator.desktop.controller.characters.SelectController;
import ru.gurps.generator.desktop.lib.export.pojo.JsonCharacter;
import ru.gurps.generator.desktop.lib.export.pojo.JsonCharacterFeature;
import ru.gurps.generator.desktop.models.characters.*;
import ru.gurps.generator.desktop.models.rules.Feature;
import ru.gurps.generator.desktop.models.characters.CharactersAddon;

import java.io.*;
import java.util.HashMap;

public class ExportToJson {
    public ExportToJson(File file) {
        Integer id = SelectController.character.id;
        JsonCharacter jsonCharacter = new JsonCharacter();
        jsonCharacter.character = SelectController.character;
        jsonCharacter.characterCulturas = new CharactersCultura().where("characterId", id);
        jsonCharacter.characterLanguages = new CharactersLanguage().where("characterId", id);
        jsonCharacter.characterSkills = new CharactersSkill().where("characterId", id);
        jsonCharacter.characterSpells = new CharactersSpell().where("characterId", id);

        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", SelectController.character.id);

        for(Feature feature : SelectController.character.features()){
            JsonCharacterFeature jsonCharacterFeature = new JsonCharacterFeature();
            params.put("featureId", feature.id);
            jsonCharacterFeature.charactersFeature = (CharactersFeature) new CharactersFeature().find_by(params);
            jsonCharacterFeature.charactersAddons = new CharactersAddon().where(params);
            jsonCharacter.jsonCharacterFeatures.add(jsonCharacterFeature);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.print(gson.toJson(jsonCharacter));
            writer.close();
        } catch(FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
