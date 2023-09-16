package com.example.sprint8.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sprint8.R
import com.example.sprint8.UI.fragments.FavoritesTracksFragment
import com.example.sprint8.UI.fragments.PlaylistsFragment
import com.example.sprint8.UI.viewmodel.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class MediaLibraryActivity : AppCompatActivity() {
    private val viewModel: MediaLibraryViewModel by inject()
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_libraryactivity)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)


        viewPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {

                    tab.text = this.getString(R.string.favorites_tracks)
                }

                1 -> tab.text = this.getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        val toolbar = findViewById<Toolbar>(R.id.tool_mediateka)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}