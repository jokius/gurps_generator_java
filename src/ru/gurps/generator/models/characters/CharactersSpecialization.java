package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersSpecialization extends Model {
    public Integer id;
    public Integer characterId;
    public Integer specializationId;
    public Integer cost;
    public Integer level;

    public CharactersSpecialization() {
    }

    public CharactersSpecialization(Integer characterId, Integer specializationId, Integer cost, Integer level) {
        this.characterId = characterId;
        this.specializationId = specializationId;
        this.cost = cost;
        this.level = level;
    }
}
