package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersSkill extends Model {
    public Integer id;
    public Integer characterId;
    public Integer skillId;
    public Integer cost;
    public Integer level;

    public CharactersSkill() {
    }

    public CharactersSkill(Integer characterId, Integer skillId, Integer cost, Integer level) {
        this.characterId = characterId;
        this.skillId = skillId;
        this.cost = cost;
        this.level = level;
    }
}
