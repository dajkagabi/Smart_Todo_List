package com.example.smart_todo_list.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Ez az adatmodell osztály (Entity), ami meghatározza, hogyan tároljuk a teendőket az adatbázisban.
 * Minden egyes példány egy sort jelent a "todo_items" táblában.
 *
 * Bővítettük:
 * - category: A feladat típusa (pl. Munka, Otthon)
 * - timestamp: Mikor lett létrehozva vagy mi a határidő (időpont tárolása)
 */
@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    // Az elem egyedi azonosítója, amit a Room automatikusan generál
    val id: Int = 0,
    // A teendő szövege (pl. "Vegyél tejet")
    val title: String,
    // Készen van-e a feladat? (pipa állapot)
    val isDone: Boolean = false,
    // Alapértelmezett kategória
    val category: String = "Általános",
    // Aktuális idő mentése Long formátumban
    val timestamp: Long = System.currentTimeMillis()
)
