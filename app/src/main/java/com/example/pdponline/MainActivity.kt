package com.example.pdponline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.Toast
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    lateinit var handler: Handler
    var doubleclick = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerView).navigateUp()
    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                if (fragment is HomeFragment) {
                    if (doubleclick) {
                        super.onBackPressed()
                        return
                }
                    handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        doubleclick = false
                    }, 2000)
                    doubleclick = true
                    Toast.makeText(
                        this,
                        "Dasturdan chiqish uchun yana bir marta bosing",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    super.onBackPressed()
                }


            }
        }
    }
}