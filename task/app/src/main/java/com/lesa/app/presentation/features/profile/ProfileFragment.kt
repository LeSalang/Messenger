package com.lesa.app.presentation.features.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.R
import com.lesa.app.databinding.FragmentProfileBinding
import com.lesa.app.di.profile.ProfileComponentViewModel
import com.lesa.app.presentation.elm.ElmBaseFragment
import com.lesa.app.presentation.features.profile.elm.ProfileEvent
import com.lesa.app.presentation.features.profile.elm.ProfileStoreFactory
import com.lesa.app.presentation.utils.LceState
import com.squareup.picasso.Picasso
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.lesa.app.presentation.features.profile.elm.ProfileEffect as Effect
import com.lesa.app.presentation.features.profile.elm.ProfileEvent as Event
import com.lesa.app.presentation.features.profile.elm.ProfileState as State

class ProfileFragment : ElmBaseFragment<Effect, State, Event>(
    R.layout.fragment_profile
) {
    private val binding: FragmentProfileBinding by viewBinding()

    @Inject
    lateinit var storeFactory: ProfileStoreFactory

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        storeFactory.create()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ProfileComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.accept(ProfileEvent.Ui.Init)
        setupRefreshButton()
    }

    override fun render(state: State) {
        when (val dataToRender = state.profileUi) {
            is LceState.Content -> {
                val content = dataToRender.content
                binding.user.apply {
                    userNameTextView.text = content.name
                    userNetStatusTextView.text = resources.getString(R.string.status_active)
                    userNetStatusTextView.setTextColor(resources.getColor(R.color.green))
                    Picasso.get().load(content.avatar).into(userLogoImage)
                }
                binding.user.itemProfile.visibility = VISIBLE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = GONE
            }
            is LceState.Error -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = VISIBLE
                binding.shimmer.visibility = GONE
            }
            LceState.Loading -> {
                binding.user.itemProfile.visibility = GONE
                binding.error.errorItem.visibility = GONE
                binding.shimmer.visibility = VISIBLE
            }
            LceState.Idle -> TODO()
        }
    }

    private fun setupRefreshButton() {
        binding.error.refreshButton.setOnClickListener {
            store.accept(ProfileEvent.Ui.ReloadProfile)
        }
    }
}