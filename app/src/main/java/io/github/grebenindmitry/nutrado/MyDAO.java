package io.github.grebenindmitry.nutrado;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDAO {
    @Insert void insertProduct(Product... products);
    @Insert void insertList(ProductList... lists);

    @Transaction
    @Query("SELECT * FROM lists")
    List<ListsWithProducts> getListsWithProducts();

    @Query("SELECT * FROM lists")
    List<ProductList> getLists();

    @Update void updateProduct(final Product product);
    @Update void updateList(final ProductList list);

    @Delete void deleteProduct(final Product product);
    @Delete void deleteList(final ProductList list);
}
