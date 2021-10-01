package io.github.grebenindmitry.nutrado

import androidx.room.Dao
import androidx.room.ColumnInfo
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DAOProductInLists {
    @get:ColumnInfo
    @get:Query("SELECT * FROM products")
    @get:Transaction
    val productsInLists: List<StructProductInLists>

    @Transaction
    @Query("SELECT * FROM products WHERE productId=:id")
    fun getProductInLists(id: String): StructProductInLists?
}