package ru.gurps.generator.desktop.models.characters;

import ru.gurps.generator.desktop.config.Model;

public class CharactersAddon extends Model {
    public Integer id;
    public Integer characterId;
    public Integer featureId;
    public Integer addonId;
    public String cost;
    public String level;

    public CharactersAddon() {
    }

    public CharactersAddon(Integer characterId, Integer featureId, Integer addonId, String cost, String level) {
        this.characterId = characterId;
        this.featureId = featureId;
        this.addonId = addonId;
        this.cost = cost;
        this.level = level;
    }
}
