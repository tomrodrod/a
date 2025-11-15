package com.android.example.pruebaprueba.di

import android.content.Context
import com.android.example.pruebaprueba.core.platform.service.NetworkHandler
import com.android.example.pruebaprueba.core.platform.service.NetworkHandlerImpl
import com.android.example.pruebaprueba.core.platform.service.client.Client
import com.android.example.pruebaprueba.data.MindeeRepositoryImpl
import com.android.example.pruebaprueba.data.datasource.MindeeDataSourceService
import com.android.example.pruebaprueba.data.datasource.MindeeDataSourceServiceImpl
import com.android.example.pruebaprueba.data.datasource.WebhookEventDataSource
import com.android.example.pruebaprueba.data.datasource.WebhookEventDataSourceImpl
import com.android.example.pruebaprueba.data.service.MindeeApi
import com.android.example.pruebaprueba.data.service.MindeeService
import com.android.example.pruebaprueba.domain.MindeeRepository
import com.android.example.pruebaprueba.models.mindee.InferenceResponse
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SmartSplitModule {

    @Singleton
    @Provides
    fun provideNetworkHandlerImp(@ApplicationContext appContext: Context): NetworkHandler =
        NetworkHandlerImpl(context = appContext)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder().apply {
        this.baseUrl("https://api-v2.mindee.net")
        this.client(Client.createClient())
        this.addConverterFactory(GsonConverterFactory.create())
    }.build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideInferenceParser(gson: Gson): (String) -> InferenceResponse? = { json ->
        try {
            gson.fromJson(json, InferenceResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @Provides
    @Singleton
    fun provideMindeeApi(retrofit: Retrofit): MindeeApi = retrofit.create(MindeeApi::class.java)

    @Provides
    @Singleton
    fun provideMindeeDataSourceService(service: MindeeService): MindeeDataSourceService =
        MindeeDataSourceServiceImpl(service)

    @Provides
    @Singleton
    fun provideMindeeRepository(
        mindeeDataSourceService: MindeeDataSourceService,
        webhookEventDataSource: WebhookEventDataSource
    ): MindeeRepository = MindeeRepositoryImpl(mindeeDataSourceService, webhookEventDataSource)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SmartSplitAbstractModule {

    @Binds
    @Singleton
    abstract fun bindWebhookEventDataSource(
        impl: WebhookEventDataSourceImpl
    ): WebhookEventDataSource

}