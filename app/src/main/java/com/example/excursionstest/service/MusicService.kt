package com.example.excursionstest.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

class MusicService : MediaBrowserServiceCompat() {

    private var mediaSession : MediaSessionCompat? = null
    private lateinit var stateBuilder : PlaybackStateCompat.Builder
    private var exoPlayer: SimpleExoPlayer? = null

    private var oldUri: Uri? = null

    private val mediaSessionCallback = object : MediaSessionCompat.Callback(){
        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            super.onPlayFromUri(uri, extras)

            uri?.let {

                if (uri != oldUri){
                    val mediaItem = MediaItem.fromUri(uri)
                    play(mediaItem)
                    oldUri = uri
                }else{
                    play()
                }
            }
        }

        override fun onPause() {
            super.onPause()
            pause()
        }

        override fun onStop() {
            super.onStop()
            stop()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            seekTo(pos)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            when(action){
                "seek_back" -> seekBack()
                "seek_forward" -> seekForward()
            }
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot("", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {

    }

    override fun onCreate() {
        super.onCreate()

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        exoPlayer!!.addListener( object : Player.Listener{
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)

                if (state == ExoPlayer.STATE_READY) {

                    val metadataBuilder = MediaMetadataCompat.Builder()
                    metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, exoPlayer!!.duration)
                    mediaSession?.setMetadata(metadataBuilder.build())
                }

            }
        })

        mediaSession = MediaSessionCompat(baseContext, "TAG")
            .apply {
                setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
                stateBuilder = PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE)
                setPlaybackState(stateBuilder.build())
                setCallback(mediaSessionCallback)
                setSessionToken(sessionToken)
                isActive = true
            }

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    fun play(mediaItem: MediaItem){
        if (exoPlayer == null) exoPlayer = SimpleExoPlayer.Builder(this).build()

        exoPlayer?.apply {
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, exoPlayer!!.currentPosition, 1.0f)
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun play(){
        exoPlayer?.apply {
            playWhenReady = true
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, exoPlayer!!.currentPosition, 1.0f)
            mediaSession?.isActive = true
        }
    }

    fun pause(){
        exoPlayer?.apply {
            playWhenReady = false
            if (playbackState == PlaybackStateCompat.STATE_PLAYING){
                updatePlaybackState(PlaybackStateCompat.STATE_PAUSED, exoPlayer!!.currentPosition, 0.0f)
            }
        }
    }

    fun stop(){
        updatePlaybackState(PlaybackStateCompat.STATE_NONE, exoPlayer!!.currentPosition, 0.0f )
        exoPlayer?.playWhenReady = false
        exoPlayer?.release()
        exoPlayer = null
        mediaSession?.isActive = false
        mediaSession?.release()
    }

    fun seekTo(position: Long){
        val playbackSpeed = if (exoPlayer?.playbackState == PlaybackStateCompat.STATE_PLAYING) 1.0f else 0.0f
        updatePlaybackState(exoPlayer!!.playbackState, position, playbackSpeed)
        exoPlayer?.seekTo(position)
    }

    fun seekBack(){
        val currentPosition = exoPlayer?.currentPosition ?: 0
        if (currentPosition >= 5000) {
            seekTo(currentPosition - 5 * 1000)
        }else{
            seekTo(0)
        }
    }

    fun seekForward(){

        val duration = exoPlayer?.duration ?: 0
        val currentPosition = exoPlayer?.currentPosition ?: 0

        if (duration - currentPosition > 5000) {
            seekTo(currentPosition + 5 * 1000)
        }else{
            seekTo(duration)
        }
    }


    private fun updatePlaybackState(state: Int, position: Long, playbackSpeed: Float){
        mediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(state, position, playbackSpeed)
                .build()
        )
    }

}