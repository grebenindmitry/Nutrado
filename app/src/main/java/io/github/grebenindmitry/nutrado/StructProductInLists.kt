package io.github.grebenindmitry.nutrado

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class StructProductInLists(
    @Embedded
    var product: StructProduct
) {

    @Relation(
        parentColumn = "productId",
        entity = StructList::class,
        entityColumn = "listId",
        associateBy = Junction(
            value = CrossrefProductList::class,
            parentColumn = "productId",
            entityColumn = "listId"))
    var lists: List<StructList>? = null
}