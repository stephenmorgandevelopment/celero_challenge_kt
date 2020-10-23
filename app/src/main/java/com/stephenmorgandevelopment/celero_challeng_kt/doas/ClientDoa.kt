package com.stephenmorgandevelopment.celero_challeng_kt.doas

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: Client)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clients: List<Client>)

    @Query("DELETE FROM Client")
    suspend fun deleteAll()

    @Query("SELECT identifier, name, serviceReason FROM client ORDER BY visitOrder asc")
    fun loadSimpleClients(): LiveData<List<SimpleClient>>

    @Query("SELECT * FROM client WHERE identifier = :identifier")
    fun load(identifier: Long): Client

    @Query("SELECT * FROM client WHERE identifier = :identifier")
    fun loadLive(identifier: Long): LiveData<Client>
}