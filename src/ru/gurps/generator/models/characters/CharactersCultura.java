package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersCultura extends Model {
    public Integer id;
    public Integer characterId;
    public Integer culturaId;
    public Integer cost = 0;

    public CharactersCultura() {
    }

    public CharactersCultura(Integer characterId, Integer culturaId, Integer cost) {
        this.characterId = characterId;
        this.culturaId = culturaId;
        this.cost = cost;
    }
}
