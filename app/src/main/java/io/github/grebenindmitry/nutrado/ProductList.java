package io.github.grebenindmitry.nutrado;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ProductList {
    @PrimaryKey(autoGenerate = true) private int listId;
    private String name;
    private String description;

    public ProductList(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Ignore
    public ProductList(int listId, String name, String description) {
        this.listId = listId;
        this.name = name;
        this.description = description;
    }

    public void setListId(int listId) {
        this.listId = listId;
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
