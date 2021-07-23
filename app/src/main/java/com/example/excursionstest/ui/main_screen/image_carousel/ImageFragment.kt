package com.example.excursionstest.ui.main_screen.image_carousel

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.excursionstest.databinding.FragmentImageBinding
import com.example.excursionstest.ui.util.loadImage

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding
    get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentImageBinding.inflate(inflater, container, false)

        val view = binding.root

        val image = requireArguments().getString("image")

        binding.stepImage.loadImage(Uri.parse(image))

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}