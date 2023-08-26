package com.example.sprint8.data.settings

import com.example.sprint8.data.preferences.SettingControl

class SettingRepository(
    private val settingControl: SettingControl
): SettingRepositoryInterface {

    override fun editEnableDarkThemeSetting(checked: Boolean) {
        settingControl.editEnableDarkThemeSetting(checked)
    }

}

interface SettingRepositoryInterface{
    fun editEnableDarkThemeSetting(checked: Boolean)
}