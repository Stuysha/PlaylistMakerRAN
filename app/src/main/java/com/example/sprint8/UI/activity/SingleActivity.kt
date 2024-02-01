package com.example.sprint8.UI.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sprint8.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SingleActivity : AppCompatActivity() {
    lateinit var bottomNavigationView:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_box) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        setupNav(navController)
    }
    private fun setupNav(navController:NavController) {
       // val navController = findNavController(R.id.nav_host_fragment)
       // findViewById<BottomNavigationView>(R.id.bottomNav)
         //   .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment -> showBottomNav()
                R.id.mediaLibraryFragment -> showBottomNav()
                R.id.searchFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE

    }
}