package com.lesa.app.channels

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.Screens
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentChannelsPagerBinding
import com.lesa.app.model.Topic
import kotlinx.coroutines.launch

class ChannelsPagerFragment : Fragment(R.layout.fragment_channels_pager) {
    private val binding: FragmentChannelsPagerBinding by viewBinding()
    private lateinit var adapter: CompositeAdapter
    private val viewModel: ChannelsViewModel by viewModels(
        ownerProducer = {
            parentFragment ?: this
        }
    ) { ChannelsViewModelFactory(requireContext()) }

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
                    error.errorItem.visibility = GONE
                    shimmerLayout.visibility = GONE
                }
                updateList(state = state)
            }
            ChannelsScreenState.Error -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    error.errorItem.visibility = VISIBLE
                    shimmerLayout.visibility = GONE
                    error.refreshButton.setOnClickListener {

                        TODO()

                    }
                }
            }
            ChannelsScreenState.Initial -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    error.errorItem.visibility = GONE
                    shimmerLayout.visibility = GONE
                }
            }
            ChannelsScreenState.Loading -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    error.errorItem.visibility = GONE
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
                TopicDelegateAdapter(onClick = { openChat(it) })
            )
        )
        binding.channelsRecycleView.adapter = adapter

    }

    private fun openChat(topic: Topic) {
        App.INSTANCE.router.navigateTo(Screens.Chat(topic = topic))
    }

    private fun updateList(state: ChannelsScreenState.DataLoaded) {
        val items = ChannelsDelegateItemFactory().makeDelegateItems(
            list = state.list, expandedChannelId = state.expandedChannelId
        )
        adapter.submitList(items)
    }
}