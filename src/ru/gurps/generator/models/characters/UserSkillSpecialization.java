package ru.gurps.generator.models.characters;

import ru.gurps.generator.config.Model;

public class UserSkillSpecialization extends Model {
    public Integer id;
    public Integer userId;
    public Integer skillSpecializationId;
    public Integer cost;
    public Integer level;

    public UserSkillSpecialization() {
    }

    public UserSkillSpecialization(Integer userId, Integer skillSpecializationId, Integer cost, Integer level) {
        this.userId = userId;
        this.skillSpecializationId = skillSpecializationId;
        this.cost = cost;
        this.level = level;
    }
}
