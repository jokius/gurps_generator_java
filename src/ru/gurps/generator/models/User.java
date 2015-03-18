package ru.gurps.generator.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.config.Model;

import java.util.stream.Collectors;

public class User extends Model {
    public Integer id;
    public String player;
    public String name;
    public String currentPoints;
    public String maxPoints;
    public Integer st = 10;
    public Integer dx = 10;
    public Integer iq = 10;
    public Integer ht = 10;
    public Integer hp = 10;
    public Integer will = 10;
    public Integer per = 10;
    public Integer fp = 10;
    public Double bs = 5.0;
    public Integer move = 5;
    public Integer sm = 0;
    public Integer growth = 0;
    public Integer weight = 0;
    public Integer age = 0;
    public Integer tl = 0;
    public Integer tlCost = 0;
    public Integer head = 0;
    public Integer torse = 0;
    public Integer arm = 0;
    public Integer leg = 0;
    public Integer hand = 0;
    public Integer foot = 0;
    public Boolean noFineManipulators = false;
    
    public User(){
    }

    public User(String name, String maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public String getName() {
        return name;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getMaxPoints() {
        return maxPoints;
    }

    public ObservableList<Feature> features(){
        ObservableList<UserFeature> userFeatures = this.hasMany(new UserFeature());
        ObservableList<Feature> features = FXCollections.observableArrayList();
        for(UserFeature userFeature : userFeatures){
            Feature feature = (Feature) new Feature().find(userFeature.featureId);
            feature.add = true;
            feature.oldLevel = userFeature.level;
            feature.cost = userFeature.cost;
            features.add(feature);
        }
        return features;
    }

    public ObservableList<Skill> skills(){
        ObservableList<UserSkill> userSkills = this.hasMany(new UserFeature());
        ObservableList<Skill> skills = FXCollections.observableArrayList();
        for(UserSkill userSkill : userSkills){
            Skill skill = (Skill) new Skill().find(userSkill.skillId);
            skill.add = true;
            skill.level = userSkill.level;
            skill.cost = userSkill.cost;
            skills.add(skill);
        }
        return skills;
    }

    public ObservableList<Spell> spells(){
        ObservableList<UserSpell> userSpells = this.hasMany(new UserFeature());
        ObservableList<Spell> spells = FXCollections.observableArrayList();
        for(UserSpell userSpell : userSpells){
            Spell spell = (Spell) new Spell().find(userSpell.spellId);
            spell.add = true;
            spell.level = userSpell.level;
            spell.cost = userSpell.cost;
            spells.add(spell);
        }
        return spells;
    }
}
