package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class Language extends Model {
    public Integer id;
    public String name;
    @Ignore public Integer spoken = 0;
    @Ignore public Integer written = 0;
    @Ignore public Integer cost = 0;

    public Language() {
    }

    public Language(String language) {
        this.name = language;
    }

    public String getLanguage() {
        return name;
    }

    public String getSpoken() {
        switch(spoken){
            case 0: return "Отсутствует";
            case 1: return "Ломаный";
            case 2: return "Акцент";
            case 3: return "Родной";
        }
        return null;
    }

    public void setSpoken(String spoken) {
        switch(spoken){
            case "Отсутствует": this.spoken = 0;
            case "Ломаный": this.spoken = 1;
            case "Акцент": this.spoken = 2;
            case "Родной": this.spoken = 3;
        }
    }

    public String getWritten() {
        switch(written){
            case 0: return "Неграмотность";
            case 1: return "Полу-грамотность";
            case 2: return "Грамотность";
        }
        return null;
    }

    public void setWritten(String written) {
        switch(written){
            case "Неграмотность": this.written = 0;
            case "Полу-грамотность": this.written = 1;
            case "Грамотность": this.written = 2;
        }
    }

    public Integer getCost() {
        return cost;
    }
}
