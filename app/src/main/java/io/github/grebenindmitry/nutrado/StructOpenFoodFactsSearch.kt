package io.github.grebenindmitry.nutrado

import com.google.gson.annotations.SerializedName
import java.util.*

class StructOpenFoodFactsSearch(@field:SerializedName("products") var products: Vector<StructProduct>)