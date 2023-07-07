package com.example.sprint8.data.preferences

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SettingSharedPreference(val context: Context) : SettingControl {
    companion object {
        const val KEY_NAME_SETTING = "setting"
        const val KEY_DARK_THEME = "darkTheme"
    }

    override fun editEnableDarkThemeSetting(checked: Boolean) {
        val settings =
            context.getSharedPreferences(KEY_NAME_SETTING, AppCompatActivity.MODE_PRIVATE)
        val edit = settings.edit()
        edit.putBoolean(KEY_DARK_THEME, checked)
        edit.apply()
    }

    override fun getDarkThemeSetting(): Boolean {
        val settings = context.getSharedPreferences(KEY_NAME_SETTING, Application.MODE_PRIVATE)
        return settings.getBoolean(KEY_DARK_THEME, false)
    }

}


interface SettingControl {
    fun editEnableDarkThemeSetting(checked: Boolean)
    fun getDarkThemeSetting(): Boolean
}