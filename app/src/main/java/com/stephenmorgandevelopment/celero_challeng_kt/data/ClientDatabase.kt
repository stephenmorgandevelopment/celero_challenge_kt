package com.stephenmorgandevelopment.celero_challeng_kt.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client

@Database(entities = [Client::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverter::class)
abstract class ClientDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
}