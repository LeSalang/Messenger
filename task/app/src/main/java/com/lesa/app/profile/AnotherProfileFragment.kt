package com.lesa.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lesa.app.databinding.FragmentAnotherProfileBinding

class AnotherProfileFragment : Fragment() {
    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding
        get() = _binding!! // TODO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnotherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "AnotherProfileFragment"
    }
}