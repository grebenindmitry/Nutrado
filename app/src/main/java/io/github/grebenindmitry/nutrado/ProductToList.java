package io.github.grebenindmitry.nutrado;

import androidx.room.Entity;

@Entity(tableName = "products_to_lists", primaryKeys = {"productId", "listId"})
public class ProductToList {
    private long productId;
    private int listId;

    public ProductToList(int productId, int listId) {
        this.productId = productId;
        this.listId = listId;
    }

    public long getProductId() {
        return productId;
    }

    public int getListId() {
        return listId;
    }
}
