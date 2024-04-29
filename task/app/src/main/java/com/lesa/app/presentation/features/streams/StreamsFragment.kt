package com.lesa.app.presentation.features.streams

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.lesa.app.R
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentStreamsBinding
import com.lesa.app.di.streams.StreamsComponentViewModel
import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.elm.ElmBaseFragment
import com.lesa.app.presentation.features.streams.elm.StreamsEvent
import com.lesa.app.presentation.features.streams.elm.StreamsState
import com.lesa.app.presentation.features.streams.elm.StreamsStoreFactory
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamUi
import com.lesa.app.presentation.navigation.Screens
import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.lesa.app.presentation.features.streams.elm.StreamsEffect as Effect
import com.lesa.app.presentation.features.streams.elm.StreamsEvent as Event
import com.lesa.app.presentation.features.streams.elm.StreamsState as State

class StreamsFragment : ElmBaseFragment<Effect, State, Event>(
    R.layout.fragment_streams
) {
    private val binding: FragmentStreamsBinding by viewBinding()
    private lateinit var adapter: CompositeAdapter
    private lateinit var type: StreamType

    @Inject
    lateinit var storeFactory: StreamsStoreFactory

    @Inject
    lateinit var router: Router

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        storeFactory.create()
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<StreamsComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = requireArguments().getParcelable(STREAM_TYPE_KEY) ?: StreamType.SUBSCRIBED
        store.accept(StreamsEvent.Ui.Init(type))
        setUpViews()

        (parentFragment as? StreamsContainerFragment)?.setSearchListener { query ->
            store.accept(
                StreamsEvent.Ui.Search(
                    query = query,
                    streamType = type
                )
            )
        }
    }

    override fun render(state: StreamsState) {
        when (val dataToRender = state.streamUi) {
            is ScreenState.Content -> {
                val list = dataToRender.content
                val expandedStreamId = state.expandedChannelId
                binding.apply {
                    channelsRecycleView.visibility = VISIBLE
                    error.errorItem.visibility = GONE
                    shimmerLayout.visibility = GONE
                }
                updateList(streams = list, expandedStreamId = expandedStreamId)
            }
            ScreenState.Error -> {
                binding.apply {
                    channelsRecycleView.visibility = GONE
                    error.errorItem.visibility = VISIBLE
                    shimmerLayout.visibility = GONE
                }
            }
            ScreenState.Loading -> {
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
        setupRefreshButton()
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                ChannelDelegateAdapter(
                    onClick = {
                        store.accept(StreamsEvent.Ui.ExpandStream(streamId = it))
                    }
                ),
                TopicDelegateAdapter(
                    onClick = {
                        openChat(it)
                    }
                )
            )
        )
        binding.channelsRecycleView.adapter = adapter
    }

    private fun openChat(topic: Topic) {
        router.navigateTo(Screens.Chat(topic = topic))
    }

    private fun updateList(streams: List<StreamUi>, expandedStreamId: Int?) {
        val items = ChannelsDelegateItemFactory().makeDelegateItems(
            list = streams, expandedChannelId = expandedStreamId
        )
        adapter.submitList(items)
    }

    private fun setupRefreshButton() {
        binding.error.refreshButton.setOnClickListener {
            store.accept(StreamsEvent.Ui.ReloadStreams(streamType = type))
        }
    }

    companion object {
        private const val STREAM_TYPE_KEY = "stream_type_key"

        fun getNewInstance(streamType: StreamType): StreamsFragment {
            return StreamsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STREAM_TYPE_KEY, streamType)
                }
            }
        }
    }
}