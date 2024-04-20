package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.profile.elm.ProfileCommand as Command
import com.lesa.app.presentation.features.profile.elm.ProfileEffect as Effect
import com.lesa.app.presentation.features.profile.elm.ProfileEvent as Event
import com.lesa.app.presentation.features.profile.elm.ProfileState as State

class ProfileReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: Event.Internal): Any {
        return when (event) {
            is Event.Internal.DataLoaded -> state {
                copy(
                    profileUi = ScreenState.Content(event.profileUi)
                )
            }
            is Event.Internal.Error -> state {
                copy(
                    profileUi = ScreenState.Error
                )
            }
        }
    }

    override fun Result.ui(event: Event.Ui): Any {
        return when (event) {
            Event.Ui.ReloadProfile -> commands {
                +Command.LoadData
            }
            Event.Ui.Init -> commands {
                +Command.LoadData
            }
        }
    }
}