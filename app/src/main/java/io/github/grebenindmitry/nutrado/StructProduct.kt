package io.github.grebenindmitry.nutrado

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import androidx.room.PrimaryKey

@Entity(tableName = "products")
class StructProduct {
    @SerializedName("id")
    @PrimaryKey
    var productId: String

    @SerializedName("product_name")
    var name: String

    @SerializedName("nutrition_grades")
    var grade: String

    @SerializedName("image_front_small_url")
    var thumbUrl: String

    @SerializedName("image_url")
    var imageUrl: String

    @SerializedName("nutriments")
    var nutriments: StructNutriments

    @SerializedName("ingredients_text")
    var ingredients: String

    constructor() {
        productId = "-1"
        name = ""
        grade = ""
        thumbUrl = ""
        imageUrl = ""
        nutriments = StructNutriments()
        ingredients = ""
    }

    constructor(
        id: String,
        name: String,
        energy: Float,
        score: String,
        thumbUrl: String,
        imageUrl: String,
        ingredients: String
    ) {
        productId = id
        this.name = name
        grade = score
        this.thumbUrl = thumbUrl
        this.imageUrl = imageUrl
        nutriments = StructNutriments()
        nutriments.energy = energy
        this.ingredients = ingredients
    }

    var energy: Float
        get() = nutriments.energy
        set(energy) { nutriments.energy = energy }
    val printEnergy: String
        get() = nutriments.printEnergy
    val isFull: Boolean
        get() = productId != "-1" && name != "" && grade != "" && thumbUrl != "" && printEnergy != "N/a"
}