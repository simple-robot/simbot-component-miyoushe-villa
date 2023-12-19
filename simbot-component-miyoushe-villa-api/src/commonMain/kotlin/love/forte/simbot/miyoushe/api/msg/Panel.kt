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

package love.forte.simbot.miyoushe.api.msg

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import love.forte.simbot.miyoushe.api.msg.ButtonComponent.Companion.C_TYPE_CALLBACK
import kotlin.jvm.JvmName

/**
 * [消息组件](https://webstatic.mihoyo.com/vila/bot/doc/message_api/component.html)
 *
 *
 *
 */
@Serializable
public data class Panel(
    /**
     * 模板id，通过创建消息组件模板接口，可以提前将组件面板保存，使用 template_id 来快捷发送消息
     */
    @SerialName("template_id")
    @get:JvmName("getTemplateIdUnsignedSource")
    val templateId: ULong? = null,

    /**
     * 由component_group组成的数组	定义小型组件，即一行摆置3个组件，每个组件最多展示2个中文字符或4个英文字符
     */
    @SerialName("small_component_group_list")
    val smallComponentGroupList: List<Component>? = null,

    /**
     * 由component_group组成的数组	定义中型组件，即一行摆置2个组件，每个组件最多展示4个中文字符或8个英文字符
     */
    @SerialName("mid_component_group_list")
    val midComponentGroupList: List<Component>? = null,

    /**
     * 由component_group组成的数组	定义大型组件，即一行摆置1个组件，每个组件最多展示10个中文字符或20个英文字符
     */
    @SerialName("big_component_group_list")
    val bigComponentGroupList: List<Component>? = null,
) {

    /**
     * Friendly property for Java.
     */
    public val templateIdAsLong: Long?
        @JvmName("templateId")
        get() = templateId?.toLong()

}

/**
 * 最基本的组件单位，每个 component 都在客户端表现为一个可交互的组件。
 * 根据在客户端消息中占据的区域大小，可以被分为 小、中、大三种形制的组件。
 *
 * ### 序列化
 *
 * [Component] 的 `type` 无法直接被 Kotlinx Serialization 自动处理，
 * 因此 [Component] 自定义了它的序列化器。进行序列化时，会根据具体类型序列化为具体的结果，
 * 而反序列化则 **只支持JSON** 环境。如果尝试使用 [Json] 以外的序列化器进行反序列化，
 * 则会得到 [IllegalStateException] 异常。
 *
 */
@Serializable(ComponentSerializer::class)
public sealed class Component {
    /**
     * 组件id，由机器人自定义，不能为空字符串。面板内的id需要唯一
     */
    public abstract val id: String

    /**
     * 组件展示文本, 不能为空
     */
    public abstract val text: String

    /**
     * 组件类型，目前支持 type=1 按钮组件，未来会扩展更多组件类型
     */
    public abstract val type: Int

    /**
     * 是否订阅该组件的回调事件
     */
    @SerialName("need_callback")
    public abstract val needCallback: Boolean

    /**
     * 组件回调透传信息，由机器人自定义
     */
    public abstract val extra: String?
}


@OptIn(ExperimentalSerializationApi::class)
@Serializer(Component::class)
internal object ComponentDescriptorType

internal object ComponentSerializer : KSerializer<Component> {
    override val descriptor: SerialDescriptor = ComponentDescriptorType.descriptor

    override fun deserialize(decoder: Decoder): Component {
        if (decoder !is JsonDecoder) {
            throw IllegalStateException("暂不支持 Component 在非 JSON 环境下多态反序列化")
        }

        val jsonObject = decoder.decodeJsonElement().jsonObject

        return when (jsonObject["type"]?.jsonPrimitive?.intOrNull) {
            // Button
            ButtonComponent.TYPE -> decoder.json.decodeFromJsonElement(ButtonComponent.serializer(), jsonObject)
            else -> decoder.json.decodeFromJsonElement(SimpleComponent.serializer(), jsonObject)
        }
    }

    override fun serialize(encoder: Encoder, value: Component) {
        when (value) {
            is SimpleComponent -> SimpleComponent.serializer().serialize(encoder, value)
            is ButtonComponent -> ButtonComponent.serializer().serialize(encoder, value)
        }
    }
}

/**
 * 当 [Component] 无法被解析为具体的类型时（例如 [ButtonComponent]） 则会默认解析为 [SimpleComponent]。
 */
@Serializable
public data class SimpleComponent(
    override val id: String,
    override val text: String,
    override val type: Int,
    @SerialName("need_callback")
    override val needCallback: Boolean = false,
    override val extra: String? = null,
) : Component()

/**
 * 按钮组件
 *
 * 除去所有组件类型共有的字段外，根据按钮组件的交互场景，按钮组件增加了几个新的字段
 *
 * `type=1`
 *
 * @property cType 组件交互类型，包括：1回传型，2输入型，3跳转型。可参考 [ButtonComponent] 中以 `C_TYPE_` 开头的常量值，例如 [C_TYPE_CALLBACK]。
 * @property input 如果交互类型为输入型，则需要在该字段填充输入内容，不能为空
 * @property link 如果交互类型为跳转型，需要在该字段填充跳转链接，不能为空
 * @property needToken 对于跳转链接来说，如果希望携带用户信息token，则need_token设置为true
 */
@Serializable
public data class ButtonComponent(
    override val id: String,
    override val text: String,
    @SerialName("need_callback")
    override val needCallback: Boolean = false,
    override val extra: String? = null,
    public val cType: Int,
    public val input: String? = null,
    public val link: String? = null,
    @SerialName("need_token")
    public val needToken: Boolean = false,
) : Component() {
    override val type: Int
        get() = TYPE

    public companion object {
        public const val TYPE: Int = 1

        /**
         * 回传型按钮
         * 用户点击后，开放平台会将点击事件回调给机器人，客户端给用户弹出 toast「操作成功」。
         * 对于回传型按钮，无论 need_callback是否为true， 机器人都会收到点击后的回调事件 `ClickMsgComponent`
         */
        public const val C_TYPE_CALLBACK: Int = 1


        /**
         * 用户点击后，客户端将组件定义的 input_content 内容填充到用户文本框内，如果 need_callback = true，机器人还会收到点击后的回调事件
         *
         * 输入格式： 如果 `input = "/打卡"`，那么填充内容如：`@打卡机器人 /打卡`
         */
        public const val C_TYPE_INPUT: Int = 2

        /**
         * 用户点击后，客户端跳转 link 字段定义的链接。如果 need_token = true，则跳转链接时会携带上用户的 acces_token；
         * 如果 need_callback = true，机器人还会收到点击后的回调事件
         */
        public const val C_TYPE_LINK: Int = 3
    }
}


