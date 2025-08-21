package com.ikerfah.thmanyah.di

import com.ikerfah.thmanyah.data.remote.NetworkClient
import com.ikerfah.thmanyah.data.repository.AppRepositoryImpl
import com.ikerfah.thmanyah.domain.repository.AppRepository
import com.ikerfah.thmanyah.domain.usecase.GetHomeSectionsUseCase
import com.ikerfah.thmanyah.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppRepository> { AppRepositoryImpl(get()) }
    factory { GetHomeSectionsUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}

val networkModule = module {
    single {
        NetworkClient.create(
            baseUrlHome = "https://api-v2-b2sit6oh3a-uc.a.run.app/"
        )
    }
}