package io.github.grebenindmitry.nutrado;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"productId", "listId"})
public class ProductListCrossref {
    @NonNull
    @ForeignKey(entity = Product.class, parentColumns = "productId", childColumns = "productId", onDelete = ForeignKey.CASCADE)
    public String productId;
    @ForeignKey(entity = ProductList.class, parentColumns = "listId", childColumns = "listId", onDelete = ForeignKey.CASCADE)
    public int listId;

    public ProductListCrossref(@NonNull String productId, int listId) {
        this.productId = productId;
        this.listId = listId;
    }
}
