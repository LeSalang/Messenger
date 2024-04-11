package com.lesa.app

import com.lesa.app.chat.message.MessageView
import com.lesa.app.model.Channel
import com.lesa.app.model.Emoji
import com.lesa.app.model.Message
import com.lesa.app.model.Topic
import com.lesa.app.model.User
import com.lesa.app.model.UserNetStatus
import java.util.Date

val currentDate = Date()
val currentDateInMills = currentDate.time

val stubChannels: MutableList<Channel>
    get() = mutableListOf(
        Channel(
            id = 7730, name = "Orville Wong", topics = listOf(
                Topic(id = 7591, name = "Merlin Navarro", count = 6097, color = R.color.dark_blue),
                Topic(id = 3372, name = "Basil Pratt", count = 3247, color = R.color.dark_green),
                Topic(id = 2703, name = "Louie O'Neil", count = 2324, color = R.color.dark_orange),
                Topic(id = 3, name = "Tuck", count = 3, color = R.color.dark_purple),
            ), isSubscribed = true
        ), Channel(
            id = 5000, name = "Chelsea Hewitt", topics = listOf(
                Topic(id = 2703, name = "Louie O'Neil", count = 2324, color = R.color.dark_orange),
                Topic(id = 3, name = "Tuck", count = 3, color = R.color.dark_red),
            ), isSubscribed = true
        ), Channel(
            id = 5480,
            name = "Warren Olson",
            topics = listOf(), isSubscribed = true
        ), Channel(
            id = 2277, name = "Rene Travis", topics = listOf(
                Topic(id = 7591, name = "Merlin Navarro", count = 6097, color = R.color.dark_blue),
                Topic(id = 3372, name = "Basil Pratt", count = 3247, color = R.color.dark_green),
                Topic(id = 2703, name = "Louie O'Neil", count = 2324, color = R.color.dark_orange),
                Topic(id = 3, name = "Tuck", count = 3, color = R.color.dark_purple),
            ), isSubscribed = false
        ), Channel(
            id = 7076, name = "Leroy Wolf", topics = listOf(
                Topic(id = 2703, name = "Louie O'Neil", count = 2324, color = R.color.dark_orange),
                Topic(id = 3, name = "Tuck", count = 3, color = R.color.dark_purple),
            ), isSubscribed = false
        ), Channel(
            id = 6987, name = "Gena Gibson", topics = listOf(
                Topic(id = 7591, name = "Merlin Navarro", count = 6097, color = R.color.dark_blue)
            ), isSubscribed = false
        )
    )

val stubPeople: List<User>
    get() = mutableListOf(
        User(
            id = 7484,
            name = "Josh Heath",
            email = "jamie.caldwell@example.com",
            avatar = R.drawable.avatar,
            netStatus = UserNetStatus.ACTIVE
        ), User(
            id = 2332,
            name = "Darius Shepherd",
            email = "marquis.benjamin@example.com",
            avatar = R.drawable.avatar,
            netStatus = UserNetStatus.IDLE
        ), User(
            id = 1161,
            name = "Erwin Koch",
            email = "agustin.harper@example.com",
            avatar = R.drawable.avatar,
            netStatus = UserNetStatus.OFFLINE
        ), User(
            id = 4158,
            name = "Kasey Hardin",
            email = "dewayne.ryan@example.com",
            avatar = R.drawable.avatar,
            netStatus = UserNetStatus.ACTIVE
        ), User(
            id = 7738,
            name = "Greta Bell",
            email = "deborah.pittman@example.com",
            avatar = R.drawable.avatar,
            netStatus = UserNetStatus.ACTIVE
        )
    )

val stubMessageList: List<Message>
    get() = mutableListOf(
        Message(
            id = 3481,
            senderName = "Джэйсон Стэтхэм",
            message = "Никогда не сдавайтесь, идите к своей цели! А если будет сложно – сдавайтесь.",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC12", userIds = setOf(1000), count = 1
                ),
                Emoji(
                    emojiCode = "\uD83D\uDE0D", userIds = setOf(1000), count = 1
                ),
                Emoji(
                    emojiCode = "\uD83D\uDCAA", userIds = setOf(123, 1234, 1000), count = 3
                ),
                Emoji(
                    emojiCode = "\uD83D\uDE00", userIds = setOf(123, 1234, 1000), count = 3
                ),
                Emoji(
                    emojiCode = "❤\uFE0F", userIds = setOf(123, 1234, 1000), count = 3
                ),
                Emoji(
                    emojiCode = "\uD83E\uDD13", userIds = setOf(123, 1234, 1000), count = 3
                ),
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 5),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 12,
            senderName = "Джэйсон Стэтхэм",
            message = "Если заблудился в лесу, иди домой.",
            emojiList = listOf(),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 4),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 1234,
            senderName = "Джэйсон Стэтхэм",
            message = "Запомни: всего одна ошибка – и ты ошибся.",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDE01", userIds = setOf(123, 1234, 1000), count = 3
                ), Emoji(
                    emojiCode = "\uD83D\uDCA9", userIds = setOf(123, 1234, 1000), count = 3
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 1),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 12345,
            senderName = "Джэйсон Стэтхэм",
            message = "Я вообще делаю что хочу. Хочу импланты — звоню врачу.",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC12", userIds = setOf(123, 1234, 1000), count = 3
                ), Emoji(
                    emojiCode = "\uD83D\uDE48", userIds = setOf(123, 1234, 1000), count = 3
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 8),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 67,
            senderName = "Джэйсон Стэтхэм",
            message = "Делай, как надо. Как не надо, не делай.",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC12", userIds = setOf(123, 1000), count = 2
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 5),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 78,
            senderName = "Джэйсон Стэтхэм",
            message = "Жи-ши пиши от души.",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC3A", userIds = setOf(123, 1000), count = 2
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 4),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 245,
            senderName = "Я",
            message = "Спасибо за совет)))",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC4D", userIds = setOf(123), count = 1
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 5),
            type = MessageView.Model.Type.OUTGOING
        ), Message(
            id = 89,
            senderName = "Я",
            message = "Жиза...",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC4D", userIds = setOf(123), count = 1
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 6),
            type = MessageView.Model.Type.OUTGOING
        ), Message(
            id = 890,
            senderName = "Я",
            message = "понял)",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC4D", userIds = setOf(123), count = 1
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 4),
            type = MessageView.Model.Type.OUTGOING
        ), Message(
            id = 891,
            senderName = "Я",
            message = "нипонял(",
            emojiList = listOf(
                Emoji(
                    emojiCode = "\uD83D\uDC4D", userIds = setOf(123), count = 1
                )
            ),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 4),
            type = MessageView.Model.Type.OUTGOING
        ), Message(
            id = 892,
            senderName = "Я",
            message = "Хватит мне писать!",
            emojiList = listOf(),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 1),
            type = MessageView.Model.Type.OUTGOING
        )
    )
