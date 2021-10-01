package io.github.grebenindmitry.nutrado

import android.graphics.Color
import android.graphics.ColorSpace
import androidx.core.graphics.convertTo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
class StructList(
    var name: String,
    var description: String,
    val color: Color,
    var icon: Int,
    @PrimaryKey(autoGenerate = true) var listId: Int = 1
) {
    val selColor: Color
        get() = Color.valueOf(color.red(), color.green(), color.blue(), 1F)

    val deselColor: Color
        get() = Color.valueOf(color.red(), color.green(), color.blue(), 0.5F)
}