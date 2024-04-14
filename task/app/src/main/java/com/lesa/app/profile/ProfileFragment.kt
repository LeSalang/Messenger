package com.lesa.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val binding: FragmentProfileBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        lifecycleScope.launch {
            viewModel.state.collect(::setUpUserView)
        }
    }

    private fun setUpUserView(userState: ProfileScreenState) {
        when (userState) {
            is ProfileScreenState.DataLoaded -> {
                val user = userState.user
                binding.user.apply {
                    userNameTextView.text = user.name
                    userNetStatusTextView.text = resources.getString(user.netStatus.text)
                    userNetStatusTextView.setTextColor(resources.getColor(user.netStatus.color))
                    Picasso.get().load(user.avatar).into(userLogoImage)
                }
                binding.user.itemProfile.visibility = VISIBLE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = GONE
            }
            ProfileScreenState.Error -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = VISIBLE
                binding.shimmer.visibility = GONE
            }
            ProfileScreenState.Loading -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = VISIBLE
            }
        }
    }
}