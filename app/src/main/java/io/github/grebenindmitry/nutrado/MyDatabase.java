package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {Product.class, ProductList.class, ProductListCrossref.class},
        version = 1,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MyDatabase extends RoomDatabase {
    public abstract ListDAO listDAO();
    public abstract ProductDAO productDAO();
    public abstract ListWithProductsDAO listWithProductsDAO();
    public abstract ProductInListsDAO productInListsDAO();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "my_db").addCallback(initCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback initCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                final ListDAO listDAO = INSTANCE.listDAO();
                //create dummy list for offline data
                listDAO.insert(new ProductList(
                        -2,
                        "Offline",
                        "Data downloaded upon last execution of the app",
                        Color.valueOf(0x7FE53935),
                        R.drawable.ic_outline_wifi_off_24));
                //create dummy list for online items
                listDAO.insert(new ProductList(
                        -1,
                        "Online",
                        "Products loaded from OpenFoodFacts API",
                        Color.valueOf(0x7F4CAF50),
                        R.drawable.ic_outline_wifi_24));
            });
        }
    };
}
