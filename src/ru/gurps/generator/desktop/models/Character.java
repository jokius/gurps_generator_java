package ru.gurps.generator.desktop.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.desktop.config.Model;
import ru.gurps.generator.desktop.models.characters.*;
import ru.gurps.generator.desktop.models.rules.*;

public class Character extends Model {
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
    
    public Character(){
    }

    public Character(String name, String maxPoints) {
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
        ObservableList<CharactersFeature> charactersFeatures = this.hasMany(new CharactersFeature());
        ObservableList<Feature> features = FXCollections.observableArrayList();
        for(CharactersFeature charactersFeature : charactersFeatures){
            Feature feature = (Feature) new Feature().find(charactersFeature.featureId);
            feature.add = true;
            feature.oldLevel = charactersFeature.level;
            feature.cost = charactersFeature.cost;
            features.add(feature);
        }
        return features;
    }

    public ObservableList<Skill> skills(){
        ObservableList<CharactersSkill> charactersSkills = this.hasMany(new CharactersSkill());
        ObservableList<Skill> skills = FXCollections.observableArrayList();
        for(CharactersSkill charactersSkill : charactersSkills){
            Skill skill = (Skill) new Skill().find(charactersSkill.skillId);
            skill.add = true;
            skill.level = charactersSkill.level;
            skill.cost = charactersSkill.cost;
            skills.add(skill);
        }

        ObservableList<CharactersSpecialization> charactersSpecializations = this.hasMany(new CharactersSpecialization());
        for(CharactersSpecialization charactersSpecialization : charactersSpecializations){
            Specialization specialization = (Specialization)
                    new Specialization().find(charactersSpecialization.specializationId);
            Skill skill = (Skill) new Skill().find(specialization.skillId);
            skill.name = skill.name + " (" + specialization.name + ")";
            skill.nameEn = skill.nameEn + " (" + specialization.nameEn + ")";
            skill.level = charactersSpecialization.level;
            skill.cost = charactersSpecialization.cost;
            skills.add(skill);
        }
        return skills;
    }

    public ObservableList<Spell> spells(){
        ObservableList<CharactersSpell> charactersSpells = this.hasMany(new CharactersSpell());
        ObservableList<Spell> spells = FXCollections.observableArrayList();
        for(CharactersSpell charactersSpell : charactersSpells){
            Spell spell = (Spell) new Spell().find(charactersSpell.spellId);
            spell.add = true;
            spell.level = charactersSpell.level;
            spell.finalCost = charactersSpell.cost;
            spells.add(spell);
        }
        return spells;
    }

    public ObservableList<Language> languages(){
        ObservableList<CharactersLanguage> charactersLanguages = this.hasMany(new CharactersLanguage());
        ObservableList<Language> languages = FXCollections.observableArrayList();
        for(CharactersLanguage charactersLanguage : charactersLanguages){
            Language language = (Language) new Language().find(charactersLanguage.languageId);
            language.spoken = charactersLanguage.spoken;
            language.written = charactersLanguage.written;
            language.cost = charactersLanguage.cost;
            languages.add(language);
        }
        return languages;
    }

    public ObservableList<Cultura> cultures(){
        ObservableList<CharactersCultura> charactersCulturas = this.hasMany(new CharactersCultura());
        ObservableList<Cultura> culturas = FXCollections.observableArrayList();
        for(CharactersCultura charactersCultura : charactersCulturas){
            Cultura cultura = (Cultura) new Cultura().find(charactersCultura.culturaId);
            cultura.cost = charactersCultura.cost;
            culturas.add(cultura);
        }
        return culturas;
    }

    public ObservableList<Quirk> quirks(){
        ObservableList<CharactersQuirk> charactersQuirks = this.hasMany(new CharactersQuirk());
        ObservableList<Quirk> quirks = FXCollections.observableArrayList();
        for(CharactersQuirk charactersQuirk : charactersQuirks){
            Quirk quirk = (Quirk) new Quirk().find(charactersQuirk.quirkId);
            quirk.cost = charactersQuirk.cost;
            quirks.add(quirk);
        }
        return quirks;
    }

    public String getPlayer() {
        if(player == null || player.equals("")) return "-";
        else return player;
    }

    public boolean delete(){
        new CharactersFeature().delete_all(new CharactersFeature().where("characterId", id));
        new CharactersModifier().delete_all(new CharactersModifier().where("characterId", id));
        new CharactersLanguage().delete_all(new CharactersLanguage().where("characterId", id));
        new CharactersSkill().delete_all(new CharactersSkill().where("characterId", id));
        new CharactersSpecialization().delete_all(new CharactersSpecialization().where("characterId", id));
        new CharactersCultura().delete_all(new CharactersCultura().where("characterId", id));
        new CharactersQuirk().delete_all(new CharactersQuirk().where("characterId", id));
        new CharactersSpell().delete_all(new CharactersSpell().where("characterId", id));
        new CharactersTechnique().delete_all(new CharactersTechnique().where("characterId", id));
        return super.delete();
    }
}
