package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("product_name")
    private String name;
    @SerializedName("nutrition_grades")
    private String grade;
    @SerializedName("image_front_thumb_url")
    private String thumbUrl;
    @SerializedName("nutriments")
    private ProductNutriments nutriments;

    public ProductData() {
        this.name = "";
        this.grade = "";
        this.thumbUrl = "";
        this.nutriments = new ProductNutriments();
    }

    public ProductData(String name, int energy, String score, String thumbUrl) {
        this.name = name;
        this.grade = score;
        this.thumbUrl = thumbUrl;
        nutriments.setEnergy(energy);
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getPrintEnergy() {
        return nutriments.getPrintEnergy();
    }

    public void setEnergy(float energy) {
        nutriments.setEnergy(energy);
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
