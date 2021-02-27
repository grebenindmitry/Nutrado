package io.github.grebenindmitry.nutrado;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface ProductInListsDAO {
    @Transaction
    @Query("SELECT * FROM products")
    @ColumnInfo()
    List<ProductInLists> getProductsInLists();

    @Transaction
    @Query("SELECT * FROM products WHERE productId=:id")
    ProductInLists getProductInLists(String id);
}
