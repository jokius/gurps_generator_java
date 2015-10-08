package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class CharactersLanguage extends Model {
    public Integer id;
    public Integer characterId;
    public Integer languageId;
    public Integer spoken = 0;
    public Integer written = 0;
    public Integer cost = 0;

    public CharactersLanguage() {
    }

    public CharactersLanguage(Integer characterId, Integer languageId, Integer spoken, Integer written, Integer cost) {
        this.characterId = characterId;
        this.languageId = languageId;
        this.spoken = spoken;
        this.written = written;
        this.cost = cost;
    }
}
