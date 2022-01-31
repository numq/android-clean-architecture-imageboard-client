package com.numq.androidgrpcimageboard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.numq.androidgrpcimageboard.data.board.local.BoardDao
import com.numq.androidgrpcimageboard.data.board.local.BoardDataModel
import com.numq.androidgrpcimageboard.data.post.local.PostDao
import com.numq.androidgrpcimageboard.data.post.local.PostDataModel
import com.numq.androidgrpcimageboard.data.thread.local.ThreadDao
import com.numq.androidgrpcimageboard.data.thread.local.ThreadDataModel

@Database(
    entities = [BoardDataModel::class, ThreadDataModel::class, PostDataModel::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun boardDao(): BoardDao
    abstract fun threadDao(): ThreadDao
    abstract fun postDao(): PostDao
}

object Converters {

    @TypeConverter
    fun listToString(data: List<String>): String = data.joinToString()

    @TypeConverter
    fun stringToList(data: String): List<String> = data.split(',')

}