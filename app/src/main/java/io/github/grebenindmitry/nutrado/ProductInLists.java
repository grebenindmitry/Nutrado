package io.github.grebenindmitry.nutrado;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import java.util.List;

public class ProductInLists {
    @Embedded public Product product;
    @Relation(
            parentColumn = "productId",
            entity = ProductList.class,
            entityColumn = "listId",
            associateBy = @Junction(
                    value = ProductListCrossref.class,
                    parentColumn = "productId",
                    entityColumn = "listId"
            )
    )
    public List<ProductList> lists;
}
