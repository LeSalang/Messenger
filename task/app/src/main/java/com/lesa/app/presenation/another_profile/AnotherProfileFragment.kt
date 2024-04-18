package com.lesa.app.presenation.another_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.R
import com.lesa.app.databinding.FragmentAnotherProfileBinding
import com.lesa.app.domain.model.User
import com.squareup.picasso.Picasso

class AnotherProfileFragment : Fragment(R.layout.fragment_another_profile) {
    private val binding: FragmentAnotherProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserView(requireArguments().getParcelable(USER_ID_KEY))
        binding.backButton.setOnClickListener {
            INSTANCE.router.exit()
        }
    }

    private fun setUpUserView(user: User?) {
        if (user == null) return
        binding.user.apply{
            userNameTextView.text = user.name
            userNetStatusTextView.text = resources.getString(user.presence.text)
            userNetStatusTextView.setTextColor(resources.getColor(user.presence.color))
            Picasso.get().load(user.avatar).into(userLogoImage)
        }
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"

        fun getNewInstance(user: User): AnotherProfileFragment {
            return AnotherProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_ID_KEY, user)
                }
            }
        }
    }
}