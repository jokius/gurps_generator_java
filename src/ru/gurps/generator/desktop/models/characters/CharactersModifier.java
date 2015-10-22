package ru.gurps.generator.desktop.models.characters;

import ru.gurps.generator.desktop.config.Model;

public class CharactersModifier extends Model {
    public Integer id;
    public Integer characterId;
    public Integer modifierId;
    public Integer featureId;
    public Integer cost;
    public Integer level;

    public CharactersModifier() {
    }

    public CharactersModifier(Integer characterId, Integer modifierId, Integer featureId, Integer cost, Integer level) {
        this.characterId = characterId;
        this.modifierId = modifierId;
        this.featureId = featureId;
        this.cost = cost;
        this.level = level;
    }
}
