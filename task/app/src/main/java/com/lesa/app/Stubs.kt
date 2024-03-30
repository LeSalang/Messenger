package com.lesa.app

import com.lesa.app.chat.message.MessageView
import com.lesa.app.model.Emoji
import com.lesa.app.model.Message
import java.util.Date

val currentDate = Date()
val currentDateInMills = currentDate.time

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
            id = 3481,
            senderName = "Джэйсон Стэтхэм",
            message = "Если заблудился в лесу, иди домой.",
            emojiList = listOf(),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 4),
            type = MessageView.Model.Type.INCOMING
        ), Message(
            id = 3481,
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
            id = 3481,
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
            id = 3481,
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
            id = 3481,
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
            id = 245,
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
            id = 245,
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
            id = 245,
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
            id = 245,
            senderName = "Я",
            message = "Хватит мне писать!",
            emojiList = listOf(),
            date = Date(currentDateInMills - 1000 * 60 * 60 * 24 * 1),
            type = MessageView.Model.Type.OUTGOING
        )
    )
