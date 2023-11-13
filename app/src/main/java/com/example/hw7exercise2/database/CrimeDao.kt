package com.example.hw7exercise2.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.hw7exercise2.Crime
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Crime

    @Update
     fun updateCrime(crime: Crime)

}