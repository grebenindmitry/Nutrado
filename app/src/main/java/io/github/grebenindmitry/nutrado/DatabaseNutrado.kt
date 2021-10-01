package io.github.grebenindmitry.nutrado

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(
    entities = [StructProduct::class, StructList::class, CrossrefProductList::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class DatabaseNutrado : RoomDatabase() {
    abstract fun listDAO(): DAOList
    abstract fun productDAO(): DAOProduct
    abstract fun listWithProductsDAO(): DAOListWithProducts
    abstract fun productInListsDAO(): DAOProductInLists

    companion object {
        @Volatile
        private var INSTANCE: DatabaseNutrado? = null
        fun getDatabase(context: Context): DatabaseNutrado? {
            if (INSTANCE == null) {
                synchronized(DatabaseNutrado::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseNutrado::class.java,
                            "my_db").addCallback(initCallback).build()
                    }
                }
            }
            return INSTANCE
        }

        private val initCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    val listDAO = INSTANCE!!.listDAO()
                    //create dummy list for offline data
                    listDAO.insert(StructList(
                        "Offline",
                        "Data downloaded upon last execution of the app",
                        Color.valueOf(0x7FE53935),
                        R.drawable.ic_outline_wifi_off_24,
                        -1))
                }
            }
        }
    }
}