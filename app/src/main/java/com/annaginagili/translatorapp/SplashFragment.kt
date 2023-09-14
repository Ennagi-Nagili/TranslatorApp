package com.annaginagili.translatorapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.annaginagili.translatorapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentSplashBinding.inflate(inflater)

        (requireActivity() as MainActivity).bottom.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment(""))
        }, 3000)

        return binding.root
    }
}