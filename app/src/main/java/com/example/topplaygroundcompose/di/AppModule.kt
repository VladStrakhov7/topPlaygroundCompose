package com.example.topplaygroundcompose.di

import android.content.Context
import com.example.topplaygroundcompose.data.api.OpenLigaApi
import com.example.topplaygroundcompose.data.local.AppDatabase
import com.example.topplaygroundcompose.data.local.MatchDao
import com.example.topplaygroundcompose.data.repository.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openligadb.de/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideOpenLigaApi(retrofit: Retrofit): OpenLigaApi {
        return retrofit.create(OpenLigaApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }
    
    @Provides
    @Singleton
    fun provideMatchDao(database: AppDatabase): MatchDao {
        return database.matchDao()
    }
    
    @Provides
    @Singleton
    fun provideMatchRepository(
        api: OpenLigaApi,
        matchDao: MatchDao
    ): MatchRepository {
        return MatchRepository(api, matchDao)
    }
}

