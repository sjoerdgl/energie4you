package com.sjoerdgl.energie4you

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "defective_item")
class DefectiveItem {

    @PrimaryKey(autoGenerate = true)
    private val id: Int = 0

    @NonNull
    private val name: String

    private val description: String
    //..other fields, getters, setters
}

