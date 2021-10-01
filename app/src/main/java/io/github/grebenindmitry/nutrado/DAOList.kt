package io.github.grebenindmitry.nutrado

import androidx.room.*

@Dao
interface DAOList {
    @Insert
    fun insert(vararg lists: StructList)

    @get:Query("SELECT * FROM lists")
    val lists: List<StructList>?

    @Query("SELECT * FROM lists WHERE listId=:id")
    fun getList(id: Int): StructList?

    @Update
    fun update(list: StructList)

    @Delete
    fun delete(list: StructList)
}