package com.lesa.app.channels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.model.Stream
import com.lesa.app.repositories.StreamsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val streamsRepository: StreamsRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ChannelsScreenState> =
        MutableStateFlow(ChannelsScreenState.Initial)
    val state: StateFlow<ChannelsScreenState>
        get() = _state.asStateFlow()
    val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private var allStreams = mutableListOf<Stream>()

    var isTitleSearch: Boolean = false
    private var channelScreenType = ChannelsScreenType.SUBSCRIBED

    init {
        listenToSearchQuery()
    }

    private fun listenToSearchQuery() {
        searchQuery
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(500)
            .mapLatest {
                _state.value = ChannelsScreenState.Loading
                search(it)
            }
            .flowOn(Dispatchers.Default)
            .onEach { _state.value = it }
            .launchIn(viewModelScope)

    }

    private fun search(query: String): ChannelsScreenState {
        runCatchingNonCancellation {
            searchChannels(query = query)
        }.fold(
            onSuccess = {
                return ChannelsScreenState.DataLoaded(list = it)
            },
            onFailure = {
                return ChannelsScreenState.Error
            }
        )
    }

    private fun searchChannels(query: String): List<Stream> { // TODO: move to repository
        val refactoredQuery = query.trim(' ')
        val list = allStreams.filter { channel ->
            channel.topics.any { topic ->
                topic.name.contains(refactoredQuery, ignoreCase = true)
            } || channel.name.contains(refactoredQuery, ignoreCase = true)
        }
        return list
    }

    private fun filterChannels() {
        when (state.value) {
            is ChannelsScreenState.DataLoaded -> {
                val list = when (channelScreenType) {
                    ChannelsScreenType.SUBSCRIBED -> {
                        allStreams.filter {
                            it.isSubscribed
                        }.toMutableList()
                    }

                    ChannelsScreenType.ALL -> allStreams
                }
                _state.value = ChannelsScreenState.DataLoaded(list = list)
            }

            ChannelsScreenState.Error -> return
            ChannelsScreenState.Initial -> return
            ChannelsScreenState.Loading -> return
        }
    }

    fun loadChannels() {
        viewModelScope.launch {
            _state.value = ChannelsScreenState.Loading
            try {
                allStreams = streamsRepository.getAllStreams().toMutableList()
                _state.value = ChannelsScreenState.DataLoaded(allStreams)
                filterChannels()
            } catch (e: Exception) {
                _state.value = ChannelsScreenState.Error
            }
        }
    }

    fun setUpScreenType(screenType: ChannelsScreenType) {
        channelScreenType = screenType
        filterChannels()
    }

    fun expandChannel(id: Int) {
        val state = this.state.value
        when (state) {
            is ChannelsScreenState.DataLoaded -> {
                val list = state.list
                val expandedChannelId = if (state.expandedChannelId == id) null else id
                _state.value = ChannelsScreenState.DataLoaded(list = list, expandedChannelId = expandedChannelId)
            }
            ChannelsScreenState.Error -> return
            ChannelsScreenState.Initial -> return
            ChannelsScreenState.Loading -> return
        }
    }
}

class ChannelsViewModelFactory(
    private val context: Context
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = context.applicationContext as App
        val streamRepository = application.appContainer.streamsRepository
        return ChannelsViewModel(
            streamsRepository = streamRepository
        ) as T
    }
}

enum class ChannelsScreenType(val title: Int) {
    SUBSCRIBED(title = R.string.channels_subscribed),
    ALL(title = R.string.channels_all_streams)
}

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

