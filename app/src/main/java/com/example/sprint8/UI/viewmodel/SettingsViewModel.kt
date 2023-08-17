package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sprint8.App
import com.example.sprint8.domain.settings.SettingInteractor

class SettingsViewModel(
    private val settingInteractor: SettingInteractor,
    private val application: App
) : ViewModel() {


    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingInteractor.editEnableDarkThemeSetting(checked)
        application.switchTheme(checked)
    }

}