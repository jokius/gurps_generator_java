package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserSkillSpecialization extends Model {
    public Integer id;
    public Integer userSkillId;
    public Integer skillSpecializationId;
    public String cost;
    public String level;

    public UserSkillSpecialization() {
    }

    public UserSkillSpecialization(Integer id, Integer userSkillId, Integer skillSpecializationId, String cost, String level) {
        this.id = id;
        this.userSkillId = userSkillId;
        this.skillSpecializationId = skillSpecializationId;
        this.cost = cost;
        this.level = level;
    }
}
