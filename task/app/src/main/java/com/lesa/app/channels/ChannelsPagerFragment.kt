package com.lesa.app.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.Screens
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentChannelsPagerBinding
import kotlinx.coroutines.launch

class ChannelsPagerFragment : Fragment() {
    private val binding: FragmentChannelsPagerBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private lateinit var adapter: CompositeAdapter
    private val viewModel: ChannelsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()

        lifecycleScope.launch {
            viewModel.state.collect(::render)
        }
    }

    private fun render(state: ChannelsScreenState) {
        when (state) {
            is ChannelsScreenState.DataLoaded -> {
                binding.apply {
                    channelsRecycleView.visibility = VISIBLE
                    renderStatusMessage.visibility = GONE
                    shimmerLayout.visibility = GONE
                }
                updateList(state = state)
            }

            ChannelsScreenState.Error -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    renderStatusMessage.visibility = VISIBLE
                    shimmerLayout.visibility = GONE
                    renderStatusMessage.text = resources.getString(R.string.error)
                }
            }

            ChannelsScreenState.Initial -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    renderStatusMessage.visibility = GONE
                    shimmerLayout.visibility = GONE
                }
            }

            ChannelsScreenState.Loading -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    renderStatusMessage.visibility = GONE
                    shimmerLayout.visibility = VISIBLE
                }
            }
        }
    }

    private fun setUpViews() {
        setUpRecycleView()
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                ChannelDelegateAdapter(onClick = { viewModel.expandChannel(it) }),
                TopicDelegateAdapter(onClick = { openProfile(it) })
            )
        )
        binding.channelsRecycleView.adapter = adapter

    }

    private fun openProfile(userId: Int) {
        App.INSTANCE.router.navigateTo(Screens.Chat(topicId = userId))
    }

    private fun updateList(state: ChannelsScreenState.DataLoaded) {
        val items = ChannelsDelegateItemFactory().makeDelegateItems(
            list = state.list, expandedChannelId = state.expandedChannelId
        )
        adapter.submitList(items)
    }

}