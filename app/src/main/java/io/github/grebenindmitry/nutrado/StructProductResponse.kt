package io.github.grebenindmitry.nutrado

import com.google.gson.annotations.SerializedName

class StructProductResponse(
    @field:SerializedName("product") var product: StructProduct,
    @field:SerializedName("status") var status: Int)