package com.lesa.app.chat

import com.lesa.app.chat.date.DateDelegateItem
import com.lesa.app.chat.message.MessageDelegateItem
import com.lesa.app.chat.message.MessageView
import com.lesa.app.chat.message.emoji.EmojiView
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.model.Message
import java.text.SimpleDateFormat

class ChatDelegateItemFactory {
    fun makeDelegateItems(
        list: List<Message>,
    ): MutableList<DelegateItem> {
        val formatter = SimpleDateFormat("yyyyMMdd")
        val messagesGroupedByDates = list.groupBy {
            formatter.format(it.date)
        }.toSortedMap()
        val result = mutableListOf<DelegateItem>()
        messagesGroupedByDates.forEach { (date, messages) ->
            result.add(
                DateDelegateItem(messages[0].date)
            )
            messages.forEach { message ->
                val itemModel = MessageView.Model(
                    id = message.id,
                    avatar = message.avatar,
                    userName = message.senderName,
                    text = message.content,
                    emojiList = message.reactions
                        .map {
                            EmojiView.Model(
                                emoji = String(Character.toChars(it.key.toInt(16))),
                                count = it.value.count,
                                isSelected = it.value.isOwn
                            )
                        }
                        .sortedByDescending { it.count },
                    type = if (message.isOwn) {
                        MessageView.Model.Type.OUTGOING
                    } else {
                        MessageView.Model.Type.INCOMING
                    }
                )
                result.add(
                    MessageDelegateItem(itemModel)
                )
            }
        }
        return result
    }
}