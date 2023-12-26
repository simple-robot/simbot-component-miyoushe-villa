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

package love.forte.simbot.component.miyoushe.bot

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job
import love.forte.simbot.ID
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.bot.Bot
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.definition.Contact
import love.forte.simbot.definition.Group
import love.forte.simbot.definition.GuildBot
import love.forte.simbot.definition.SocialRelationsContainer
import love.forte.simbot.literal
import love.forte.simbot.message.Image
import love.forte.simbot.message.Image.Key.toImage
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.Robot
import love.forte.simbot.resources.Resource.Companion.toResource
import love.forte.simbot.utils.item.Items
import java.net.MalformedURLException
import java.net.URL


/**
 * 米游社大别野 Bot。
 *
 * @author ForteScarlet
 */
public abstract class VillaBot : Bot, GuildBot {
    protected abstract val job: Job
    public abstract val source: love.forte.simbot.miyoushe.stdlib.bot.Bot

    override val bot: VillaBot
        get() = this

    override val id: ID
        get() = source.ticket.botId.ID

    /**
     * 大别野 bot 暂时没有获取 bot 自身信息的接口，
     * [username] 在收到第一个事件中的 [Event.robot] 之前将返回 [Ticket.botId][love.forte.simbot.miyoushe.stdlib.bot.Bot.Ticket.botId]
     */
    abstract override val username: String

    /**
     * 大别野 bot 暂时没有获取 bot 自身信息的接口，
     * [avatar] 在收到第一个事件中的 [Event.robot] 之前将返回空字符串 `""`
     */
    abstract override val avatar: String

    abstract override val component: VillaComponent

    /**
     * 大别野 bot 暂时没有获取 bot 自身信息的接口，
     * 在尚未启动的时候，[isMe] 只会使用 [id] 进行比较；
     * 启动后，在收到第一个消息后会尝试将 [Event.robot] 信息缓存下来，
     * 并在后续追加使用 [Robot.Template.id] 进行比较
     */
    abstract override fun isMe(id: ID): Boolean

    override val isActive: Boolean
        get() = job.isActive

    override val isCancelled: Boolean
        get() = job.isCancelled

    override val isStarted: Boolean
        get() = source.isStarted

    abstract override val manager: VillaBotManager

    @JvmSynthetic
    override suspend fun cancel(reason: Throwable?): Boolean {
        if (!job.isActive) return false

        job.cancel(reason?.let { CancellationException(it.localizedMessage, it) })
        job.join()
        return true
    }

    @JvmSynthetic
    override suspend fun join() {
        job.join()
    }

    override fun invokeOnCompletion(handler: CompletionHandler) {
        job.invokeOnCompletion(handler)
    }

    /**
     * [resolveImage] 会将 [id] 视为 `url`, 并使用 [toImage] 转化为 [Image].
     *
     * @throws IllegalArgumentException 如果 url 解析失败 (对 [MalformedURLException] 的包装, see [URL])
     */
    @JvmSynthetic
    override suspend fun resolveImage(id: ID): Image<*> {
        val url = try {
            URL(id.literal)
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException(e)
        }

        return url.toResource().toImage()
    }

    /**
     * 大别野机器人无法获取所有的别野列表，始终得到 [Items.emptyItems]
     */
    override val guilds: Items<VillaGuild>
        get() = Items.emptyItems()

    @JST
    abstract override suspend fun guild(id: ID): VillaGuild?

    override fun toString(): String {
        return "VillaBot(botId=${source.ticket.botId})"
    }

    // unsupported

    /**
     * 米游社大别野不支持 '群聊' 相关内容。
     */
    override val isGroupsSupported: Boolean
        get() = false

    @Deprecated(GROUP_UNSUPPORTED_MESSAGE, ReplaceWith("-1"))
    @JSTP
    override suspend fun groupCount(): Int = SocialRelationsContainer.COUNT_NOT_SUPPORTED

    @Deprecated(GROUP_UNSUPPORTED_MESSAGE, ReplaceWith("Items.emptyItems()", "love.forte.simbot.utils.item.Items"))
    override val groups: Items<Group> get() = Items.emptyItems()

    @Deprecated(GROUP_UNSUPPORTED_MESSAGE, ReplaceWith("null"))
    @JST
    override suspend fun group(id: ID): Group? = null

    /**
     * 米游社大别野不支持 '联系人'（私聊） 相关内容。
     */
    override val isContactsSupported: Boolean
        get() = false

    @Deprecated(CONTACT_UNSUPPORTED_MESSAGE, ReplaceWith("Items.emptyItems()", "love.forte.simbot.utils.item.Items"))
    override val contacts: Items<Contact> get() = Items.emptyItems()

    @Deprecated(CONTACT_UNSUPPORTED_MESSAGE, ReplaceWith("-1"))
    @JSTP
    override suspend fun contactCount(): Int = SocialRelationsContainer.COUNT_NOT_SUPPORTED

    @Deprecated(CONTACT_UNSUPPORTED_MESSAGE, ReplaceWith("null"))
    @JST
    override suspend fun contact(id: ID): Contact? = null


    /**
     * 大别野中 bot 无法作为成员存在。
     * @throws UnsupportedOperationException 不支持
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("'asMember' is not supported in villa: bot can't be a member")
    @JST(blockingBaseName = "toMember", blockingSuffix = "", asyncBaseName = "toMember")
    override suspend fun asMember(): VillaMember = throw UnsupportedOperationException("Villa bot can't be a member")
}


internal const val GROUP_UNSUPPORTED_MESSAGE = "'Group' is unsupported in miyoushe villa"
internal const val CONTACT_UNSUPPORTED_MESSAGE = "'Contact' is unsupported in miyoushe villa"


