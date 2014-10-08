package ru.gurps.generator.pojo;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@DatabaseTable(tableName = "features")
public class Feature {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private boolean advantage;
    @DatabaseField
    private String title;
    @DatabaseField(columnName = "title_en")
    private String titleEn;
    @DatabaseField
    private String type;
    @DatabaseField
    private int cost;
    @DatabaseField
    private String description;
    @DatabaseField
    private int max_level;
    @DatabaseField
    private boolean psi;
    @DatabaseField
    private boolean cybernetic;
    @ForeignCollectionField
    private ForeignCollection<Additional> additionals;

    //    public Feature(int id, boolean advantage, String title, String titleEn, String type, int cost, String description, int max_level, boolean psi, boolean cybernetic) {
//        this.id = id;
//        this.advantage = advantage;
//        this.title = title;
//        this.titleEn = titleEn;
//        this.type = type;
//        this.cost = cost;
//        this.description = description;
//        this.max_level = max_level;
//        this.psi = psi;
//        this.cybernetic = cybernetic;
//    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public boolean isAdvantage() {
        return advantage;
    }

    public void setAdvantage(boolean advantage) {
        this.advantage = advantage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMax_level() {
        return max_level;
    }

    public void setMax_level(int max_level) {
        this.max_level = max_level;
    }

    public boolean isPsi() {
        return psi;
    }

    public void setPsi(boolean psi) {
        this.psi = psi;
    }

    public boolean isCybernetic() {
        return cybernetic;
    }

    public void setCybernetic(boolean cybernetic) {
        this.cybernetic = cybernetic;
    }

    public ForeignCollection<Additional> getAdditionals() { return additionals; }

    public void setAdditionals(ForeignCollection<Additional> additionals) { this.additionals = additionals; }
}
