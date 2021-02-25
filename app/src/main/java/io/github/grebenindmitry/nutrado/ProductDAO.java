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
public interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Product... products);

    @Query("SELECT * FROM products")
    List<Product> getProducts();

    @Query("SELECT * FROM products WHERE productId=:id")
    Product getProduct(String id);

    @Update void update(final Product product);

    @Delete void delete(final Product product);
}
