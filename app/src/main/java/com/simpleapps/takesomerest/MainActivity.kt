package com.simpleapps.takesomerest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.simpleapps.takesomerest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = ActivityMainBinding.inflate(layoutInflater)
        val root = inflate.root
        setContentView(root)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainAct = this
        SharedPrefUtils.sharedPreferences(applicationContext)

        if (SharedPrefUtils.getBooleanData(applicationContext, "skip_home")) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    fun click(v: View, mainActivity: MainActivity) {
        finish()
        startAct(this, Intent(this, HomeActivity::class.java))
        SharedPrefUtils.saveData(applicationContext, "skip_home", true)
    }

    companion object {
        fun startAct(activity: Activity, intent: Intent) {
            activity.startActivity(intent)
        }
    }
}