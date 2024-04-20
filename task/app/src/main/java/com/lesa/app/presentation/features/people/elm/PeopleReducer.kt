package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.features.people.elm.PeopleEvent
import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.people.elm.PeopleCommand as Command
import com.lesa.app.presentation.features.people.elm.PeopleEffect as Effect
import com.lesa.app.presentation.features.people.elm.PeopleEvent as Event
import com.lesa.app.presentation.features.people.elm.PeopleState as State

class PeopleReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: PeopleEvent.Internal): Any {
        return when (event) {
            is PeopleEvent.Internal.DataLoaded -> state {
                copy(
                    peopleUi = ScreenState.Content(event.userUiList)
                )
            }
            PeopleEvent.Internal.Error -> state {
                copy(
                    peopleUi = ScreenState.Error
                )
            }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui): Any {
        return when (event) {
            PeopleEvent.Ui.Init -> commands {
                +Command.LoadData
            }
            PeopleEvent.Ui.ReloadPeople -> commands {
                +Command.LoadData
            }
        }
    }
}