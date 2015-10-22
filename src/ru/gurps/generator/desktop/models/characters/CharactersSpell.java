package ru.gurps.generator.desktop.models.characters;

import ru.gurps.generator.desktop.config.Model;

public class CharactersSpell extends Model {
    public Integer id;
    public Integer characterId;
    public Integer spellId;
    public Integer level;
    public Integer cost;

    public CharactersSpell() {
    }

    public CharactersSpell(Integer characterId, Integer spellId, Integer level, Integer cost) {
        this.characterId = characterId;
        this.spellId = spellId;
        this.level = level;
        this.cost = cost;
    }
}
