package ru.gurps.generator.models;

import ru.gurps.generator.Main;
import ru.gurps.generator.config.Model;

public class Language extends Model {
    public Integer id;
    public String name;
    @Ignore public Integer spoken = 0;
    @Ignore public Integer written = 0;
    @Ignore public Integer cost = 0;
    @Ignore public Boolean add = false;

    public Language() {
    }

    public Language(String language) {
        this.name = language;
    }

    public String getName() {
        return name;
    }

    public String getSpoken() {
        switch(spoken){
            case 0: return Main.locale.getString("not_have");
            case 1: return Main.locale.getString("broken");
            case 2: return Main.locale.getString("accent");
            case 3: return Main.locale.getString("native");
        }
        return null;
    }

    public void setSpoken(String spoken) {
        if(spoken.equals(Main.locale.getString("not_have"))) this.spoken = 0;
        else if(spoken.equals(Main.locale.getString("broken"))) this.spoken = 1;
        else if(spoken.equals(Main.locale.getString("accent"))) this.spoken = 2;
        else if(spoken.equals(Main.locale.getString("native"))) this.spoken = 3;
    }

    public String getWritten() {
        switch(written){
            case 0: return Main.locale.getString("illiteracy");
            case 1: return Main.locale.getString("semi-literate");
            case 2: return Main.locale.getString("literacy");
        }
        return null;
    }

    public void setWritten(String written) {
        if(written.equals(Main.locale.getString("illiteracy"))) this.written = 0;
        else if(written.equals(Main.locale.getString("semi-literate"))) this.written = 1;
        else if(written.equals(Main.locale.getString("literacy"))) this.written = 2;
    }

    public String getCost() {
        return Integer.toString(cost);
    }

    public Boolean getAdd() {
        return add;
    }
}
