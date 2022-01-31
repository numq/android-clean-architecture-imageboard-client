package com.numq.androidgrpcimageboard.data.post.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.numq.androidgrpcimageboard.platform.constant.AppConstants

@Entity(tableName = AppConstants.Database.TABLE_POSTS)
data class PostDataModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "threadId")
    val threadId: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "quoteIds")
    var quoteIds: List<String>,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "videoUrl")
    val videoUrl: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long
)