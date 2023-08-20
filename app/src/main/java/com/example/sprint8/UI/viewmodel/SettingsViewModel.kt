package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sprint8.App
import com.example.sprint8.domain.settings.SettingInteractor

class SettingsViewModel(
    private val settingInteractor: SettingInteractor,
) : ViewModel() {

    private var changeActiveDarkTheme = MutableLiveData<Boolean?>(null)
    fun getActiveDarkTheme(): LiveData<Boolean?> = changeActiveDarkTheme


    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingInteractor.editEnableDarkThemeSetting(checked)
        changeActiveDarkTheme.value = checked
    }

}