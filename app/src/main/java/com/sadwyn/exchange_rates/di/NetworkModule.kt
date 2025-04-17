package com.sadwyn.exchange_rates.di

import com.sadwyn.exchange_rates.data.api.OpenExchangeApi
import com.sadwyn.exchange_rates.data.network.AuthInterceptor
import com.sadwyn.exchange_rates.data.network.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val API_ACCESS_TOKEN = "85c16fbba5fe4e5089f3e99fffce2d72"
    private const val BASE_URL = "https://openexchangerates.org/"

    @Provides
    fun provideTokenProvider(): TokenProvider = object : TokenProvider {
        override fun getToken(): String {
            return API_ACCESS_TOKEN
        }
    }

    @Provides
    fun provideAuthInterceptor(tokenProvider: TokenProvider): Interceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): OpenExchangeApi {
        return retrofit.create(OpenExchangeApi::class.java)
    }
}