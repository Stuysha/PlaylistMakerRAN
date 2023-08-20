package com.example.sprint8.di

import com.example.sprint8.UI.viewmodel.*
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.player.PlayerRepository
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.data.preferences.SettingControl
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.data.settings.SettingRepository
import com.example.sprint8.domain.player.PlayerInteractor
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
        MediaViewModel(it.get(), get())
    }
    viewModel {
        MediaLibraryViewModel()
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        SettingsViewModel(get())
    }
}
val interactorModule = module {
    single {
        SearchInteractor(get())
    }
    single {
        SettingInteractor(get())
    }
    factory {
        PlayerInteractor(get())
    }
}
val repositoryModule = module {
    single {
        SearchRepository(get(), get())
    }
    single {
        SettingRepository(get())
    }
    factory {
        PlayerRepository(get())
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
    single<SettingControl> {
        SettingSharedPreference(androidContext())
    }
    factory {
        android.media.MediaPlayer()
    }

}
