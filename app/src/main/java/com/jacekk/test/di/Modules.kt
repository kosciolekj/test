package com.jacekk.test.di

import com.google.gson.GsonBuilder
import com.jacekk.test.data.LoginRepository
import com.jacekk.test.data.LoginRepositoryImpl
import com.jacekk.test.network.NetworkApi
import com.jacekk.test.network.NetworkApiRepository
import com.jacekk.test.network.NetworkApiRepositoryImpl
import com.jacekk.test.ui.login.LoginFragmentViewModel
import com.jacekk.test.ui.loginresult.LoginResultViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModule = module {
    viewModel { LoginFragmentViewModel() }
    viewModel { LoginResultViewModel(get()) }
}

val domainModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
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
            .baseUrl("https://applover-login.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .build()
    }

    single<NetworkApi> { get<Retrofit>().create(NetworkApi::class.java) }

    single {
        NetworkApiRepositoryImpl(
            networkApi = get(),
            networkDispatcher = Dispatchers.IO
        )
    } bind NetworkApiRepository::class
}

