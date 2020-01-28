package com.sjoerdgl.energie4you

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FaultyItem::class], version = 2, exportSchema = false)
abstract class FaultyItemDatabase : RoomDatabase() {

    abstract fun faultyItemDao(): FaultyItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: FaultyItemDatabase? = null

        fun getDatabase(context: Context): FaultyItemDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaultyItemDatabase::class.java,
                    "faulty_item_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
