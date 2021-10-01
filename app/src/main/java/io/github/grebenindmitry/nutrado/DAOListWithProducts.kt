package io.github.grebenindmitry.nutrado

import androidx.room.*

@Dao
interface DAOListWithProducts {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProductToList(vararg productListCrossref: CrossrefProductList)

    @get:Query("SELECT * FROM lists")
    @get:Transaction
    val listsWithProducts: List<StructListWithProducts>

    @Transaction
    @Query("SELECT * FROM lists WHERE listId=:id")
    fun getListWithProducts(id: Int): StructListWithProducts?

    @Delete
    fun disconnect(list: CrossrefProductList)
}