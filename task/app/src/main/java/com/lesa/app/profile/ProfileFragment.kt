package com.lesa.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.databinding.FragmentProfileBinding
import com.lesa.app.model.User
import com.lesa.app.stubPeople
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private val binding: FragmentProfileBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserView(stubPeople.first())
    }

    private fun setUpUserView(user: User) {
        binding.include.apply {
            userNameTextView.text = user.name
            userNetStatusTextView.text = resources.getString(user.netStatus.text)
            userNetStatusTextView.setTextColor(resources.getColor(user.netStatus.color))
            Picasso.get().load(user.avatar).into(userLogoImage)
        }
    }
}