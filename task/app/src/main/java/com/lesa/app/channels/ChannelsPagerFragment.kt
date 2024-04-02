package com.lesa.app.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.App
import com.lesa.app.Screens
import com.lesa.app.channels.ChannelsFragment.Companion.PAGER_ALL
import com.lesa.app.channels.ChannelsFragment.Companion.PAGER_KEY
import com.lesa.app.channels.ChannelsFragment.Companion.PAGER_SUBSCRIBED
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.databinding.FragmentChannelsPagerBinding
import com.lesa.app.model.Channel
import com.lesa.app.stubChannels

class ChannelsPagerFragment : Fragment() {
    private var _binding: FragmentChannelsPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CompositeAdapter
    private val allStreams = stubChannels
    private var screenType = PAGER_ALL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChannelsPagerBinding.inflate(inflater, container, false)
        screenType = requireArguments().getString(PAGER_KEY) ?: PAGER_ALL
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        setUpRecycleView()
    }

    private fun setUpRecycleView() {
        val delegates: List<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>> = listOf(
            ChannelDelegateAdapter(onClick = { expandChannel(it) }) as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>,
            TopicDelegateAdapter(onClick = { openProfile(it) }) as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>,
        )
        adapter = CompositeAdapter(delegates)
        binding.channelsRecycleView.adapter = adapter
        updateList()
    }

    private fun expandChannel(id: Int) {
        val index = allStreams.indexOfFirst {
            it.id == id
        }
        val isExpanded = allStreams[index].isExpanded
        allStreams[index] = allStreams[index].copy(
            isExpanded = !isExpanded
        )
        updateList()
    }

    private fun openProfile(userId: Int) {
        App.INSTANCE.router.navigateTo(Screens.Chat(topicId = userId))
    }

    private fun updateList() {
        adapter.submitList(
            makeDelegateItems(
                list = createChannelsList(allStreams, screenType)
            )
        )
    }

    private fun makeDelegateItems(
        list: List<Channel>
    ): MutableList<DelegateItem> {
        return ChannelsDelegateItemFactory().makeDelegateItems(list = list)
    }

    private fun createChannelsList(allStreams: List<Channel>, screenType: String) : List<Channel> {
        return if (screenType == PAGER_SUBSCRIBED) {
            val list = allStreams.filter {
                it.isSubscribed
            }
            list
        } else {
            allStreams
        }
    }
}