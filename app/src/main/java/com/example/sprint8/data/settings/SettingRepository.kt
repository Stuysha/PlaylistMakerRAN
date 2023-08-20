package com.example.sprint8.data.settings

import com.example.sprint8.data.preferences.SettingControl

class SettingRepository(
    private val settingControl: SettingControl
) {

    fun editEnableDarkThemeSetting(checked: Boolean) {
        settingControl.editEnableDarkThemeSetting(checked)
    }

}