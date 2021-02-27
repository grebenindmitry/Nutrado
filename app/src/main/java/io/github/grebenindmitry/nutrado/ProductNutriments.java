package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

//Currently stub, could be expanded if the application gets support for more nutriments
public class ProductNutriments {
    @SerializedName("energy-kcal")
    private float energy;

    public ProductNutriments() {
        this.energy = -1;
    }

    public ProductNutriments(float energy) {
        this.energy = energy;
    }

    public String getPrintEnergy() {
        return this.energy < 0 ? "N/a": String.valueOf(this.energy);
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
}
