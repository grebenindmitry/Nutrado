package io.github.grebenindmitry.nutrado;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ListDAO {
    @Insert void insert(ProductList... lists);

    @Query("SELECT * FROM lists")
    List<ProductList> getLists();

    @Query("SELECT * FROM lists WHERE listId=:id")
    ProductList getList(int id);

    @Update void update(final ProductList list);

    @Delete void delete(final ProductList list);
}
