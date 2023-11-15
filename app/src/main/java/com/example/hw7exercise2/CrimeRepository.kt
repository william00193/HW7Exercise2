package com.example.hw7exercise2

import android.content.Context
import androidx.room.Room
import com.example.hw7exercise2.database.CrimeDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*


private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context:Context,
    private val coroutineScope : CoroutineScope= GlobalScope
) {

    private val database: CrimeDataBase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDataBase::class.java,
            DATABASE_NAME
        )
        .createFromAsset(DATABASE_NAME)
        .build()

   suspend fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    fun updateCrime(crime:Crime){
        coroutineScope.launch {

            database.crimeDao().updateCrime(crime)
        }

    }


    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE
                ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}