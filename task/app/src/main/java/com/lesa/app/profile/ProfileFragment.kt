package com.lesa.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lesa.app.databinding.FragmentProfileBinding
import com.lesa.app.model.User
import com.lesa.app.stubPeople
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!! // TODO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserView(stubPeople.first())
    }

    private fun setUpUserView(user: User) {
        binding.include.apply {
            userNameTextView.text = user.name
            userChatStatusTextView.text = user.chatStatus
            userNetStatusTextView.text = resources.getString(user.netStatus.text)
            userNetStatusTextView.setTextColor(resources.getColor(user.netStatus.color))
            Picasso.get().load(user.avatar).into(userLogoImage)
        }
    }
}