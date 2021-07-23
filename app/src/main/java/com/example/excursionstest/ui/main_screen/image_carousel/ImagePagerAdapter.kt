package com.example.excursionstest.ui.main_screen.image_carousel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ImagePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val images: List<String>) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return images.size
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = ImageFragment()
        val bundle = Bundle()
        bundle.putString("image", images[position])
        fragment.arguments = bundle

        return fragment
    }
}