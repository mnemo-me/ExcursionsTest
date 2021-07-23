package com.example.excursionstest.ui.main_screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.excursionstest.R
import com.example.excursionstest.databinding.FragmentMainScreenBinding
import com.example.excursionstest.ui.MainActivity
import com.example.excursionstest.ui.main_screen.image_carousel.ImagePagerAdapter
import com.example.excursionstest.ui.util.loadImage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by viewModel<MainScreenViewModel>()

    private lateinit var mainActivity : MainActivity

    private var audio : String? = null

    lateinit var receiver: BroadcastReceiver

    lateinit var intentFilter: IntentFilter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        mainActivity = requireActivity() as MainActivity

        binding.mainScreenStepName.text = viewModel.step.name
        binding.mainScreenPlayerText.text = viewModel.step.name
        binding.mainScreenStepDescription.text = Html.fromHtml(viewModel.step.description)

        val viewPagerAdapter = ImagePagerAdapter(childFragmentManager, lifecycle, viewModel.step.images)
        binding.mainScreenStepImages.adapter = viewPagerAdapter

        TabLayoutMediator(binding.mainScreenStepImagesTabs, binding.mainScreenStepImages) {tabs, position ->

        }.attach()

        binding.mainScreenStepImagesCount.text = "${binding.mainScreenStepImagesTabs.selectedTabPosition+1}/${binding.mainScreenStepImagesTabs.tabCount}"

        binding.mainScreenStepImagesTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.mainScreenStepImagesCount.text = "${binding.mainScreenStepImagesTabs.selectedTabPosition+1}/${binding.mainScreenStepImagesTabs.tabCount}"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        audio = viewModel.step.audio


        binding.mainScreenPlayerPanel.setOnClickListener{view ->
            view.findNavController().navigate(R.id.action_mainScreenFragment_to_playerFragment)
        }

        val state = mainActivity.getPlaybackState()
        setPlayPauseButtonImage(state)

        binding.mainScreenPlayerPlayButton.setOnClickListener{
            mainActivity.playPause(Uri.parse("android.resource://${mainActivity.packageName}$audio"))
        }

        binding.mainScreenPlayerSeekBackButton.setOnClickListener{
            mainActivity.seekBack()
        }

        binding.mainScreenSeekForwardButton.setOnClickListener{
            mainActivity.seekForward()
        }

        lifecycleScope.launch {

            while (true){
                binding.mainScreenPlayerSeekBar.progress = mainActivity.getProgress().div(1000).toInt()
                delay(1000)
            }
        }

        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    "track_initialized" -> {
                        binding.mainScreenPlayerSeekBar.max = (intent.extras!!.getLong("duration").div(1000)).toInt()
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

    private fun setPlayPauseButtonImage(state: Int){
        if (state == PlaybackStateCompat.STATE_PLAYING){
            binding.mainScreenPlayerPlayButton.loadImage(R.drawable.outline_pause_black_24)
        }else{
            binding.mainScreenPlayerPlayButton.loadImage(R.drawable.outline_play_circle_filled_black_24)
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