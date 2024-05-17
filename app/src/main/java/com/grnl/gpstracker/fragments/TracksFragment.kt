package com.grnl.gpstracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grnl.gpstracker.R
import com.grnl.gpstracker.databinding.FragmentMainBinding
import com.grnl.gpstracker.databinding.FragmentViewTrackBinding
import com.grnl.gpstracker.databinding.TracksBinding

class TracksFragment : Fragment() {
    private lateinit var binding: TracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = TracksFragment()
    }
}