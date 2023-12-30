/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-miyoushe.
 *
 * simbot-component-miyoushe is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-miyoushe is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-miyoushe,
 * If not, see <https://www.gnu.org/licenses/>.
 */

@file:JvmName("VillaMessages")

package love.forte.simbot.component.miyoushe.message

import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.component.miyoushe.uploadToOssData
import love.forte.simbot.literal
import love.forte.simbot.message.*
import love.forte.simbot.miyoushe.api.image.GetUploadImageParamsApi
import love.forte.simbot.miyoushe.api.msg.*
import love.forte.simbot.miyoushe.api.msg.QuoteInfo.Companion.toQuoteInfo
import love.forte.simbot.miyoushe.event.SendMessage
import love.forte.simbot.resources.ByteArrayResource
import love.forte.simbot.resources.FileResource
import love.forte.simbot.resources.PathResource
import love.forte.simbot.resources.URLResource
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

private fun interface SendMessageApiCreator {
    suspend fun api(roomId: ULong): SendMessageApi
}

@InternalSimbotApi
public suspend fun Message.sendTo(
    bot: VillaBot,
    villaId: String,
    roomId: ULong,
    autoQuote: QuoteInfo? = null
): List<SendMessageResult> {
    val apiList = toSendApi(bot, villaId, roomId, autoQuote)
    // 没有可回复的内容：准备用于发送的API列表为空
    check(apiList.isNotEmpty()) { "Nothing to reply: the list of APIs ready to be used for sending is empty" }

    return apiList.map {
        println("api: $it")
        it.requestDataBy(bot, villaId)
    }
}

@InternalSimbotApi
public suspend fun MessageContent.sendTo(
    bot: VillaBot,
    villaId: String,
    roomId: ULong,
    autoQuote: QuoteInfo? = null
): List<SendMessageResult> {
    val apiList = toSendApi(bot, villaId, roomId, autoQuote)
    // 没有可回复的内容：准备用于发送的API列表为空
    check(apiList.isNotEmpty()) { "Nothing to reply: the list of APIs ready to be used for sending is empty" }

    return apiList.map {
        println("api: $it")
        it.requestDataBy(bot, villaId)
    }
}


@InternalSimbotApi
public suspend fun MessageContent.toSendApi(
    bot: VillaBot,
    villaId: String,
    roomId: ULong,
    autoQuote: QuoteInfo? = null
): List<SendMessageApi> {
    if (this is VillaReceivedMessageContent) {
        val content = this.source.extendData.content
        val objectName = source.extendData.objectName
        val api = SendMessageApi.create(
            roomId = roomId,
            objectName = when (objectName) {
                SendMessage.OBJECT_NAME_TEXT -> TextMsgContent.OBJECT_NAME
                SendMessage.OBJECT_NAME_POST -> PostMsgContent.OBJECT_NAME
                else -> error("Unknown extendData.objectName: $objectName")
            },
            msgContent = content
        )

        return listOf(api)
    }

    return messages.toSendApi(bot, villaId, roomId, autoQuote)
}


@InternalSimbotApi
public suspend fun Message.toSendApi(
    bot: VillaBot,
    villaId: String,
    roomId: ULong,
    autoQuote: QuoteInfo? = null
): List<SendMessageApi> {
    /* 元素可能的类型：
    * - [SendMessageApiCreator]
    * - [MsgContentInfo]
    */
    suspend fun List<Any>.mapToApi(): List<SendMessageApi> {
        return map {
            when (it) {
                is SendMessageApiCreator -> it.api(roomId)
                is MsgContentInfo<*> -> SendMessageApi.create(roomId, it, bot.source.apiDecoder)
                else -> error("Error type: $it (${it::class})")
            }
        }
    }

    if (this is Messages) {
        if (isEmpty()) return emptyList()
        if (size == 1) return first().toMsgContents0(bot, villaId, autoQuote).mapToApi()

        // messages
        return toMsgContents0(this, bot, villaId, autoQuote).mapToApi()
    }

    // is element
    return (this as Message.Element<*>).toMsgContents0(bot, villaId, autoQuote).mapToApi()
}

private suspend fun Image<*>.toImgMsgContentInfo(
    bot: VillaBot,
    villaId: String,
    autoQuote: QuoteInfo?
): MsgContentInfo<ImgMsgContent> {
    when (this) {
        is VillaImgMsgContent -> {
            return MsgContentInfo(content = source, quoteInfo = autoQuote)
        }

        // others, use resource.
        else -> when (val resource = resource()) {
            //region standards
            is URLResource -> {
                return MsgContentInfo(
                    content = ImgMsgContent(url = resource.url.toString()),
                    quoteInfo = autoQuote
                )
            }
            // upload.
            is ByteArrayResource -> {
                val bytes = resource.bytes
                val uploaded = uploadToOssData(
                    bot,
                    villaId,
                    bytes,
                    GetUploadImageParamsApi.ext(bytes) ?: GetUploadImageParamsApi.EXT_PNG
                )

                return MsgContentInfo(
                    content = ImgMsgContent(url = uploaded.url),
                    quoteInfo = autoQuote
                )
            }

            is FileResource -> {
                val file = resource.file
                require(file.isFile) { "Img file '$file' is not a file(isFile = false)" }
                require(file.canRead()) { "Img file '$file' can't be read(canRead = false)" }

                val uploaded = uploadToOssData(
                    bot,
                    villaId,
                    file,
                    GetUploadImageParamsApi.ext(file.name) ?: GetUploadImageParamsApi.EXT_PNG
                )

                return MsgContentInfo(
                    content = ImgMsgContent(url = uploaded.url),
                    quoteInfo = autoQuote
                )
            }

            is PathResource -> {
                val path = resource.path
                require(path.isRegularFile()) { "Img file '$path' is not an regular file (isRegularFile = false)" }
                require(path.isReadable()) { "Img file '$path' is not readable (isReadable = false)" }

                val uploaded = uploadToOssData(
                    bot,
                    villaId,
                    path,
                    GetUploadImageParamsApi.ext(path.name) ?: GetUploadImageParamsApi.EXT_PNG
                )

                return MsgContentInfo(
                    content = ImgMsgContent(url = uploaded.url),
                    quoteInfo = autoQuote
                )
            }
            //endregion
            else -> {
                // upload.
                val bytes = resource.openStream().buffered().use { sb -> sb.readBytes() }

                val uploaded = uploadToOssData(
                    bot,
                    villaId,
                    bytes,
                    GetUploadImageParamsApi.ext(bytes) ?: GetUploadImageParamsApi.EXT_PNG
                )

                return MsgContentInfo(
                    content = ImgMsgContent(url = uploaded.url),
                    quoteInfo = autoQuote
                )

            }
        }
    }
}

private suspend fun Message.Element<*>.toMsgContents0(
    bot: VillaBot,
    villaId: String,
    autoQuote: QuoteInfo?
): List<Any> {
    fun returnListOf(value: MsgContentInfo<*>): List<Any> = listOf(value)
    fun returnListOf(value: SendMessageApiCreator): List<Any> = listOf(value)

    when (this) {
        is StandardMessage<*> -> when (this) {
            is At ->
                return returnListOf(
                    MsgContentInfo(
                        content = TextMsgContent.EMPTY,
                        mentionedInfo = MentionedInfo.mentionMembers(listOf(target.literal)),
                        quoteInfo = autoQuote
                    )
                )

            AtAll -> return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent.EMPTY,
                    mentionedInfo = MentionedInfo.mentionAll(),
                    quoteInfo = autoQuote
                )
            )

            is PlainText -> return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent(text = text),
                    quoteInfo = autoQuote
                )
            )

            is Image -> {
                return returnListOf(toImgMsgContentInfo(bot, villaId, autoQuote))
            }

            else -> {
                // Face, Emoji
                return emptyList()
            }
        }

        is VillaLink -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent(
                        text = this.text,
                        entities = listOf(TextMsgContent.Entity(0, text.length, link))
                    ),
                    quoteInfo = autoQuote
                )
            )
        }

        is VillaSendMessage -> {
            return returnListOf { roomId -> createApi(roomId) }
        }

        is VillaStyleText -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent(
                        text = this.text,
                        entities = listOf(TextMsgContent.Entity(0, text.length, style))
                    ),
                    quoteInfo = autoQuote
                )
            )
        }

        is VillaVillaRoomLink -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent(
                        text = name,
                        entities = listOf(TextMsgContent.Entity(0, name.length, link))
                    ),
                    quoteInfo = autoQuote
                )
            )
        }


        is VillaPanel -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent.EMPTY,
                    panel = source,
                    quoteInfo = autoQuote
                )
            )
        }

        is VillaQuote -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent.EMPTY,
                    quoteInfo = source
                )
            )
        }

        is VillaQuoteMessage -> {
            return returnListOf(
                MsgContentInfo(
                    content = TextMsgContent.EMPTY,
                    quoteInfo = source.toQuoteInfo()
                )
            )
        }
    }

    return emptyList()
}

private class MultiMessagesBuilder(private val autoQuote: QuoteInfo?) {
    private val result = mutableListOf<Any>()
    var currentBuilder: MsgContentInfoBuilder<*, *>? = null

    inline fun currentOrNew(new: () -> MsgContentInfoBuilder<*, *>): MsgContentInfoBuilder<*, *> {
        return currentBuilder ?: new().also { currentBuilder = it }
    }

    fun currentOrNewText(): MsgContentInfoBuilder<*, *> {
        return currentOrNew { MsgContentInfo.textBuilder() }
    }

    fun textOrNew(): MsgContentInfoBuilder<TextMsgContentBuilder, TextMsgContent> =
        checkOrNew { MsgContent.textBuilder() }

    fun imgOrNew(): MsgContentInfoBuilder<ImgMsgContentBuilder, ImgMsgContent> =
        checkOrNew { MsgContent.imgBuilder() }

    fun postOrNew(): MsgContentInfoBuilder<PostMsgContentBuilder, PostMsgContent> =
        checkOrNew { MsgContent.postBuilder() }

    fun newText(): MsgContentInfoBuilder<TextMsgContentBuilder, TextMsgContent> =
        buildAndNew(MsgContentInfo.textBuilder())

    fun newImg(): MsgContentInfoBuilder<ImgMsgContentBuilder, ImgMsgContent> =
        buildAndNew(MsgContentInfo.imgBuilder())

    fun newPost(): MsgContentInfoBuilder<PostMsgContentBuilder, PostMsgContent> =
        buildAndNew(MsgContentInfo.postBuilder())

    @Suppress("UNCHECKED_CAST")
    inline fun <reified B : MsgContent.Builder<C>, C : MsgContent> checkOrNew(new: () -> B): MsgContentInfoBuilder<B, C> {
        with(currentBuilder) {
            if (this == null) return MsgContentInfoBuilder(new()).also { currentBuilder = it }
            if (builder is B) return (this as MsgContentInfoBuilder<B, C>).also { currentBuilder = it }

            if (!isBuilderUsed) {
                // builder 尚未被使用，复制可能存在的 mentionedInfo,quoteInfo,panel
                return buildAndNew(MsgContentInfoBuilder(new()).apply {
                    mentionedInfo = this@with.mentionedInfo
                    quoteInfo = this@with.quoteInfo
                    panel = this@with.panel
                })
            }

            return buildAndNew(MsgContentInfoBuilder(new()))
        }
    }

    fun <B : MsgContentInfoBuilder<*, *>> buildAndNew(newBuilder: B): B {
        currentBuilder?.also { cb ->
            if (autoQuote != null && cb.quoteInfo == null) {
                cb.quoteInfo = autoQuote
            }

            cb.build().also { result.add(it) }
        }
        currentBuilder = newBuilder
        return newBuilder
    }

    fun buildAndSetNull() {
        currentBuilder?.build()?.also { result.add(it) }
        currentBuilder = null
    }

    fun appendToResult(value: SendMessageApiCreator) = result.add(value)
    fun appendToResult(value: MsgContentInfo<*>) = result.add(value)

    fun buildAndAppendToResult(value: SendMessageApiCreator) {
        buildAndSetNull()
        appendToResult(value)
    }

    fun buildAndAppendToResult(value: MsgContentInfo<*>) {
        buildAndSetNull()
        appendToResult(value)
    }

    fun closure(): List<Any> {
        currentBuilder?.also { b ->
            if (autoQuote != null && b.quoteInfo == null) {
                b.quoteInfo = autoQuote
            }

            result.add(b.build())
        }
        return result
    }
}

/**
 * [messages] 至少有2个元素
 *
 * 返回值：
 * - [SendMessageApiCreator]
 * - [MsgContentInfo]
 */
private suspend fun toMsgContents0(
    messages: Messages,
    bot: VillaBot,
    villaId: String,
    autoQuote: QuoteInfo?
): List<Any> {
    val builder = MultiMessagesBuilder(autoQuote)

    for (message in messages) {
        when (message) {
            is StandardMessage -> when (message) {
                is At -> {
                    var text = builder.textOrNew()
                    if (text.mentionedInfo?.type == MentionedInfo.TYPE_MENTION_ALL) {
                        text = builder.newText()
                    }

                    text.mentionedInfo {
                        userIdList.add(message.target.literal)
                    }
                }

                AtAll -> {
                    var text = builder.textOrNew()
                    if (text.mentionedInfo?.type == MentionedInfo.TYPE_MENTION_MEMBER) {
                        text = builder.newText()
                    }

                    text.mentionedInfo {
                        type = MentionedInfo.TYPE_MENTION_ALL
                    }
                }

                is PlainText -> {
                    val text = builder.textOrNew()
                    text.content {
                        appendText(message.text)
                    }
                }

                is Image -> {
                    builder.buildAndSetNull()
                    builder.appendToResult(message.toImgMsgContentInfo(bot, villaId, autoQuote))
                }

                else -> {
                    // do nothing.
                }
            }

            // others.
            is VillaLink -> {
                with(builder.textOrNew()) {
                    content {
                        val currentText = text
                        val msgText = message.text
                        appendText(msgText)
                        entity {
                            length = msgText.length
                            offset = currentText.length
                            content = message.link
                        }
                    }
                }
            }

            is VillaSendMessage -> {
                builder.buildAndAppendToResult(message::createApi)
            }

            is VillaStyleText -> {
                // A 不支持在多个消息元素中使用。
                bot.logger.warn(
                    "`VillaStyleText` ({}) is not supported in multiple message elements. " +
                            "If you wish to send complex message elements, consider using `love.forte.simbot.component.miyoushe.message.VillaSendMessage`",
                    message
                )
            }

            is VillaVillaRoomLink -> {
                with(builder.textOrNew()) {
                    content {
                        val currentTextLen = textBuilder.length
                        val name = message.name
                        appendText(name)
                        entity {
                            length = name.length
                            offset = currentTextLen
                            content = message.link
                        }
                    }
                }
            }

            is VillaPanel -> {
                with(builder.currentOrNewText()) {
                    panel = message.source
                }
            }

            is VillaQuote -> {
                with(builder.currentOrNewText()) {
                    quoteInfo = message.source
                }
            }

            is VillaQuoteMessage -> {
                with(builder.currentOrNewText()) {
                    quoteInfo = message.source.toQuoteInfo()
                }
            }
        }
    }

    return builder.closure()
}
