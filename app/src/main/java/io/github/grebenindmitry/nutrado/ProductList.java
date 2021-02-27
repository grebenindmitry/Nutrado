package io.github.grebenindmitry.nutrado;

import android.graphics.Color;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ProductList {
    @PrimaryKey(autoGenerate = true) private int listId;
    private String name;
    private String description;
    private Color color;
    private int icon;

    public ProductList(String name, String description, Color color, int icon) {
        this.name = name;
        this.description = description;
        this.color = Color.valueOf(color.red(), color.green(), color.blue(), .5f);
        this.icon = icon;
    }

    @Ignore
    public ProductList(int listId, String name, String description, Color color, int icon) {
        this.listId = listId;
        this.name = name;
        this.description = description;
        this.color = Color.valueOf(color.red(), color.green(), color.blue(), .5f);
        this.icon = icon;
    }


    //getters
    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getDarkColor() {
        return Color.valueOf(this.color.red() * 0.5f, this.color.green() * 0.5f, this.color.blue() * 0.5f, this.color.alpha());
    }

    public int getIcon() {
        return icon;
    }
    //end getters

    //setters
    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(Color color) {
        this.color = Color.valueOf(color.red(), color.green(), color.blue(), 0x55);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    //end setters
}
