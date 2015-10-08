package ru.gurps.generator.lib.export.pojo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.models.*;
import ru.gurps.generator.models.rules.Cultura;
import ru.gurps.generator.models.rules.Language;
import ru.gurps.generator.models.rules.Skill;
import ru.gurps.generator.models.rules.Spell;

public class JsonUser {
    public User user;
    public ObservableList<JsonUserFeature> JsonUserFeatures = FXCollections.observableArrayList();
    public ObservableList<Spell> userSpells;
    public ObservableList<Skill> userSkills;
    public ObservableList<Language> userLanguages;
    public ObservableList<Cultura> userCulturas;

    public JsonUser() {
    }
}
