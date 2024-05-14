package com.lesa.app.presentation.features.chat.models

import com.lesa.app.domain.model.Emoji
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.Topic

object ChatMapper {
    fun map(message: Message) : MessageUi {
        return MessageUi(
            id = message.id,
            avatar = message.senderAvatar,
            content = message.content,
            senderName = message.senderName,
            reactions = reactionMapToUi(message.reactions),
            date = message.date,
            topic = message.topic,
            isOwn = message.isOwn
        )
    }

    fun topicToUiMap(topic: Topic) : TopicUi {
        return TopicUi(
            name = topic.name,
            color = topic.color,
            streamName = topic.streamName,
            streamId = topic.streamId
        )
    }

    private fun reactionMapToUi(reaction: Map<String, Emoji>) : Map<String, EmojiUi> {
        return reaction.mapValues {
            it.key
            it.value.toUi()
        }
    }

    private fun Emoji.toUi() : EmojiUi {
        return EmojiUi(
            emojiCode = emojiCode,
            emojiName = emojiName,
            isOwn = isOwn,
            count = count
        )
    }
}