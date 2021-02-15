package io.github.grebenindmitry.nutrado;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey private long id;
    @SerializedName("product_name")
    private String name;
    @SerializedName("nutrition_grades")
    private String grade;
    @SerializedName("image_front_thumb_url")
    private String thumbUrl;
    @SerializedName("nutriments")
    private ProductNutriments nutriments;

    public Product() {
        this.id = -1;
        this.name = "";
        this.grade = "";
        this.thumbUrl = "";
        this.nutriments = new ProductNutriments();
    }

    public Product(String name, int energy, String score, String thumbUrl) {
        this.name = name;
        this.grade = score;
        this.thumbUrl = thumbUrl;
        nutriments.setEnergy(energy);
    }

    public Product(int id, String name, int energy, String score, String thumbUrl) {
        this.id = id;
        this.name = name;
        this.grade = score;
        this.thumbUrl = thumbUrl;
        nutriments.setEnergy(energy);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public ProductNutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(ProductNutriments nutriments) {
        this.nutriments = nutriments;
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
