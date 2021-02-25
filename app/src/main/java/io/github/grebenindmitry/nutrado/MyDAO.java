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
public interface MyDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Product... products);
    @Insert void insertList(ProductList... lists);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addProductToList(ProductListCrossref... productListCrossref);

    @Transaction
    @Query("SELECT * FROM lists")
    List<ListWithProducts> getListsWithProducts();

    @Transaction
    @Query("SELECT * FROM lists WHERE listId=:id")
    ListWithProducts getListWithProducts(int id);

    @Query("SELECT * FROM lists")
    List<ProductList> getLists();

    @Query("SELECT * FROM products")
    List<Product> getProducts();

    @Update void updateProduct(final Product product);
    @Update void updateList(final ProductList list);

    @Delete void deleteProduct(final Product product);
    @Delete void deleteList(final ProductList list);
}
