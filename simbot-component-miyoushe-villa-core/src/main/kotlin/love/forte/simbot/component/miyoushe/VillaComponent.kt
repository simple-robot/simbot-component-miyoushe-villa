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

package love.forte.simbot.component.miyoushe

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import love.forte.simbot.*
import love.forte.simbot.component.miyoushe.message.*
import love.forte.simbot.message.At
import love.forte.simbot.message.Message


/**
 *
 * 米游社大别野组件。
 *
 * 米游社大别野组件内的大部分相关类型都会以 `Villa` 开头，例如 `VillaBot`。
 *
 * @author ForteScarlet
 */
public class VillaComponent : Component {

    override val componentSerializersModule: SerializersModule
        get() = serializersModule

    override val id: String
        get() = ID_VALUE

    public companion object Factory : ComponentFactory<VillaComponent, MiyousheVillaComponentConfiguration> {
        /**
         * 组件的ID标识常量。
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public const val ID_VALUE: String = "simbot.villa"

        /**
         * [ID_VALUE] 的 [ID] 类型。
         */
        @Deprecated("Unused")
        public val componentID: CharSequenceID = ID_VALUE.ID

        /**
         * 当一个 at 的消息类型为提及 bot 时，使用在 [At.type] 中的类型
         */
        public const val MENTION_BOT_TYPE: String = "bot"

        /**
         * 大别野组件的序列化模块信息。
         */
        @JvmStatic
        public val serializersModule: SerializersModule = SerializersModule {
            polymorphic(Message.Element::class) {
                subclass(VillaImgMsgContent.serializer())
                subclass(VillaSendMessage.serializer())
                subclass(VillaLink.serializer())
                subclass(VillaPanel.serializer())
                subclass(VillaQuote.serializer())
                subclass(VillaQuoteMessage.serializer())
                subclass(VillaStyleText.serializer())
                subclass(VillaVillaRoomLink.serializer())
            }
        }

        override val key: Attribute<VillaComponent> = attribute(ID_VALUE)

        override suspend fun create(configurator: MiyousheVillaComponentConfiguration.() -> Unit): VillaComponent {
            MiyousheVillaComponentConfiguration().configurator()
            return VillaComponent()
        }
    }
}


public class MiyousheVillaComponentConfiguration
// nothing now.
