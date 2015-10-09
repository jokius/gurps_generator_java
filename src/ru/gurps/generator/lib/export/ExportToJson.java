package ru.gurps.generator.lib.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.gurps.generator.controller.characters.CharactersController;
import ru.gurps.generator.lib.export.pojo.JsonCharacter;
import ru.gurps.generator.lib.export.pojo.JsonCharacterFeature;
import ru.gurps.generator.models.characters.*;
import ru.gurps.generator.models.rules.Feature;
import ru.gurps.generator.models.characters.CharactersAddon;

import java.io.*;
import java.util.HashMap;

public class ExportToJson {
    public ExportToJson(File file) {
        Integer id = CharactersController.character.id;
        JsonCharacter jsonCharacter = new JsonCharacter();
        jsonCharacter.character = CharactersController.character;
        jsonCharacter.characterCulturas = new CharactersCultura().where("characterId", id);
        jsonCharacter.characterLanguages = new CharactersLanguage().where("characterId", id);
        jsonCharacter.characterSkills = new CharactersSkill().where("characterId", id);
        jsonCharacter.characterSpells = new CharactersSpell().where("characterId", id);

        HashMap<String, Object> params = new HashMap<>();
        params.put("characterId", CharactersController.character.id);

        for(Feature feature : CharactersController.character.features()){
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
