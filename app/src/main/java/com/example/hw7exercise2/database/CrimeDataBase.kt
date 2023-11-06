package com.example.hw7exercise2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hw7exercise2.Crime
import com.example.hw7exercise2.database.CrimeTypeConverters

@Database(entities = [Crime:: class], version=1, exportSchema = false)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDataBase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao


}