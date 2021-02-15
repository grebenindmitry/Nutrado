package io.github.grebenindmitry.nutrado;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface ProductDAO {
    @Insert
    void insert(Product... products);

    @Update
    void update(final Product product);

    @Delete
    void delete(final Product product);
}
