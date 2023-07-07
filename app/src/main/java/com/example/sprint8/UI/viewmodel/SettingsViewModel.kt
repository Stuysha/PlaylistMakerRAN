package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sprint8.App
import com.example.sprint8.creator.CreatorSettingObject
import com.example.sprint8.domain.settings.SettingInteractor

class SettingsViewModel(
    private val settingInteractor: SettingInteractor,
    private val application: App
) : ViewModel() {


    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingInteractor.editEnableDarkThemeSetting(checked)
        application.switchTheme(checked)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY]) as App
                SettingsViewModel(
                    CreatorSettingObject.createSearchInteractor(application),
                    application
                )
            }
        }
    }
}