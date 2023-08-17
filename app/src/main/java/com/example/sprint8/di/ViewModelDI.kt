package com.example.sprint8.di

import com.example.sprint8.App
import com.example.sprint8.UI.viewmodel.*
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.data.settings.SettingRepository
import com.example.sprint8.domain.search.SearchInteractor
import com.example.sprint8.domain.settings.SettingInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }
    viewModel {
        MediaViewModel(get())
    }
    viewModel {
        MediaLibraryViewModel()
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        MediaLibraryViewModel()
    }
    viewModel {
        SettingsViewModel(get(),androidContext() as App)
    }
}
val interactorModule = module {
    single {
        SearchInteractor(get())
    }
    single {
        SettingInteractor(get())
    }
}
val repositoryModule = module {
    single {
        SearchRepository(get(), get())
    }
    single {
        SettingRepository(get())
    }

}
val internetModule = module {
    single {
        RestProvider().api
    }
}
val dataModule = module {
    single {
        HistoryControl(get())
    }
    single {
        SettingSharedPreference(get())
    }
}
