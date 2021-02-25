package io.github.grebenindmitry.nutrado;

import android.content.Context;
import android.graphics.Color;
import android.telecom.Call;

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
    public abstract MyDAO myDAO();
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
                //create dummy list for online items
                final ListDAO listDAO = INSTANCE.listDAO();
                listDAO.insert(new ProductList(
                        -1,
                        "Online",
                        "Products loaded from OpenFoodFacts API",
                        Color.valueOf(Color.GREEN),
                        R.drawable.ic_outline_home_24));
            });
        }
    };
}
