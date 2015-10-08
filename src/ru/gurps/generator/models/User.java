package ru.gurps.generator.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.gurps.generator.config.Model;
import ru.gurps.generator.models.characters.*;
import ru.gurps.generator.models.rules.*;

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
        ObservableList<UserSkill> userSkills = this.hasMany(new UserSkill());
        ObservableList<Skill> skills = FXCollections.observableArrayList();
        for(UserSkill userSkill : userSkills){
            Skill skill = (Skill) new Skill().find(userSkill.skillId);
            skill.add = true;
            skill.level = userSkill.level;
            skill.cost = userSkill.cost;
            skills.add(skill);
        }

        ObservableList<UserSkillSpecialization> userSkillSpecializations = this.hasMany(new UserSkillSpecialization());
        for(UserSkillSpecialization userSkillSpecialization : userSkillSpecializations){
            SkillSpecialization specialization = (SkillSpecialization)
                    new SkillSpecialization().find(userSkillSpecialization.skillSpecializationId);
            Skill skill = (Skill) new Skill().find(specialization.skillId);
            skill.name = skill.name + " (" + specialization.name + ")";
            skill.nameEn = skill.nameEn + " (" + specialization.nameEn + ")";
            skill.level = userSkillSpecialization.level;
            skill.cost = userSkillSpecialization.cost;
            skills.add(skill);
        }
        return skills;
    }

    public ObservableList<Spell> spells(){
        ObservableList<UserSpell> userSpells = this.hasMany(new UserSpell());
        ObservableList<Spell> spells = FXCollections.observableArrayList();
        for(UserSpell userSpell : userSpells){
            Spell spell = (Spell) new Spell().find(userSpell.spellId);
            spell.add = true;
            spell.level = userSpell.level;
            spell.finalCost = userSpell.cost;
            spells.add(spell);
        }
        return spells;
    }

    public ObservableList<Language> languages(){
        ObservableList<UserLanguage> userLanguages = this.hasMany(new UserLanguage());
        ObservableList<Language> languages = FXCollections.observableArrayList();
        for(UserLanguage userLanguage : userLanguages){
            Language language = (Language) new Language().find(userLanguage.languageId);
            language.spoken = userLanguage.spoken;
            language.written = userLanguage.written;
            language.cost = userLanguage.cost;
            languages.add(language);
        }
        return languages;
    }

    public ObservableList<Cultura> cultures(){
        ObservableList<UserCultura> userCulturas = this.hasMany(new UserCultura());
        ObservableList<Cultura> culturas = FXCollections.observableArrayList();
        for(UserCultura userCultura : userCulturas){
            Cultura cultura = (Cultura) new Cultura().find(userCultura.culturaId);
            cultura.cost = userCultura.cost;
            culturas.add(cultura);
        }
        return culturas;
    }

    public ObservableList<Quirk> quirks(){
        ObservableList<UserQuirk> userQuirks = this.hasMany(new UserQuirk());
        ObservableList<Quirk> quirks = FXCollections.observableArrayList();
        for(UserQuirk userQuirk : userQuirks){
            Quirk quirk = (Quirk) new Quirk().find(userQuirk.quirkId);
            quirk.cost = userQuirk.cost;
            quirks.add(quirk);
        }
        return quirks;
    }

    public String getPlayer() {
        if(player == null || player.equals("")) return "-";
        else return player;
    }

    public boolean delete(){
        new UserFeature().delete_all(new UserFeature().where("userId", id));
        new UserModifier().delete_all(new UserModifier().where("userId", id));
        new UserLanguage().delete_all(new UserLanguage().where("userId", id));
        new UserSkill().delete_all(new UserSkill().where("userId", id));
        new UserSkillSpecialization().delete_all(new UserSkillSpecialization().where("userId", id));
        new UserCultura().delete_all(new UserCultura().where("userId", id));
        new UserQuirk().delete_all(new UserQuirk().where("userId", id));
        new UserSpell().delete_all(new UserSpell().where("userId", id));
        new UserTechnique().delete_all(new UserTechnique().where("userId", id));
        return super.delete();
    }
}
