package ru.gurps.generator.pojo;

public class Additional {

    private int id;
    private int featureId;
    private String title;
    private String titleEn;
    private String description;
    private int cost;
    private int maxLevel;

    public Additional(int id, int featureId, String title, String titleEn, String description, int cost, int maxLevel, boolean improvement) {
        this.id = id;
        this.featureId = featureId;
        this.title = title;
        this.titleEn = titleEn;
        this.description = description;
        this.cost = cost;
        this.maxLevel = maxLevel;
        this.improvement = improvement;
    }

    public boolean isImprovement() {
        return improvement;
    }

    public void setImprovement(boolean improvement) {
        this.improvement = improvement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    private boolean improvement;

}
