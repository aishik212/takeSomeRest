package com.simpleapps.takesomerest

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import com.simpleapps.takesomerest.databinding.RestingLayoutBinding
import java.util.*

class Resting : Activity() {
    var mPlayer2: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = RestingLayoutBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        Log.d("texts", "onCreate: " + getTime())
        if (getTime() == "N") {
            mPlayer2 = MediaPlayer.create(this, R.raw.night_sound)
        } else {
            mPlayer2 = MediaPlayer.create(this, R.raw.summer_sound)
            binding.animationView.setAnimation(R.raw.summer)
            binding.animationView.playAnimation()
        }
        mPlayer2?.setVolume(0.65F, 0.65F)
        mPlayer2?.isLooping = true
        mPlayer2?.start()

        binding.gHomeBtn.setOnClickListener {
            mPlayer2?.stop()
            mPlayer2?.release()
            mPlayer2 = null
            finish()
        }
        binding.startWorkingBtn.setOnClickListener {
            mPlayer2?.stop()
            mPlayer2?.release()
            mPlayer2 = null
            finish()
            val receivedTime = intent.extras?.get("time")
            val i = Intent(this, startWorking::class.java)
            i.putExtra("time", "$receivedTime")
            MainActivity.startAct(this, i)
        }

    }

    fun getTime(): String {
        val c = Calendar.getInstance()
        val get = c.get(Calendar.HOUR_OF_DAY)
        Log.d("texts", "getTime: " + get)
        return when (get) {
            in 7..17 -> "M"
            in 0..7 -> "N"
            in 17..23 -> "N"
            else -> "M"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer2?.stop()
        mPlayer2?.release()
        mPlayer2 = null
    }
}
