package io.github.grebenindmitry.nutrado;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ProductList {
    @PrimaryKey private final int listId;
    private String name;
    private String description;

    public ProductList(int listId, String name, String description) {
        this.listId = listId;
        this.name = name;
        this.description = description;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
