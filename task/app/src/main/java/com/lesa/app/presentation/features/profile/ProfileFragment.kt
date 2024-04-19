package com.lesa.app.presentation.features.profile

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App.Companion.INSTANCE
import com.lesa.app.R
import com.lesa.app.databinding.FragmentProfileBinding
import com.lesa.app.presentation.elm.ElmBaseFragment
import com.lesa.app.presentation.features.profile.ProfileEvent
import com.lesa.app.presentation.utils.ScreenState
import com.squareup.picasso.Picasso
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import com.lesa.app.presentation.features.profile.ProfileEffect as Effect
import com.lesa.app.presentation.features.profile.ProfileEvent as Event
import com.lesa.app.presentation.features.profile.ProfileState as State

class ProfileFragment : ElmBaseFragment<Effect, State, Event>(
    R.layout.fragment_profile
) {
    private val binding: FragmentProfileBinding by viewBinding()

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        INSTANCE.appContainer.storeFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.accept(ProfileEvent.Ui.Init)
        setupRefreshButton()
    }

    override fun render(state: State) {
        when (val dataToRender = state.profileUi) {
            is ScreenState.Content -> {
                val content = dataToRender.content
                binding.user.apply {
                    userNameTextView.text = content.name
                    userNetStatusTextView.text = resources.getString(content.presence.text)
                    userNetStatusTextView.setTextColor(resources.getColor(content.presence.color))
                    Picasso.get().load(content.avatar).into(userLogoImage)
                }
                binding.user.itemProfile.visibility = VISIBLE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = GONE
            }
            is ScreenState.Error -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = VISIBLE
                binding.shimmer.visibility = GONE
            }
            ScreenState.Loading -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = VISIBLE
            }
        }
    }

    private fun setupRefreshButton() {
        binding.error.refreshButton.setOnClickListener {
            store.accept(ProfileEvent.Ui.ReloadProfile)
        }
    }
}