package com.numq.androidgrpcimageboard.data.board.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.numq.androidgrpcimageboard.platform.constant.AppConstants

@Entity(tableName = AppConstants.Database.TABLE_BOARDS)
data class BoardDataModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "isAdult")
    val isAdult: Boolean
)