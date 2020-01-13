package com.sjoerdgl.energie4you

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface FaultyItemDao {
    @Query("SELECT id, name, description, photo from faulty_item ORDER BY id DESC")
    fun getFaultyItems(): LiveData<List<FaultyItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(faultyItem: FaultyItem)

    @Update
    fun update(faultyItem: FaultyItem)

    @Delete
    fun delete(faultyItem: FaultyItem)

    @Query("SELECT * from faulty_item where id = :id LIMIT 1")
    fun findById(id: Int): LiveData<FaultyItem>
}