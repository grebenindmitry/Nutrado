package io.github.grebenindmitry.nutrado

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["productId", "listId"],
    foreignKeys = [ForeignKey(
        entity = StructProduct::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = StructList::class,
        parentColumns = arrayOf("listId"),
        childColumns = arrayOf("listId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("listId")]
)
class CrossrefProductList(val productId: String, val listId: Int)