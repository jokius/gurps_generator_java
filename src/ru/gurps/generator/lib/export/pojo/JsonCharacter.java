package ru.gurps.generator.lib.export.pojo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.models.Character;
import ru.gurps.generator.models.rules.Cultura;
import ru.gurps.generator.models.rules.Language;
import ru.gurps.generator.models.rules.Skill;
import ru.gurps.generator.models.rules.Spell;

public class JsonCharacter {
    public Character character;
    public ObservableList<JsonCharacterFeature> jsonCharacterFeatures = FXCollections.observableArrayList();
    public ObservableList<Spell> characterSpells;
    public ObservableList<Skill> characterSkills;
    public ObservableList<Language> characterLanguages;
    public ObservableList<Cultura> characterCulturas;

    public JsonCharacter() {
    }
}
