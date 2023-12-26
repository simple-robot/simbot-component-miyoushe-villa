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

import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.uploadToOssData
import love.forte.simbot.literal
import love.forte.simbot.message.*
import love.forte.simbot.miyoushe.api.image.GetUploadImageParamsApi
import love.forte.simbot.miyoushe.api.msg.*
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

/**
 */
public suspend fun Message.toSendApi(bot: VillaBot, villaId: String, roomId: ULong): List<SendMessageApi> {
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
        if (size == 1) return first().toMsgContents0(bot, villaId).mapToApi()

        // messages
        return toMsgContents0(this, bot, villaId).mapToApi()
    }

    // is element
    return (this as Message.Element<*>).toMsgContents0(bot, villaId).mapToApi()
}

private suspend fun Message.Element<*>.toMsgContents0(bot: VillaBot, villaId: String): List<Any> {
    fun returnListOf(value: MsgContentInfo<*>): List<Any> = listOf(value)
    fun returnListOf(value: SendMessageApiCreator): List<Any> = listOf(value)

    when (this) {
        is StandardMessage<*> -> when (this) {
            is At ->
                return returnListOf(
                    MsgContentInfo(
                        content = TextMsgContent(text = ""),
                        mentionedInfo = MentionedInfo.mentionMembers(listOf(target.literal))
                    )
                )

            AtAll -> return returnListOf(
                MsgContentInfo(
                    TextMsgContent(text = ""),
                    mentionedInfo = MentionedInfo.mentionAll()
                )
            )

            is PlainText -> return returnListOf(MsgContentInfo(TextMsgContent(text = text)))
            is Emoji -> {
                // TODO emoji, nothing.
                return emptyList()
            }

            is Face -> {
                // TODO face?
                return emptyList()
            }

            is Image -> {
                when (this) {
                    is VillaImgMsgContent -> {
                        return returnListOf(MsgContentInfo(source))
                    }

                    // others, use resource.
                    else -> when (val resource = resource()) {
                        //region standards
                        is URLResource -> {
                            return returnListOf(MsgContentInfo(ImgMsgContent(url = resource.url.toString())))
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

                            return returnListOf(MsgContentInfo(ImgMsgContent(url = uploaded.url)))
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

                            return returnListOf(MsgContentInfo(ImgMsgContent(url = uploaded.url)))
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

                            return returnListOf(MsgContentInfo(ImgMsgContent(url = uploaded.url)))
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

                            return returnListOf(MsgContentInfo(ImgMsgContent(url = uploaded.url)))

                        }
                    }
                }
            }

            else -> {
                // RemoteResource, or others
                return emptyList()
            }
        }

        is VillaLink -> {
            return returnListOf(
                MsgContentInfo(
                    TextMsgContent(
                        text = this.text,
                        entities = listOf(TextMsgContent.Entity(0, text.length, link))
                    )
                )
            )
        }

        is VillaSendMessage -> {
            return returnListOf { roomId -> this.createApi(roomId) }
        }

        is VillaStyleText -> {
            return returnListOf(
                MsgContentInfo(
                    TextMsgContent(
                        text = this.text,
                        entities = listOf(TextMsgContent.Entity(0, text.length, style))
                    )
                )
            )
        }

        is VillaVillaRoomLink -> {
            return returnListOf(
                MsgContentInfo(
                    TextMsgContent(
                        text = name,
                        entities = listOf(TextMsgContent.Entity(0, name.length, link))
                    )
                )
            )
        }
    }

    return emptyList()
}

/**
 * [messages] 至少有2个元素
 *
 * 返回值：
 * - [SendMessageApiCreator]
 * - [MsgContentInfo]
 */
private suspend fun toMsgContents0(messages: Messages, bot: VillaBot, villaId: String): List<Any> {
    val contentBuilders = mutableListOf<MsgContentInfoBuilder<*, *>>()
    val result = mutableListOf<Any>()

    fun appendToResult(value: SendMessageApiCreator) = result.add(value)
    fun appendToResult(value: MsgContentInfo<*>) = result.add(value)


    TODO("Messages toMsgContents0")
}
