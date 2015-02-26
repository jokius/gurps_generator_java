package ru.gurps.generator.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ru.gurps.generator.config.Model;

public class Addon extends Model {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty featuresId;
    private SimpleStringProperty title;
    private SimpleStringProperty titleEn;
    private SimpleStringProperty cost;
    private SimpleIntegerProperty resultCost;
    private SimpleStringProperty description;
    private SimpleIntegerProperty maxLevel;
    private SimpleBooleanProperty active;
    private SimpleStringProperty level;

    public Addon() {
    }

    public Addon(int id, int featuresId, String title, String titleEn, String cost, int resultCost, String description, int maxLevel, boolean active, String level) {
        this.id = new SimpleIntegerProperty(id);
        this.featuresId = new SimpleIntegerProperty(featuresId);
        this.title = new SimpleStringProperty(title);
        this.titleEn = new SimpleStringProperty(titleEn);
        this.cost = new SimpleStringProperty(cost);
        this.resultCost = new SimpleIntegerProperty(resultCost);
        this.description = new SimpleStringProperty(description);
        this.maxLevel = new SimpleIntegerProperty(maxLevel);
        this.active = new SimpleBooleanProperty(active);
        this.level = new SimpleStringProperty(level);
   }

    public int getId() { return id.get(); }

    public void setId(int sId) { id.set(sId); }

    public int getfeaturesId() { return featuresId.get(); }

    public void setfeaturesId(int sFeaturesId) { id.set(sFeaturesId); }

    public String getTitle() { return title.get(); }

    public void setTitle(String sTitle) { title.set(sTitle); }

    public String getTitleEn() { return titleEn.get(); }

    public void setTitleEn(String sTitleEn) { titleEn.set(sTitleEn); }

    public String getCost() { return cost.get(); }

    public void setCost(String sCost) { cost.set(sCost); }

    public int getResultCost() { return resultCost.get(); }

    public void setResultCost(int sResultCost) { resultCost.set(sResultCost); }

    public String getDescription() { return description.get(); }

    public void setDescription(String sDescription) { description.set(sDescription); }

    public int getMaxLevel() { return maxLevel.get(); }

    public void setMaxLevel(int sMaxLevel) { maxLevel.set(sMaxLevel); }

    public boolean getActive() { return active.get(); }

    public void setActive(boolean sActive) { active.set(sActive); }

    public String getLevel() { return level.get(); }

    public void setLevel(String sLevel) { level.set(sLevel); }
}
