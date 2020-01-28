package com.sjoerdgl.energie4you

import androidx.lifecycle.LiveData

class FaultyItemRepository(private val faultyItemDao: FaultyItemDao) {
    val allFaultyItems: LiveData<List<FaultyItem>> = faultyItemDao.getFaultyItems()

    suspend fun create(faultyItem: FaultyItem) {
        faultyItemDao.create(faultyItem)
    }

    suspend fun update(faultyItem: FaultyItem) {
        faultyItemDao.update(faultyItem)
    }

    fun findById(id: Int): LiveData<FaultyItem> {
        return faultyItemDao.findById(id)
    }
}