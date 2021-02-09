package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("product_name")
    private String name;
    @SerializedName("nutrition_grades")
    private String score;
    @SerializedName("image_front_thumb_url")
    private String thumbUrl;
    @SerializedName("nutriments")
    private ProductNutriments nutriments;

    public ProductData(String name, int energy, String score, String thumbUrl) {
        this.name = name;
        this.score = score;
        this.thumbUrl = thumbUrl;
        nutriments.setEnergy(energy);
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public float getEnergy() {
        return nutriments.getEnergy();
    }

    public void setEnergy(float energy) {
        nutriments.setEnergy(energy);
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
