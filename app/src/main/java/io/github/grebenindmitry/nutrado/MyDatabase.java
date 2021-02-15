package io.github.grebenindmitry.nutrado;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Product.class, ProductList.class, ProductToList.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class MyDatabase extends RoomDatabase {
    public abstract ProductDAO productDAO();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "my_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
