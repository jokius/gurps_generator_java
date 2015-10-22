package ru.gurps.generator.desktop.lib.export.pojo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.models.rules.Cultura;
import ru.gurps.generator.desktop.models.rules.Skill;
import ru.gurps.generator.desktop.models.rules.Spell;
import ru.gurps.generator.desktop.models.Character;
import ru.gurps.generator.desktop.models.rules.Language;

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
