package io.github.grebenindmitry.nutrado;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ListWithProducts {
    @Embedded public ProductList list;
    @Relation(
            parentColumn = "listId",
            entity = Product.class,
            entityColumn = "productId",
            associateBy = @Junction(
                    value = ProductListCrossref.class,
                    parentColumn = "listId",
                    entityColumn = "productId"
            )
    )
    public List<Product> products;
}
