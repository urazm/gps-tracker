package com.grnl.gpstracker
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grnl.gpstracker.databinding.ActivityMainBinding
import com.grnl.gpstracker.fragments.HomeFragment
import com.grnl.gpstracker.fragments.SettingsFragment

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
                    fragmentReplace(HomeFragment.newInstance())
                }
                R.id.id_settings ->{
                    fragmentReplace(SettingsFragment())
                }
            }
            true
        }
    }

    private fun fragmentReplace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment != null && currentFragment::class == fragment::class) {
            return
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
//        Log.d("MYAPP", fragment.toString())
    }


}
