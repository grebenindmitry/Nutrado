package io.github.grebenindmitry.nutrado;

import android.graphics.Color;
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

    @TypeConverter
    public Color intToColor(int color) {
        return Color.valueOf(color);
    }

    @TypeConverter
    public int colorToInt(Color color) {
        return color.toArgb();
    }
}
