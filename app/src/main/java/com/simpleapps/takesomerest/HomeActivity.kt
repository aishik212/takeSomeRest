package com.simpleapps.takesomerest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simpleapps.takesomerest.databinding.HomeLayoutBinding

class HomeActivity : AppCompatActivity() {

    var time = 20
    lateinit var inflate: HomeLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = HomeLayoutBinding.inflate(layoutInflater)
        val root = inflate.root
        setContentView(root)
        setTime()
        inflate.addTimeBtn.setOnClickListener {
            time += 10
            setTime()
        }
        inflate.reduceTimeBtn.setOnClickListener {
            time -= 10
            setTime()
        }
        inflate.startWorkingBtn.setOnClickListener {
            val intent = Intent(this, startWorking::class.java)
            intent.putExtra("time", time)
            MainActivity.startAct(this, intent)
        }
    }

    private fun setTime() {
        when {
            time > 60 -> {
                inflate.timeTv.error = "Time Should be Less than and Hour"
                time = 60
            }
            time < 20 -> {
                inflate.timeTv.error = "Time Should be Greater than 20 Minutes"
                time = 20
            }
            else -> {
                inflate.timeTv.error = null
                inflate.timeTv.setText("$time Minutes")
            }
        }
    }
}