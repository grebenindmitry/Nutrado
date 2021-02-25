package io.github.grebenindmitry.nutrado;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

@Entity(tableName = "products")
public class Product {
    @SerializedName("id")
    @PrimaryKey @NonNull
    private String productId;
    @SerializedName("product_name")
    private String name;
    @SerializedName("nutrition_grades")
    private String grade;
    @SerializedName("image_front_small_url")
    private String thumbUrl;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("nutriments")
    private ProductNutriments nutriments;

    public Product() {
        this.productId = "-1";
        this.name = "";
        this.grade = "";
        this.thumbUrl = "";
        this.imageUrl = "";
        this.nutriments = new ProductNutriments();
    }

    public Product(String id, String name, float energy, String score, String thumbUrl, String imageUrl) {
        this.productId = id;
        this.name = name;
        this.grade = score;
        this.thumbUrl = thumbUrl;
        this.imageUrl = imageUrl;
        this.nutriments = new ProductNutriments();
        nutriments.setEnergy(energy);
    }

    //getters
    @NonNull
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getEnergy() {
        return nutriments.getEnergy();
    }

    public String getPrintEnergy() {
        return nutriments.getPrintEnergy();
    }

    public ProductNutriments getNutriments() {
        return nutriments;
    }
    //end getters

    //setters
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNutriments(ProductNutriments nutriments) {
        this.nutriments = nutriments;
    }

    public void setEnergy(float energy) {
        nutriments.setEnergy(energy);
    }
    //end setters

    //methods
    public boolean isFull() {
        return (!productId.equals("-1") && !name.equals("") && !grade.equals("") && !thumbUrl.equals("") && !getPrintEnergy().equals("N/a"));
    }
    //end methods
}
