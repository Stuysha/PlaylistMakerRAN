package com.example.sprint8.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sprint8.R

class SingleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_activity)

        supportFragmentManager.beginTransaction().add(
            R.id.main_box,
            MainFragment()
        ).commit()
    }
}