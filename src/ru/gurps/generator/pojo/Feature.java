package ru.gurps.generator.pojo;

public class Feature {
    private int id;
    private boolean advantage;
    private String title;
    private String titleEn;
    private String type;
    private int cost;
    private String description;
    private int max_level;
    private boolean psi;
    private boolean cybernetic;

        public Feature(int id, String title, String titleEn, String type, int cost, String description, int max_level, boolean psi, boolean cybernetic) {
        this.id = id;
        this.title = title;
        this.titleEn = titleEn;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.max_level = max_level;
        this.psi = psi;
        this.cybernetic = cybernetic;
    }

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
}
