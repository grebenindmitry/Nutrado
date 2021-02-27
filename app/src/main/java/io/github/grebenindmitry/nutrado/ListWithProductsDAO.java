package io.github.grebenindmitry.nutrado;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface ListWithProductsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addProductToList(ProductListCrossref... productListCrossref);

    @Transaction
    @Query("SELECT * FROM lists")
    List<ListWithProducts> getListsWithProducts();

    @Transaction
    @Query("SELECT * FROM lists WHERE listId=:id")
    ListWithProducts getListWithProducts(int id);

    @Delete void disconnect(final ProductListCrossref list);
}
