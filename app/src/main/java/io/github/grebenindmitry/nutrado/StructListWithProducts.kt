package io.github.grebenindmitry.nutrado

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class StructListWithProducts(
    @Embedded
    var list: StructList
) {

    @Relation(
        parentColumn = "listId",
        entity = StructProduct::class,
        entityColumn = "productId",
        associateBy = Junction(
            value = CrossrefProductList::class,
            parentColumn = "listId",
            entityColumn = "productId"))
    var products: List<StructProduct>? = null
}