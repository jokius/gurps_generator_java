package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class UserLanguage extends Model {
    public Integer id;
    public Integer userId;
    public String languageId;
    public Integer spoken = 0;
    public Integer written = 0;
    public Integer cost = 0;

    public UserLanguage() {
    }

    public UserLanguage(Integer userId, String languageId, Integer spoken, Integer written, Integer cost) {
        this.userId = userId;
        this.languageId = languageId;
        this.spoken = spoken;
        this.written = written;
        this.cost = cost;
    }
}
