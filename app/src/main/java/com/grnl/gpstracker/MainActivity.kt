package com.grnl.gpstracker
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grnl.gpstracker.databinding.ActivityMainBinding
import com.grnl.gpstracker.fragments.HomeFragment
import com.grnl.gpstracker.fragments.SettingsFragment
import com.grnl.gpstracker.fragments.ViewTrackFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {
            fragmentReplace(HomeFragment())
        }
        onBottomNavClicks()
    }

    private fun onBottomNavClicks(){
        binding.btmNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home_id -> {
                    fragmentReplace(HomeFragment())
                }
                R.id.route_id -> {
                    fragmentReplace(ViewTrackFragment())
                }
                R.id.id_settings -> fragmentReplace(SettingsFragment())
            }
            true
        }
    }

    private fun fragmentReplace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
