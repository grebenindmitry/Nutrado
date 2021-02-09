package io.github.grebenindmitry.nutrado;

import com.google.gson.annotations.SerializedName;

public class ProductNutriments {
    @SerializedName("energy-kcal")
    private float energy;

    public ProductNutriments(int energy) {
        this.energy = energy;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
}
