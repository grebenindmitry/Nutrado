package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class ProductNutriments {
    @SerializedName("energy-kcal")
    private float energy;

    public ProductNutriments() {
        this.energy = -1;
    }

    public ProductNutriments(int energy) {
        this.energy = energy;
    }

    public String getPrintEnergy() {
        return this.energy == -1 ? "N/a": String.valueOf(this.energy);
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
}
