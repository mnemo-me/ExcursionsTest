package com.example.excursionstest.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.excursionstest.databinding.ActivityMainBinding
import com.example.excursionstest.service.MusicService


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var mediaBrowser : MediaBrowserCompat

    private val connectionCallback = object : MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@MainActivity, token)
                MediaControllerCompat.setMediaController(this@MainActivity, mediaController)
            }

            val mediaController = MediaControllerCompat.getMediaController(this@MainActivity)
            mediaController.registerCallback(controllerCallback)
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback(){

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)

            val intent = Intent()
            intent.action = "track_initialized"
            intent.putExtra("duration", metadata!!.getLong(MediaMetadataCompat.METADATA_KEY_DURATION))

            sendBroadcast(intent)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

            val intent = Intent()
            intent.action = "playback_state_changed"
            intent.putExtra("state", state?.state)

            sendBroadcast(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaBrowser = MediaBrowserCompat(this, ComponentName(this, MusicService::class.java), connectionCallback, null)
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()

        val mediaController = MediaControllerCompat.getMediaController(this)
        mediaController.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    fun playPause(uri: Uri){
        val mediaController = MediaControllerCompat.getMediaController(this)
        val state = mediaController.playbackState.state

        if (state == PlaybackStateCompat.STATE_PAUSED ||
            state == PlaybackStateCompat.STATE_STOPPED ||
            state == PlaybackStateCompat.STATE_NONE) {

            mediaController.transportControls.playFromUri(uri, null)

        } else if (state == PlaybackStateCompat.STATE_PLAYING ||
            state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_CONNECTING) {

            mediaController.transportControls.pause()
        }
    }

    fun seekBack(){
        val mediaController = MediaControllerCompat.getMediaController(this)
        mediaController.transportControls.sendCustomAction("seek_back", null)
    }

    fun seekForward(){
        val mediaController = MediaControllerCompat.getMediaController(this)
        mediaController.transportControls.sendCustomAction("seek_forward", null)
    }

    fun seekTo(position: Long){
        val mediaController = MediaControllerCompat.getMediaController(this)
        mediaController.transportControls.seekTo(position)
    }

    @SuppressLint("RestrictedApi")
    fun getProgress() : Long{
        val mediaController = MediaControllerCompat.getMediaController(this)
        return mediaController?.playbackState?.getCurrentPosition(null) ?: 0
    }

    fun getPlaybackState() : Int{
        val mediaController = MediaControllerCompat.getMediaController(this)
        return mediaController?.playbackState?.state ?: PlaybackStateCompat.STATE_NONE
    }

    fun getTrackDuration() : Long {
        val mediaController = MediaControllerCompat.getMediaController(this)
        return mediaController?.metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0L
    }
}