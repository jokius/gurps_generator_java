package ru.gurps.generator.pojo;

import javafx.beans.property.SimpleStringProperty;

public class Addon {
    private final SimpleStringProperty id;
    private final SimpleStringProperty featuresId;
    private final SimpleStringProperty title;
    private final SimpleStringProperty titleEn;
    private final SimpleStringProperty cost;
    private final SimpleStringProperty description;
    private final SimpleStringProperty maxLevel;

    public Addon(String id, String featuresId, String title, String titleEn, String cost, String description, String maxLevel) {
        this.id = new SimpleStringProperty(id);
        this.featuresId = new SimpleStringProperty(featuresId);
        this.title = new SimpleStringProperty(title);
        this.titleEn = new SimpleStringProperty(titleEn);
        this.cost = new SimpleStringProperty(cost);
        this.description = new SimpleStringProperty(description);
        this.maxLevel = new SimpleStringProperty(maxLevel);
    }

    public String getId() { return id.get(); }

    public void setId(String sId) { id.set(sId); }

    public String getfeaturesId() { return featuresId.get(); }

    public void setfeaturesId(String sFeaturesId) { id.set(sFeaturesId); }

    public String getTitle() { return title.get(); }

    public void setTitle(String sTitle) { title.set(sTitle); }

    public String getTitleEn() { return titleEn.get(); }

    public void setTitleEn(String sTitleEn) { titleEn.set(sTitleEn); }

    public String getCost() { return cost.get(); }

    public void setCost(String sCost) { cost.set(sCost); }

    public String getDescription() { return description.get(); }

    public void setDescription(String sDescription) { description.set(sDescription); }

    public String getMaxLevel() { return maxLevel.get(); }

    public void setMaxLevel(String sMaxLevel) { maxLevel.set(sMaxLevel); }
}
