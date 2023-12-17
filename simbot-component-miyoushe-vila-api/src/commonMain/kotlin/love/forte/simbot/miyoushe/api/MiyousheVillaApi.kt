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

package love.forte.simbot.miyoushe.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.modules.SerializersModule
import love.forte.simbot.miyoushe.MiyousheVilla
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmSynthetic


/**
 * 一个米游社大别野API。
 *
 * 更多参考: [大别野](https://webstatic.mihoyo.com/vila/bot/doc/)
 *
 * @author ForteScarlet
 */
public abstract class MiyousheVillaApi<out R : Any> {

    /**
     * API的请求 `method`。例如 [HttpMethod.Get]
     */
    public abstract val method: HttpMethod

    /**
     * api的请求路径。例如 `/vila/api/bot/platform/checkMemberBotAccessToken`
     */
    public abstract val path: String

    /**
     * API请求时的请求体。通常在 [method] 为 [HttpMethod.Post] 的时候才会存在。
     */
    public abstract val body: Any?

    /**
     * 基于 Kotlin serialization 的反序列化器，可用于将API的请求结果反序列化为预期的结果 [R]。
     */
    public abstract val resultSerializer: DeserializationStrategy<R>

    /**
     * 基于 Kotlin serialization 的反序列化器，可用于将API的请求结果反序列化为预期的结果 [ApiResult]<[R]>。
     */
    public abstract val apiResultSerializer: DeserializationStrategy<ApiResult<R>>

    // TODO @JST?
    /**
     * 使用当前API发起一个请求，并得到一个[HTTP响应][HttpResponse].
     */
    @JvmSynthetic
    public abstract suspend fun request(client: HttpClient, token: MiyousheVillaApiToken): HttpResponse

}

public abstract class StandardMiyousheVillaApi<out R : Any> : MiyousheVillaApi<R>() {

    abstract override fun toString(): String

    protected open fun postRequestBuilder(builder: HttpRequestBuilder) {}

    @Volatile
    private lateinit var _url: Url

    internal open val url: Url
        get() = if (::_url.isInitialized) _url else createUrl().also { _url = it }

    protected open fun URLBuilder.prepareUrl(): URLBuilder = this

    private fun createUrl(): Url {
        val missPrefix = !this.path.startsWith('/')
        val urlStr = buildString(MiyousheVilla.API_PATH.length + this.path.length + if (missPrefix) 1 else 0) {
            append(MiyousheVilla.API_PATH)
            if (missPrefix) {
                append('/')
            }
            append(this@StandardMiyousheVillaApi.path)
        }

        return URLBuilder(urlStr).prepareUrl().build()
    }

    override suspend fun request(client: HttpClient, token: MiyousheVillaApiToken): HttpResponse {
        val method = this@StandardMiyousheVillaApi.method
        val body = this@StandardMiyousheVillaApi.body
        val url = this@StandardMiyousheVillaApi.url

        return client.request {
            this.method = method
            this.url(url)
            // set Body
            when (body) {
                null -> {
                    // ignore.
                }

                is OutgoingContent -> setBody(body)
                else -> {
                    if (client.pluginOrNull(ContentNegotiation) != null) {
                        setBody(body)
                    } else {
                        try {
                            val ser = guessSerializer(body, MiyousheVilla.DefaultJson.serializersModule)
                            val bodyJson = MiyousheVilla.DefaultJson.encodeToString(ser, body)
                            setBody(bodyJson)
                        } catch (e: Throwable) {
                            try {
                                setBody(body)
                            } catch (e0: Throwable) {
                                e0.addSuppressed(e)
                                throw e0
                            }
                        }
                    }
                }
            }

            headers[MiyousheVilla.Headers.BOT_ID_KEY] = token.botId
            headers[MiyousheVilla.Headers.BOT_SECRET_KEY] = token.botSecret
            token.botVillaId?.also { headers[MiyousheVilla.Headers.BOT_VILLA_ID_KEY] = it }

            postRequestBuilder(this)
        }
    }
}

/**
 * 使用 Get 请求的API。
 *
 * @see MiyousheVillaApi
 *
 */
public abstract class MiyousheVillaGetApi<out R : Any> : StandardMiyousheVillaApi<R>() {
    override val method: HttpMethod
        get() = HttpMethod.Get

    override val body: Any?
        get() = null
}

/**
 * 使用 Post 请求的API。
 *
 * @see MiyousheVillaApi
 *
 */
public abstract class MiyousheVillaPostApi<out R : Any> : StandardMiyousheVillaApi<R>() {
    override val method: HttpMethod
        get() = HttpMethod.Post
}

/**
 * 使用 Post 请求的API, 且结果返回值为 [Unit]
 *
 * @see MiyousheVillaApi
 *
 */
public abstract class MiyousheVillaPostEmptyResultApi : MiyousheVillaPostApi<Unit>() {
    override val apiResultSerializer: DeserializationStrategy<ApiResult<Unit>>
        get() = ApiResult.emptySerializer()

    override val resultSerializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()
}

/**
 * 请求 [MiyousheVillaApi] 时所需的机器人凭证请求头信息。
 *
 * @see MiyousheVilla.Headers
 *
 */
public data class MiyousheVillaApiToken(
    /**
     * @see MiyousheVilla.Headers.BOT_ID_KEY
     */
    val botId: String,
    /**
     * @see MiyousheVilla.Headers.BOT_SECRET_KEY
     */
    val botSecret: String,
    /**
     * @see MiyousheVilla.Headers.BOT_VILLA_ID_KEY
     */
    val botVillaId: String?,
)

//region Ktor ContentNegotiation guessSerializer
// see KotlinxSerializationJsonExtensions.kt
// see SerializerLookup.kt

@Suppress("UNCHECKED_CAST")
private fun guessSerializer(value: Any?, module: SerializersModule): KSerializer<Any> = when (value) {
    null -> String.serializer().nullable
    is List<*> -> ListSerializer(value.elementSerializer(module))
    is Array<*> -> value.firstOrNull()?.let { guessSerializer(it, module) } ?: ListSerializer(String.serializer())
    is Set<*> -> SetSerializer(value.elementSerializer(module))
    is Map<*, *> -> {
        val keySerializer = value.keys.elementSerializer(module)
        val valueSerializer = value.values.elementSerializer(module)
        MapSerializer(keySerializer, valueSerializer)
    }

    else -> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        module.getContextual(value::class) ?: value::class.serializer()
    }
} as KSerializer<Any>


@OptIn(ExperimentalSerializationApi::class)
private fun Collection<*>.elementSerializer(module: SerializersModule): KSerializer<*> {
    val serializers: List<KSerializer<*>> =
        filterNotNull().map { guessSerializer(it, module) }.distinctBy { it.descriptor.serialName }

    if (serializers.size > 1) {
        error(
            "Serializing collections of different element types is not yet supported. " +
                    "Selected serializers: ${serializers.map { it.descriptor.serialName }}",
        )
    }

    val selected = serializers.singleOrNull() ?: String.serializer()

    if (selected.descriptor.isNullable) {
        return selected
    }

    @Suppress("UNCHECKED_CAST")
    selected as KSerializer<Any>

    if (any { it == null }) {
        return selected.nullable
    }

    return selected
}
//endregion
