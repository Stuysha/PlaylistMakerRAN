package com.example.sprint8

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        darkTheme = SettingSharedPreference(this).getDarkThemeSetting()
        switchTheme(darkTheme)
        startKoin {
            androidContext(this@App)
            modules(viewModelModule, interactorModule, repositoryModule, internetModule, dataModule)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}

