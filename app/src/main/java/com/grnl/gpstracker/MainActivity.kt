package com.grnl.gpstracker
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grnl.gpstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBottomNavClicks()


    }

    private fun onBottomNavClicks(){
        binding.btmNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home_id -> Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
                R.id.route_id -> Toast.makeText(this, "Route", Toast.LENGTH_LONG).show()
                R.id.id_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

}
