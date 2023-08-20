package com.example.sprint8.creator

import android.content.Context
import com.example.sprint8.data.preferences.SettingSharedPreference
import com.example.sprint8.data.settings.SettingRepository
import com.example.sprint8.domain.settings.SettingInteractor

object CreatorSettingObject {

    fun createSettingRepository(context: Context): SettingRepository {
        return SettingRepository(
            SettingSharedPreference(context)
        )
    }



    fun createSearchInteractor(context: Context): SettingInteractor {
        return SettingInteractor(
            createSettingRepository(context)
        )
    }
}