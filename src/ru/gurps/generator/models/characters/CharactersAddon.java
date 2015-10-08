package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersAddon extends Model {
    public Integer id;
    public Integer characterFeatureId;
    public Integer addonId;
    public String cost;
    public String level;

    public CharactersAddon() {
    }

    public CharactersAddon(Integer characterFeatureId, Integer addonId, String cost, String level) {
        this.characterFeatureId = characterFeatureId;
        this.addonId = addonId;
        this.cost = cost;
        this.level = level;
    }
}
