package com.lesa.app.chat

import com.lesa.app.R
import com.lesa.app.chat.date.DateDelegateItem
import com.lesa.app.chat.message.emoji.EmojiView
import com.lesa.app.chat.message.MessageDelegateItem
import com.lesa.app.chat.message.MessageView
import com.lesa.app.compositeAdapter.DelegateItem
import com.lesa.app.model.Message
import java.text.SimpleDateFormat

class ChatDelegateItemFactory() {
    fun makeDelegateItems(
        list: List<Message>,
        userId: Int,
        showEmojiPicker: (Message) -> Unit,
        onSelectEmoji: (message: Message, emojiCode: String) -> Unit
    ) : MutableList<DelegateItem> {
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
                    avatar = R.drawable.avatar,
                    userName = message.senderName,
                    text = message.message,
                    emojiList = message.emojiList.map {
                        EmojiView.Model(
                            emoji = it.emojiCode,
                            count = it.count,
                            isSelected = it.userIds.contains(userId)
                        )
                    },
                    type = message.type,
                    onLongClick = { showEmojiPicker(message) },
                    onEmojiClick = {
                        onSelectEmoji(
                            message,
                            it
                        )
                    },
                    onPlusButtonClick = {
                        showEmojiPicker(message)
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