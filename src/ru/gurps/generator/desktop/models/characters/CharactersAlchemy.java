package ru.gurps.generator.desktop.models.characters;

import ru.gurps.generator.desktop.config.Model;

public class CharactersAlchemy extends Model {
    public Integer id;
    public Integer characterId;
    public Integer alchemyId;

    public CharactersAlchemy() {
    }

    public CharactersAlchemy(Integer characterId, Integer alchemyId) {
        this.characterId = characterId;
        this.alchemyId = alchemyId;
    }
}
