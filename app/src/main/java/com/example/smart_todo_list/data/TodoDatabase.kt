package com.example.smart_todo_list.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Az adatbázis központi osztálya. 
 * Itt adjuk meg a táblákat (entities) és a verziószámot.
 * Ha változtatunk a TodoItem szerkezetén, a verziót (version) növelni kell!
 */
@Database(entities = [TodoItem::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao


    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        //
        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                // Ez a sor segít, ha változik a tábla szerkezete: 
                // törli a régit és újat kezd, megelőzve az összeomlást.
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
