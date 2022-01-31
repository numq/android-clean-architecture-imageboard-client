package com.numq.androidgrpcimageboard.platform.di

import android.content.Context
import androidx.room.Room
import com.numq.androidgrpcimageboard.data.AppDatabase
import com.numq.androidgrpcimageboard.data.board.BoardData
import com.numq.androidgrpcimageboard.data.board.local.BoardDao
import com.numq.androidgrpcimageboard.data.board.remote.BoardApi
import com.numq.androidgrpcimageboard.data.board.remote.BoardService
import com.numq.androidgrpcimageboard.data.post.PostData
import com.numq.androidgrpcimageboard.data.post.local.PostDao
import com.numq.androidgrpcimageboard.data.post.remote.PostApi
import com.numq.androidgrpcimageboard.data.post.remote.PostService
import com.numq.androidgrpcimageboard.data.thread.ThreadData
import com.numq.androidgrpcimageboard.data.thread.local.ThreadDao
import com.numq.androidgrpcimageboard.data.thread.remote.ThreadApi
import com.numq.androidgrpcimageboard.data.thread.remote.ThreadService
import com.numq.androidgrpcimageboard.domain.repository.BoardRepository
import com.numq.androidgrpcimageboard.domain.repository.PostRepository
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppConstants.Database.DB_NAME)
            .build()

    @Provides
    @Singleton
    fun provideBoardRepository(repository: BoardData): BoardRepository = repository

    @Provides
    @Singleton
    fun provideThreadRepository(repository: ThreadData): ThreadRepository = repository

    @Provides
    @Singleton
    fun providePostRepository(repository: PostData): PostRepository = repository

    @Provides
    @Singleton
    fun provideBoardDao(database: AppDatabase): BoardDao = database.boardDao()

    @Provides
    @Singleton
    fun provideThreadDao(database: AppDatabase): ThreadDao = database.threadDao()

    @Provides
    @Singleton
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()

    @Provides
    @Singleton
    fun provideBoardService(service: BoardService): BoardApi = service

    @Provides
    @Singleton
    fun provideThreadService(service: ThreadService): ThreadApi = service

    @Provides
    @Singleton
    fun providePostService(service: PostService): PostApi = service
}