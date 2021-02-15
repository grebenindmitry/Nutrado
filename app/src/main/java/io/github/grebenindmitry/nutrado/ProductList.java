package io.github.grebenindmitry.nutrado;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ProductList {
    @PrimaryKey private final int id;
    private String name;
    private String description;

    public ProductList(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
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
