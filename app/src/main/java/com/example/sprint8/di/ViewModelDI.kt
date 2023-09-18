package com.example.sprint8.di

import com.example.sprint8.UI.viewmodel.FavoritesTracksViewModel
import com.example.sprint8.UI.viewmodel.MediaLibraryViewModel
import com.example.sprint8.UI.viewmodel.MediaViewModel
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import com.example.sprint8.UI.viewmodel.SearchViewModel
import com.example.sprint8.UI.viewmodel.SettingsViewModel
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.player.PlayerRepository
import com.example.sprint8.data.player.PlayerRepositoryInterface
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.data.preferences.SettingControl
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.data.search.SearchRepositoryInterface
import com.example.sprint8.data.settings.SettingRepository
import com.example.sprint8.data.settings.SettingRepositoryInterface
import com.example.sprint8.domain.player.PlayerInteractor
import com.example.sprint8.domain.player.PlayerInteractorInterface
import com.example.sprint8.domain.search.SearchInteractor
import com.example.sprint8.domain.search.SearchInteractorInterface
import com.example.sprint8.domain.settings.SettingInteractor
import com.example.sprint8.domain.settings.SettingInteractorInterface
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
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
    viewModel {
        FavoritesTracksViewModel()
    }
    viewModel {
        PlaylistsViewModel()
    }
}
val interactorModule = module {
    single<SearchInteractorInterface> {
        SearchInteractor(get())
    }
    single<SettingInteractorInterface> {
        SettingInteractor(get())
    }
    factory<PlayerInteractorInterface> {
        PlayerInteractor(get())
    }
}
val repositoryModule = module {
    single<SearchRepositoryInterface> {
        SearchRepository(get(), get())
    }
    single<SettingRepositoryInterface> {
        SettingRepository(get())
    }
    factory<PlayerRepositoryInterface> {
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
