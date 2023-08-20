package com.example.sprint8.domain.settings

import com.example.sprint8.data.settings.SettingRepository

class SettingInteractor(private val settingRepository: SettingRepository) {

    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingRepository.editEnableDarkThemeSetting(checked)
    }
}