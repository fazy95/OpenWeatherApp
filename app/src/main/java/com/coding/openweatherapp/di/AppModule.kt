package com.coding.openweatherapp.di

import com.coding.openweatherapp.data.repository.LocationRepositoryImpl
import com.coding.openweatherapp.BuildConfig
import com.coding.openweatherapp.data.repository.WeatherRepositoryImpl
import com.coding.openweatherapp.data.source.localDataSource.LocalDataSource
import com.coding.openweatherapp.data.source.localDataSource.LocalDataSourceImpl
import com.coding.openweatherapp.data.source.remoteDataSource.RemoteDataSource
import com.coding.openweatherapp.data.source.remoteDataSource.RemoteDataSourceImpl
import com.coding.openweatherapp.domain.repository.WeatherRepository
import com.coding.openweatherapp.domain.usecase.GetWeatherForCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLastSearchedCityUseCase
import com.coding.openweatherapp.domain.usecase.GetWeatherForLocationUseCase
import com.coding.openweatherapp.data.api.ApiService
import com.coding.openweatherapp.domain.usecase.GetCheckPermissionUseCase
import com.coding.openweatherapp.domain.usecase.GetConvertWeatherTemperatureUseCase
import com.coding.openweatherapp.domain.usecase.GetCurrentLocationUseCase
import com.coding.openweatherapp.ui.viewmodel.WeatherViewModel
import com.coding.openweatherapp.domain.repository.LocationRepository
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // Retrofit
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
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
    single { get<Retrofit>().create(ApiService::class.java) }

    // Data Sources
    single<RemoteDataSource> { RemoteDataSourceImpl(get()) }
    single<LocalDataSource> { LocalDataSourceImpl(androidContext()) }

    // Location Services
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    // Repositories
    single<LocationRepository> { LocationRepositoryImpl(androidContext(), get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetWeatherForCityUseCase(get()) }
    factory { GetWeatherForLastSearchedCityUseCase(get()) }
    factory { GetWeatherForLocationUseCase(get()) }
    factory { GetCheckPermissionUseCase(get()) }
    factory { GetCurrentLocationUseCase(get()) }
    factory { GetConvertWeatherTemperatureUseCase() }

    // ViewModel
    viewModel {
        WeatherViewModel(
            getWeatherForLastSearchedCity = get(),
            getWeatherForCityUseCase = get(),
            getWeatherForLocationUseCase = get(),
            getCurrentLocationUseCase = get(),
            getCheckPermissionUseCase = get(),
            getConvertWeatherTemperatureUseCase = get()
        )
    }
}