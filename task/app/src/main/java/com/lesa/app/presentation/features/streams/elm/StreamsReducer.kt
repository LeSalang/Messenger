package com.lesa.app.presentation.features.streams.elm

import android.util.Log
import com.lesa.app.presentation.features.streams.elm.StreamsEvent
import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.streams.elm.StreamsCommand as Command
import com.lesa.app.presentation.features.streams.elm.StreamsEffect as Effect
import com.lesa.app.presentation.features.streams.elm.StreamsEvent as Event
import com.lesa.app.presentation.features.streams.elm.StreamsState as State

class StreamsReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: Event.Internal): Any {
        Log.d("myLog", "result internal $event")
        return when (event) {
            is Event.Internal.DataLoaded -> state {
                copy(
                    streamUi = ScreenState.Content(event.streamUiList)
                )
            }
            Event.Internal.Error -> state {
                copy(
                    streamUi = ScreenState.Error
                )
            }
        }
    }

    override fun Result.ui(event: Event.Ui): Any {
        Log.d("myLog", "result ui $event")
        return when (event) {
            is Event.Ui.Init -> commands {
                +Command.LoadData(streamType = event.streamType)
            }
            is Event.Ui.ReloadStreams -> commands {
                +Command.LoadData(streamType = event.streamType)
            }
            is StreamsEvent.Ui.ExpandStream -> state {
                copy(
                    expandedChannelId = if (event.streamId == this.expandedChannelId) {
                        null
                    } else {
                        event.streamId
                    }
                )
            }
        }
    }
}