package ru.gurps.generator.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ru.gurps.generator.config.Model;

public class Feature extends Model {
    private SimpleIntegerProperty id;
    private SimpleBooleanProperty advantage;
    private SimpleStringProperty title;
    private SimpleStringProperty titleEn;
    private SimpleStringProperty type;
    private SimpleIntegerProperty cost;
    private SimpleStringProperty description;
    private SimpleIntegerProperty oldLevel;
    private SimpleIntegerProperty maxLevel;
    private SimpleBooleanProperty psi;
    private SimpleBooleanProperty cybernetic;
    private SimpleBooleanProperty add;

    public Feature() {
    }

    public Feature(int id, boolean advantage, String title, String titleEn, String type, int cost,
                   String description, int oldLevel, int maxLevel, boolean psi, boolean cybernetic, boolean add) {
        this.id = new SimpleIntegerProperty(id);
        this.advantage = new SimpleBooleanProperty(advantage);
        this.title = new SimpleStringProperty(title);
        this.titleEn = new SimpleStringProperty(titleEn);
        this.type = new SimpleStringProperty(type);
        this.cost = new SimpleIntegerProperty(cost);
        this.description = new SimpleStringProperty(description);
        this.oldLevel = new SimpleIntegerProperty(oldLevel);
        this.maxLevel = new SimpleIntegerProperty(maxLevel);
        this.psi = new SimpleBooleanProperty(psi);
        this.cybernetic = new SimpleBooleanProperty(cybernetic);
        this.add = new SimpleBooleanProperty(add);
    }

    public int getId() { return id.get(); }

    public void setId(int sId) { id.set(sId); }

    public boolean getAdvantage() { return advantage.get(); }

    public void setAdvantage(boolean sAdvantage) { advantage.set(sAdvantage); }

    public String getTitle() { return title.get(); }

    public void setTitle(String sTitle) { title.set(sTitle); }

    public String getTitleEn() { return titleEn.get(); }

    public void setTitleEn(String sTitleEn) { titleEn.set(sTitleEn); }

    public String getType() { 
        String new_type = type.get();
        new_type = new_type.replace("[", "");
        new_type = new_type.replace("]", "");
        new_type = new_type.replace(",", "/ ");
        new_type = new_type.replace("1", "Ф ");
        new_type = new_type.replace("2", "Соц ");
        new_type = new_type.replace("3", "М ");
        new_type = new_type.replace("4", "Э ");
        new_type = new_type.replace("5", "С ");
        return new_type;
    }

    public void setType(String sType) { type.set(sType); }

    public int getCost() { return cost.get(); }

    public void setCost(int sCost) { cost.set(sCost); }

    public String getDescription() { return description.get(); }

    public void setDescription(String sDescription) { description.set(sDescription); }

    public int getMaxLevel() { return maxLevel.get(); }

    public void setMaxLevel(int sMaxLevel) { maxLevel.set(sMaxLevel); }

    public int getOldLevel() { return oldLevel.get(); }

    public void setOldLevel(int sOldLevel) { oldLevel.set(sOldLevel); }

    public boolean getPsi() { return psi.get(); }

    public void setPsi(boolean sPsi) { psi.set(sPsi); }

    public boolean getCybernetic() { return cybernetic.get(); }

    public void setCybernetic(boolean sCybernetic) { cybernetic.set(sCybernetic); }

    public boolean isAdd() {
        return add.get();
    }

    public void setAdd(boolean sAdd) {
        add.set(sAdd);
    }
}
