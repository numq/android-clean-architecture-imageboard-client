package com.numq.androidgrpcimageboard.data.thread.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.numq.androidgrpcimageboard.platform.constant.AppConstants

@Entity(tableName = AppConstants.Database.TABLE_THREADS)
data class ThreadDataModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "boardId")
    val boardId: String,
    @ColumnInfo(name = "postCount")
    val postCount: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long,
    @ColumnInfo(name = "bumpedAt")
    val bumpedAt: Long
)