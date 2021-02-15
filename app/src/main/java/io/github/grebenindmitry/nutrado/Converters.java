package io.github.grebenindmitry.nutrado;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public float nutrimentsToFloat(ProductNutriments productNutriments) {
        return productNutriments.getEnergy();
    }

    @TypeConverter
    public ProductNutriments floatToNutriments(float energy) {
        return new ProductNutriments(energy);
    }
}
