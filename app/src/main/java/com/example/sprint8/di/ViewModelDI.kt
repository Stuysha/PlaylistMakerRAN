package com.example.sprint8.di

import androidx.room.Room
import com.example.sprint8.UI.viewmodel.CreatingNewPlaylistViewModel
import com.example.sprint8.UI.viewmodel.FavoritesTracksViewModel
import com.example.sprint8.UI.viewmodel.MediaLibraryViewModel
import com.example.sprint8.UI.viewmodel.MediaViewModel
import com.example.sprint8.UI.viewmodel.PlayListViewModel
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import com.example.sprint8.UI.viewmodel.SearchViewModel
import com.example.sprint8.UI.viewmodel.SettingsViewModel
import com.example.sprint8.data.converters.TrackConverter
import com.example.sprint8.data.db.AppDatabase
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.media.CreatingNewPlaylistRepository
import com.example.sprint8.data.media.FavoriteTracksRepository
import com.example.sprint8.data.media.TrackRepository
import com.example.sprint8.data.player.PlayerRepository
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.data.preferences.SettingControl
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.data.settings.SettingRepository
import com.example.sprint8.domain.interfaces.CreatingNewPlaylistRepositoryInterface
import com.example.sprint8.domain.interfaces.FavoriteTracksRepositoryInterface
import com.example.sprint8.domain.interfaces.PlayerRepositoryInterface
import com.example.sprint8.domain.interfaces.SearchRepositoryInterface
import com.example.sprint8.domain.interfaces.SettingRepositoryInterface
import com.example.sprint8.domain.interfaces.TrackRepositoryInterface
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractor
import com.example.sprint8.domain.media.CreatingNewPlaylistInteractorInterface
import com.example.sprint8.domain.media.FavoriteTracksInteractor
import com.example.sprint8.domain.media.FavoriteTracksInteractorInterface
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
        MediaViewModel(it.get(), get(), get())
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
        FavoritesTracksViewModel(get())
    }
    viewModel {
        PlaylistsViewModel(get())
    }
    viewModel {
        CreatingNewPlaylistViewModel(get())
    }
    viewModel {
        PlayListViewModel(it.get(), get())
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
        PlayerInteractor(get(), get())
    }
    factory<FavoriteTracksInteractorInterface> {
        FavoriteTracksInteractor(get(), get())
    }
    factory<CreatingNewPlaylistInteractorInterface> {
        CreatingNewPlaylistInteractor(get(), get())
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
        PlayerRepository(get(), get())
    }
    factory { TrackConverter() }
    factory<FavoriteTracksRepositoryInterface> {
        FavoriteTracksRepository(get())
    }
    factory<CreatingNewPlaylistRepositoryInterface> {
        CreatingNewPlaylistRepository(get(), get())
    }
    factory<TrackRepositoryInterface> {
        TrackRepository(get(), get())
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
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
