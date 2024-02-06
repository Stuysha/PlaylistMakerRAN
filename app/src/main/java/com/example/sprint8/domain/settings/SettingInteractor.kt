package com.example.sprint8.domain.settings

import com.example.sprint8.domain.interfaces.SettingRepositoryInterface

class SettingInteractor(private val settingRepository: SettingRepositoryInterface) :
    SettingInteractorInterface {

    override fun editEnableDarkThemeSetting(checked: Boolean) {
        settingRepository.editEnableDarkThemeSetting(checked)
    }
}

interface SettingInteractorInterface {
    fun editEnableDarkThemeSetting(checked: Boolean)
}