package com.ikerfah.thmanyah.di

import com.ikerfah.thmanyah.data.remote.ApiService
import com.ikerfah.thmanyah.data.remote.NetworkClient
import com.ikerfah.thmanyah.data.remote.SearchService
import com.ikerfah.thmanyah.data.repository.AppRepositoryImpl
import com.ikerfah.thmanyah.domain.repository.AppRepository
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import com.ikerfah.thmanyah.domain.usecase.SearchUseCase
import com.ikerfah.thmanyah.ui.home.HomeViewModel
import com.ikerfah.thmanyah.ui.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppRepository> { AppRepositoryImpl(get(), get()) }
    factory { GetHomeSectionsUseCase(get()) }
    factory { SearchUseCase(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}

val networkModule = module {
    single {
        NetworkClient.create(
            baseUrl = "https://api-v2-b2sit6oh3a-uc.a.run.app/",
            service = ApiService::class.java
        )
    }

    single {
        NetworkClient.create(
            baseUrl = "https://mock.apidog.com/m1/735111-711675-default/",
            service = SearchService::class.java
        )
    }
}