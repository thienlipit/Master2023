package com.example.rsa_android

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rsa_android.databinding.ActivityMainBinding
import com.example.rsa_android.fragment.MyViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

const val SAVE_PUBLIC_KEY = 1
const val SAVE_PRIVATE_KEY = 2
const val LOAD_OTHER_PUBLIC_KEY = 3
const val EXPORT_ENCRYPTED_TEXT = 4
const val LOAD_OTHER_PRIVATE_KEY = 5
const val LOAD_ENCRYPTED_TEXT = 6

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var myViewPager2Adapter: MyViewPager2Adapter

    @RequiresApi(Build.VERSION_CODES.N)
    val requestMultiplePermissionsAPI30Below =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
            when {
                permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, true) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, true) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private val requestMultiplePermissionsAPI31Above =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        myViewPager2Adapter = MyViewPager2Adapter(this)

        viewPager2.adapter = myViewPager2Adapter
        setSupportActionBar(toolbar)


        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewPager2.isUserInputEnabled = false

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
        requestAllPermission()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissionsAPI31Above.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            requestMultiplePermissionsAPI30Below.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }
}