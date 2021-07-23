package com.example.excursionstest.ui.player

import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.excursionstest.R
import com.example.excursionstest.databinding.FragmentPlayerBinding
import com.example.excursionstest.ui.MainActivity
import com.example.excursionstest.ui.util.loadImage
import com.example.excursionstest.ui.util.setTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentPlayerBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var mainActivity : MainActivity

    private var audio : String? = null

    lateinit var receiver: BroadcastReceiver
    lateinit var intentFilter: IntentFilter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        val view = binding.root

        mainActivity = requireActivity() as MainActivity

        binding.playerExcursionNameText.text = viewModel.excursion.name
        binding.playerStepName.text = viewModel.step.name
        binding.playerText.text = Html.fromHtml(viewModel.step.description)

        audio = viewModel.step.audio

        binding.playerStepListButton.setOnClickListener{view ->
            parentFragment?.view?.findNavController()?.navigate(R.id.action_playerFragment_to_stepListFragment)
        }

        val state = mainActivity.getPlaybackState()
        setPlayPauseButtonImage(state)

        val trackDuration = mainActivity.getTrackDuration()
        binding.playerSeekBar.max = (trackDuration.div(1000)).toInt()
        binding.playerTotalTime.setTime(trackDuration)

        binding.playerPlayButton.setOnClickListener{
            mainActivity.playPause(Uri.parse("android.resource://${mainActivity.packageName}$audio"))
        }

        binding.playerSeekBackButton.setOnClickListener{
            mainActivity.seekBack()
        }

        binding.playerSeekForwardButton.setOnClickListener{
            mainActivity.seekForward()
        }

        binding.playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if (fromUser){
                    val activity = requireActivity() as MainActivity
                    activity.seekTo(progress.times(1000).toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.playerBackButton.setOnClickListener{view ->
            dismiss()
        }

        lifecycleScope.launch {

            while (true){
                binding.playerSeekBar.progress = mainActivity.getProgress().div(1000).toInt()
                binding.playerCurrentTime.setTime(mainActivity.getProgress())
                delay(1000)
            }
        }

        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    "track_initialized" -> {
                        val timeInMs = intent.extras!!.getLong("duration")
                        binding.playerSeekBar.max = (timeInMs.div(1000)).toInt()
                        binding.playerTotalTime.setTime(timeInMs)
                    }
                    "playback_state_changed" -> setPlayPauseButtonImage(intent.extras!!.getInt("state"))
                }
            }
        }

        intentFilter = IntentFilter()
        intentFilter.addAction("track_initialized")
        intentFilter.addAction("playback_state_changed")

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener{dialogInterface ->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                val layoutParams = view.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                view.layoutParams = layoutParams
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                behavior.skipCollapsed = true
            }
        }

        return dialog
    }

    private fun setPlayPauseButtonImage(state: Int){
        if (state == PlaybackStateCompat.STATE_PLAYING){
            binding.playerPlayButton.loadImage(R.drawable.outline_pause_black_24)
        }else{
            binding.playerPlayButton.loadImage(R.drawable.outline_play_circle_filled_black_24)
        }
    }

    override fun onStart() {
        super.onStart()
        context?.registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        context?.unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}