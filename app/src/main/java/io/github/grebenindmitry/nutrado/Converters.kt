package io.github.grebenindmitry.nutrado

import android.graphics.Color
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun nutrimentsToFloat(productNutriments: StructNutriments): Float { return productNutriments.energy }

    @TypeConverter
    fun floatToNutriments(energy: Float): StructNutriments { return StructNutriments(energy) }

    @TypeConverter
    fun intToColor(color: Int): Color { return Color.valueOf(color) }

    @TypeConverter
    fun colorToInt(color: Color): Int { return color.toArgb() }
}