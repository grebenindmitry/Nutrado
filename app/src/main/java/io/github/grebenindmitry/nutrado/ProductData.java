package io.github.grebenindmitry.nutrado;

public class ProductData {
    private String name;
    private int energy;
    private String score;
    private String thumbUrl;

    public ProductData(String name, int energy, String score, String thumbUrl) {
        this.name = name;
        this.energy = energy;
        this.score = score;
        this.thumbUrl = thumbUrl;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
