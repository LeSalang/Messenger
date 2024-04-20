package com.lesa.app.presentation.features.people

import com.lesa.app.presentation.features.people.PeopleEvent
import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.people.PeopleCommand as Command
import com.lesa.app.presentation.features.people.PeopleEffect as Effect
import com.lesa.app.presentation.features.people.PeopleEvent as Event
import com.lesa.app.presentation.features.people.PeopleState as State

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