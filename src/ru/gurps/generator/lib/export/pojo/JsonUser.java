package ru.gurps.generator.lib.export.pojo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.models.*;

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
