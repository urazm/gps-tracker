package com.grnl.gpstracker.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
 import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.grnl.gpstracker.helpers.DialogManager
import com.grnl.gpstracker.databinding.FragmentMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.grnl.gpstracker.helpers.checkPermissions

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        settingsOsm()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPermissions()
    }
    override fun onResume() {
        super.onResume()
        checkLocPermission()
    }



    private fun settingsOsm() {
        Configuration.getInstance().load(
            requireActivity(),
            requireActivity().getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    private fun initOsm() = with(binding) {
        map.controller.setZoom(17.0)
        val mLocProvider = GpsMyLocationProvider(requireActivity())
        val mLocOverlay = MyLocationNewOverlay(mLocProvider, map)
        mLocOverlay.enableMyLocation()
        mLocOverlay.enableFollowLocation()
        mLocOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(mLocOverlay)
        }
    }

    private fun registerPermissions() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initOsm()
            } else {
                Toast.makeText(
                    context,
                    "Вы не дали разрешение на отслеживание местоположения",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkLocPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionAfter10()
        } else {
            checkPermissionBefore10()
        }
        checkLocationEnabled()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAfter10() {
        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermissions(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            initOsm()
        } else {
            Log.d("HomeFragment", "Запрос разрешений после Android 10")
            pLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        }
    }

    private fun checkPermissionBefore10() {
        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initOsm()
            checkLocationEnabled()
        } else {
            Log.d("HomeFragment", "Запрос разрешений до Android 10")
            pLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkLocationEnabled(){
        val lManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!isEnabled){
            DialogManager.showLocEnableDialog(
                activity as AppCompatActivity,
                object : DialogManager.Listener {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            )
        } else {
            Toast.makeText(context, "Location enabled", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
