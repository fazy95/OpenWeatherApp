package com.coding.openweatherapp.di

import com.coding.openweatherapp.data.repository.RepositoryImpl
import com.coding.openweatherapp.data.source.localDataSource.LocalDataSource
import com.coding.openweatherapp.data.source.localDataSource.LocalDataSourceImpl
import com.coding.openweatherapp.data.source.remoteDataSource.RemoteDataSource
import com.coding.openweatherapp.data.source.remoteDataSource.RemoteDataSourceImpl
import com.coding.openweatherapp.domain.repository.Repository
import com.coding.weatherapp.BuildConfig
import com.coding.weatherapp.data.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // Retrofit
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService
    single {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl(get())
    }
    single<LocalDataSource> {
        LocalDataSourceImpl(androidContext())
    }
    single<Repository> {
        RepositoryImpl(get(), get())
    }
}