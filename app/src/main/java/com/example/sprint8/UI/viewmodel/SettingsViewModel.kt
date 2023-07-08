package com.example.sprint8.UI.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : ViewModel() {

    private var changeActiveDarkTheme = MutableLiveData<Boolean?>(null)
    fun getActiveDarkTheme(): LiveData<Boolean?> = changeActiveDarkTheme


    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingInteractor.editEnableDarkThemeSetting(checked)
        changeActiveDarkTheme.value = checked
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY]) as App
                SettingsViewModel(
                    CreatorSettingObject.createSearchInteractor(application),
                )
            }
        }
    }
}