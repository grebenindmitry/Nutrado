package io.github.grebenindmitry.nutrado

import com.google.gson.annotations.SerializedName

//Currently stub, could be expanded if the application gets support for more nutriments
class StructNutriments {
    @SerializedName("energy-kcal")
    var energy: Float

    constructor() { energy = -1F }

    constructor(energy: Float) { this.energy = energy }

    val printEnergy: String
        get() = if (energy < 0) "N/a" else energy.toString()
}