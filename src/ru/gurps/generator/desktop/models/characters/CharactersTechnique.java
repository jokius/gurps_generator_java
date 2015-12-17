package ru.gurps.generator.desktop.models.characters;

import ru.gurps.generator.desktop.config.Model;

public class CharactersTechnique extends Model {
    public Integer id;
    public Integer characterId;
    public Integer techniqueId;
    public Integer cost;
    public Integer level;

    public CharactersTechnique() {
    }

    public CharactersTechnique(Integer characterId, Integer techniqueId, Integer cost, Integer level) {
        this.characterId = characterId;
        this.techniqueId = techniqueId;
        this.cost = cost;
        this.level = level;
    }
}
