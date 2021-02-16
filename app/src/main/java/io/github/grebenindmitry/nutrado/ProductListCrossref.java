package io.github.grebenindmitry.nutrado;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

@Entity(tableName = "products_to_lists", primaryKeys = {"productId", "listId"})
public class ProductListCrossref {
    public long productId;
    public int listId;
}
