package ru.gurps.generator.pojo;

import javafx.beans.property.SimpleStringProperty;

public class Feature {
    private final SimpleStringProperty id;
    private final SimpleStringProperty advantage;
    private final SimpleStringProperty title;
    private final SimpleStringProperty titleEn;
    private final SimpleStringProperty type;
    private final SimpleStringProperty cost;
    private final SimpleStringProperty description;
    private final SimpleStringProperty oldLevel;
    private final SimpleStringProperty maxLevel;
    private final SimpleStringProperty psi;
    private final SimpleStringProperty cybernetic;

    public Feature(String id, String advantage, String title, String titleEn, String type, String cost, String description, String oldLevel, String maxLevel, String psi, String cybernetic) {
        this.id = new SimpleStringProperty(id);
        this.advantage = new SimpleStringProperty(advantage);
        this.title = new SimpleStringProperty(title);
        this.titleEn = new SimpleStringProperty(titleEn);
        this.type = new SimpleStringProperty(type);
        this.cost = new SimpleStringProperty(cost);
        this.description = new SimpleStringProperty(description);
        this.oldLevel = new SimpleStringProperty(oldLevel);
        this.maxLevel = new SimpleStringProperty(maxLevel);
        this.psi = new SimpleStringProperty(psi);
        this.cybernetic = new SimpleStringProperty(cybernetic);
    }

    public String getId() { return id.get(); }

    public void setId(String sId) { id.set(sId); }

    public String getAdvantage() { return advantage.get(); }

    public void setAdvantage(String sAdvantage) { advantage.set(sAdvantage); }

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

    public String getCost() { return cost.get(); }

    public void setCost(String sCost) { cost.set(sCost); }

    public String getDescription() { return description.get(); }

    public void setDescription(String sDescription) { description.set(sDescription); }

    public String getMaxLevel() { return maxLevel.get(); }

    public void setMaxLevel(String sMaxLevel) { maxLevel.set(sMaxLevel); }

    public String getOldLevel() { return oldLevel.get(); }

    public void setOldLevel(String sOldLevel) { oldLevel.set(sOldLevel); }

    public String getPsi() { return psi.get(); }

    public void setPsi(String sPsi) { psi.set(sPsi); }

    public String getCybernetic() { return cybernetic.get(); }

    public void setCybernetic(String sCybernetic) { cybernetic.set(sCybernetic); }
}
