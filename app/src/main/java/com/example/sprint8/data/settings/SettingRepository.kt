package com.example.sprint8.data.settings

import com.example.sprint8.data.preferences.SettingControl
import com.example.sprint8.domain.interfaces.SettingRepositoryInterface

class SettingRepository(
    private val settingControl: SettingControl
): SettingRepositoryInterface {

    override fun editEnableDarkThemeSetting(checked: Boolean) {
        settingControl.editEnableDarkThemeSetting(checked)
    }

}

