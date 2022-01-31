package com.numq.androidgrpcimageboard.platform.di

import android.content.Context
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import io.grpc.android.AndroidChannelBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppChannel(@ApplicationContext context: Context): ManagedChannel {
        return AndroidChannelBuilder
            .forAddress(AppConstants.Network.GRPC_ADDRESS, AppConstants.Network.GRPC_PORT)
            .usePlaintext()
            .context(context)
            .build()
    }

}