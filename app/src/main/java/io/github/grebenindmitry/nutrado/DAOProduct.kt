package io.github.grebenindmitry.nutrado

import androidx.room.*

@Dao
interface DAOProduct {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg products: StructProduct)

    @get:Query("SELECT * FROM products")
    val products: List<StructProduct>?

    @Query("SELECT * FROM products WHERE productId=:id")
    fun getProduct(id: String): StructProduct?

    @Update
    fun update(product: StructProduct)

    @Delete
    fun delete(product: StructProduct)
}