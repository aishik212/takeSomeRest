package com.simpleapps.takesomerest

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.simpleapps.takesomerest.databinding.StartWorkingLayoutBinding

class startWorking : Activity() {
    lateinit var inflate: StartWorkingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = StartWorkingLayoutBinding.inflate(layoutInflater)
        val root = inflate.root
        setContentView(root)
        Log.d("texts", "onCreate: " + intent.hasExtra("time") + " " + intent.extras?.get("time"))
        val time = Integer.parseInt("${intent.extras?.get("time") ?: 30}")
        val l = time * 60 * 1000L
        object : CountDownTimer(l, 1000) {
            override fun onTick(p0: Long) {
                val left = p0 / 1000
                val minute = left / 60
                val seconds = left - minute * 60
                inflate.timeTv.text = String.format("%02d:%02d", minute, seconds)
                var speed = ((time / minute) - 1) + 0.6F
                if (speed > 4.5) {
                    speed = 4.5F
                }
                inflate.animationView2.speed = speed
            }

            override fun onFinish() {
                finish()
            }
        }.start()
        inflate.takeBreakBtn.setOnClickListener {
            finish()
        }
    }
}
