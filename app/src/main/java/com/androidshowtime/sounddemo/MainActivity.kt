package com.androidshowtime.sounddemo

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        //creating MediaPlayer with MediaPlayer.create() inside onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.blippy_trance)

        //create AudioManager
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        //establish maximum volume for the device and Music Stream
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        //getting current volume
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        //set maximum volume on seek bar
        seekBar.max = maxVolume

        //set current device volume  on seek bar
        seekBar.progress = currentVolume
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //control volume using seek bar
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                textView.text = progress.toString()

                Timber.i("Volume Progress Level: $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

//the audio is contained inside the mediaPlayer
        scrubSeekBar.max = mediaPlayer.duration
        scrubSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Timber.i("Track Progress Level $progress")
                mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {


                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer.start()
            }
        })
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                scrubSeekBar.progress = mediaPlayer.currentPosition
            }
        }, 0, 500)

    }

    inline fun timer(
        name: String? = null,
        daemon: Boolean = false,
        initialDelay: Long = 0L,
        period: Long = 300,
        crossinline action: TimerTask.() -> Unit
    ) {
        scrubSeekBar.setProgress(mediaPlayer.currentPosition)
    }

    fun play(view: View) {
        //start playing when play button is clicked
        mediaPlayer.start()
    }

    fun pause(view: View) {
        //pause when pause button is clicked
        mediaPlayer.pause()
    }
}
