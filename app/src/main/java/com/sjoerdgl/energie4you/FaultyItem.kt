package com.sjoerdgl.energie4you

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faulty_item")
data class FaultyItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "photo") var photo: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
