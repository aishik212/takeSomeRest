package com.simpleapps.takesomerest

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.simpleapps.takesomerest.databinding.StartWorkingLayoutBinding
import org.json.JSONObject
import java.util.*


class startWorking : Activity() {


    lateinit var countDownTimer: CountDownTimer
    var mPlayer2: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startWorkingLayoutBinding = StartWorkingLayoutBinding.inflate(layoutInflater)
        val root = startWorkingLayoutBinding.root
        setContentView(root)
        var receivedTime = intent.extras?.get("time")
        if (BuildConfig.DEBUG) {
            receivedTime = "00:03"
        }
        val split = receivedTime.toString().split(":")
        Log.d("texts", "onCreate: $split")
        if (split.size > 1) {
            receivedTime = split[0]
        }
        time = Integer.parseInt("${receivedTime ?: 30}")
        var l = time * 60 * 1000L
        if (split.size > 1) {
            Log.d("texts", "onCreate: ...." + split + " " + split[1])
            Log.d("texts", "onCreate: ...." + Integer.parseInt(split[1]) * 60)
            l += Integer.parseInt(split[1]) * 1000
        }
        Log.d("texts", "FINAL TIME: -> $l")
        mPlayer2 = MediaPlayer.create(this, R.raw.slowed_calm_sound)
        mPlayer2?.setVolume(0.65F, 0.65F)
        mPlayer2?.isLooping = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPlayer2?.playbackParams?.speed = 0.25F
        }
        mPlayer2?.start()
        val muteAnimationView = startWorkingLayoutBinding.muteAnimationView
        muteAnimationView.setMinAndMaxFrame(0, 70)
        muteAnimationView.playAnimation()
        muteAnimationView.setOnClickListener {
            if (mPlayer2?.isPlaying == true) {
                mPlayer2?.pause()
                muteAnimationView.setMinAndMaxFrame(70, 150)
                muteAnimationView.playAnimation()
            } else {
                mPlayer2?.start()
                muteAnimationView.setMinAndMaxFrame(0, 70)
                muteAnimationView.playAnimation()
            }
        }
        val takeBreakBtn = startWorkingLayoutBinding.takeBreakBtn
        val intent = Intent(this, TimerService::class.java)
        countDownTimer = object : CountDownTimer(l, 1000) {
            override fun onTick(p0: Long) {
                val left = p0 / 1000
                minute = left / 60
                seconds = left - minute * 60
                updateTime(this@startWorking)
                startWorkingLayoutBinding.workDoneView.visibility = GONE
            }

            override fun onFinish() {
                mPlayer2?.stop()
                startWorkingLayoutBinding.animationView2.pauseAnimation()
                startWorkingLayoutBinding.workDoneView.visibility = VISIBLE
                startWorkingLayoutBinding.takeBreakTop.visibility = GONE
                startWorkingLayoutBinding.workDoneAnimationView.playAnimation()
                /*if (BuildConfig.DEBUG) {
                    startWorkingLayoutBinding.restBtn.performClick()
                }*/
                Log.d("texts", "onFinish: ")
            }
        }

        countDownTimer.start()
        takeBreakBtn.setOnClickListener {
            val json = JSONObject()
            json.put("time", System.currentTimeMillis())
            json.put("completed", false)
            json.put("name", null)
            json.put("totalTime", receivedTime)
            json.put("type", "WORK")
            json.put("timeLeft", minute * 60 + seconds)
            json.put("restarting", false)
            SharedPrefUtils.saveData(
                applicationContext,
                "Session_" + System.currentTimeMillis(),
                json.toString()
            )
            finish()
            countDownTimer.cancel()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra("stop", false)
            startForegroundService(intent)
        } else {
            intent.putExtra("stop", false)
            startService(intent)
        }
        startWorkingLayoutBinding.restartBtn.setOnClickListener {
            val json = JSONObject()
            json.put("time", System.currentTimeMillis())
            json.put("completed", false)
            json.put("name", null)
            json.put("type", "WORK")
            json.put("totalTime", receivedTime)
            json.put("timeLeft", minute * 60 + seconds)
            json.put("restarting", true)
            SharedPrefUtils.saveData(
                applicationContext,
                "Session_" + System.currentTimeMillis(),
                json.toString()
            )
            finish()
            val i = Intent(this, startWorking::class.java)
            i.putExtra("time", "$receivedTime")
            MainActivity.startAct(this, i)
        }
        startWorkingLayoutBinding.restBtn.setOnClickListener {
            val json = JSONObject()
            json.put("time", System.currentTimeMillis())
            json.put("completed", false)
            json.put("name", null)
            json.put("type", "WORK")
            json.put("totalTime", receivedTime)
            json.put("timeLeft", minute * 60 + seconds)
            json.put("restarting", false)
            SharedPrefUtils.saveData(
                applicationContext,
                "Session_" + System.currentTimeMillis(),
                json.toString()
            )
            finish()
            val i = Intent(this, Resting::class.java)
            i.putExtra("time", "$receivedTime")
            MainActivity.startAct(this, i)
        }
        startWorkingLayoutBinding.workDoneToday.setOnClickListener {
            val json = JSONObject()
            json.put("time", System.currentTimeMillis())
            json.put("completed", true)
            json.put("name", null)
            json.put("type", "WORK")
            json.put("totalTime", receivedTime)
            json.put("timeLeft", minute * 60 + seconds)
            json.put("restarting", false)
            SharedPrefUtils.saveData(
                applicationContext,
                "Session_" + System.currentTimeMillis(),
                json.toString()
            )
            finish()
        }
//        stopService(intent)
    }

    companion object {
        lateinit var startWorkingLayoutBinding: StartWorkingLayoutBinding

        fun updateTime(startWorking: Activity) {
            startWorkingLayoutBinding.timeTv.text = String.format("%02d:%02d", minute, seconds)
            Log.d("texts", "updateTime: " + time + " " + minute)
            if (minute < 1) {
                minute = 1
            }
            var speed = ((time / minute) - 1) + 0.6F
            if (speed > 4.5) {
                speed = 4.5F
            }
            startWorkingLayoutBinding.animationView2.speed = speed
            TimerService.showNotif(
                startWorking,
                "Timer Running",
                "Timer Left " + String.format("%02d:%02d", minute, seconds)
            )
        }

        var minute: Long = -1
        var seconds: Long = -1
        var time: Int = -1

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("texts", "onDestroy: ")
        countDownTimer.cancel()
        mPlayer2?.stop()
        mPlayer2?.release()
        mPlayer2 = null
        stopService(Intent(this, TimerService::class.java))
        TimerService.removeNotification(applicationContext)
    }
}
