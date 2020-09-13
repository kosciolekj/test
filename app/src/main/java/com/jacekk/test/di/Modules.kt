package com.jacekk.test.di

import com.google.gson.GsonBuilder
import com.jacekk.test.network.AppLoverApiRepository
import com.jacekk.test.network.AppLoverApiRepositoryImpl
import com.jacekk.test.network.BASE_URL
import com.jacekk.test.network.LoginApi
import com.jacekk.test.ui.login.LoginFragmentViewModel
import com.jacekk.test.ui.loginresult.LoginResultViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModule = module {
    viewModel { LoginFragmentViewModel(androidApplication()) }
    viewModel { LoginResultViewModel(androidApplication(), get()) }
}

val appModule = module {
    single {
        val builder = GsonBuilder()
        builder.excludeFieldsWithoutExposeAnnotation()
        builder.create()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .build()
    }

    single<LoginApi> { get<Retrofit>().create(LoginApi::class.java) }

    single {
        AppLoverApiRepositoryImpl(
            loginApi = get(),
            networkDispatcher = Dispatchers.IO
        )
    } bind AppLoverApiRepository::class
}

