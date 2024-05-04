package com.lesa.app.presentation.features.user

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.lesa.app.R
import com.lesa.app.databinding.FragmentAnotherProfileBinding
import com.lesa.app.di.people.PeopleComponentViewModel
import com.lesa.app.presentation.features.people.model.UserUi
import com.lesa.app.presentation.utils.BottomBarViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFragment : Fragment(R.layout.fragment_another_profile) {
    private val binding: FragmentAnotherProfileBinding by viewBinding()
    private lateinit var bottomBarViewModel: BottomBarViewModel

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<PeopleComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBarViewModel = ViewModelProvider(requireActivity())[BottomBarViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        setUpUserView(requireArguments().getParcelable(USER_KEY))
        setUpBackButton()
    }

    override fun onStart() {
        viewLifecycleOwner.lifecycleScope.launch {
            bottomBarViewModel.isBottomBarShown.emit(false)
        }
        super.onStart()
    }

    override fun onStop() {
        viewLifecycleOwner.lifecycleScope.launch {
            bottomBarViewModel.isBottomBarShown.emit(true)
        }
        super.onStop()
    }

    private fun setUpBackButton() {
        binding.backButton.setOnClickListener {
            router.exit()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            router.exit()
        }
    }

    private fun setUpUserView(user: UserUi?) {
        if (user == null) return
        binding.user.apply{
            userNameTextView.text = user.name
            userNetStatusTextView.text = resources.getString(user.presence.text)
            if (user.presence.color != null) {
                userNetStatusTextView.isVisible = true
                userNetStatusTextView.setTextColor(resources.getColor(user.presence.color))
            } else {
                userNetStatusTextView.isVisible = false
            }
            Picasso.get().load(user.avatar).into(userLogoImage)
        }
    }

    companion object {
        private const val USER_KEY = "user_id_key"

        fun getNewInstance(user: UserUi): UserFragment {
            return UserFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_KEY, user)
                }
            }
        }
    }
}