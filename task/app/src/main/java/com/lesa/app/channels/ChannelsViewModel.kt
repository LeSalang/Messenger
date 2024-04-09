package com.lesa.app.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesa.app.R
import com.lesa.app.model.Channel
import com.lesa.app.stubChannels
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

class ChannelsViewModel : ViewModel() {
    private val _state: MutableStateFlow<ChannelsScreenState> =
        MutableStateFlow(ChannelsScreenState.Initial)
    val state: StateFlow<ChannelsScreenState>
        get() = _state.asStateFlow()
    val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private var allChannels = mutableListOf<Channel>()

    var isTitleSearch: Boolean = false
    private var channelScreenType = ChannelsScreenType.SUBSCRIBED

    init {
        listenToSearchQuery()
    }

    private fun listenToSearchQuery() {
        searchQuery.filter {
                it.isNotEmpty()
            }.distinctUntilChanged().debounce(500).mapLatest {
                _state.value = ChannelsScreenState.Loading
                search(it)
            }.flowOn(Dispatchers.Default).onEach {
                _state.value = it
            }.launchIn(viewModelScope)

    }

    private suspend fun search(query: String): ChannelsScreenState {
        runCatchingNonCancellation {
            searchChannels(query = query)
        }.fold(onSuccess = {
            return ChannelsScreenState.DataLoaded(list = it)
        }, onFailure = {
            return ChannelsScreenState.Error
        })
    }

    private suspend fun searchChannels(query: String): List<Channel> { // TODO: move to repository
        val refactoredQuery = query.trim(' ')
        val list = allChannels.filter { channel ->
            channel.topics.any { topic ->
                topic.name.contains(refactoredQuery, ignoreCase = true)
            } || channel.name.contains(refactoredQuery, ignoreCase = true)
        }
        delay(3000)
        return list
    }

    private fun filterChannels() {
        when (state.value) {
            is ChannelsScreenState.DataLoaded -> {
                val list = when (channelScreenType) {
                    ChannelsScreenType.SUBSCRIBED -> {
                        allChannels.filter {
                            it.isSubscribed
                        }.toMutableList()
                    }

                    ChannelsScreenType.ALL -> allChannels
                }
                _state.value = ChannelsScreenState.DataLoaded(list = list)
            }

            ChannelsScreenState.Error -> return
            ChannelsScreenState.Initial -> return
            ChannelsScreenState.Loading -> return
        }
    }

    suspend fun loadChannels() {
        allChannels = stubChannels
        delay(500) // imitation of downloading
        if ((0..2).random() == 0) {
            _state.value = ChannelsScreenState.Error
        } else {
            _state.value = ChannelsScreenState.DataLoaded(list = allChannels)
        }
        filterChannels()
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

