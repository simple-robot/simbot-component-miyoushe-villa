import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.serialization.protobuf.ProtoBuf
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.MiyousheVillaApiToken
import love.forte.simbot.miyoushe.api.image.GetUploadImageParamsApi
import love.forte.simbot.miyoushe.api.image.upload
import love.forte.simbot.miyoushe.api.requestData
import love.forte.simbot.utils.md5
import love.forte.simbot.utils.toHex
import java.io.File
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
@Suppress("OPT_IN_USAGE")
class UploadTest {
    //region p
    private val botId = "bot_uyoiEQHXtqFgBLHdoBJg"
    private val botSecret = "ieObyL0j3EY85WfT38TOYRLukOyM0X6lU29x6Me2QDYLv"
    private val json = MiyousheVilla.DefaultJson
    private val protoBuf = ProtoBuf {
        encodeDefaults = true
    }
    private val villaId = "6272"
    private val token = MiyousheVillaApiToken(botId, botSecret, villaId)
    //endregion

    @Test
    fun uploadTest() = runTest {
        val client = HttpClient()
        val file = File("C:\\Users\\Administrator\\Desktop\\图\\JetBrains.png")

        val md5 = md5 { update(file.readBytes()) }.toHex()

        println(md5)

        val api = GetUploadImageParamsApi.create(md5, "png")
        val data = api.requestData(client, token)
        println(data)

    }

    @Test
    fun doUploadTest() = runTest {
        val server = embeddedServer(CIO, port = 8080) {
            routing {
                post("/test") {
                    println("On Post: $this")
                    try {
                        println("Headers: ")
                        context.request.headers.forEach { s, strings ->
                            println("\t$s: $strings")
                        }
                        val text = context.receiveStream().bufferedReader().use { it.readText() }
                        println("Received Text: $text")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw e
                    }
                }
            }
        }.apply { start(wait = false) }

        println("Server: $server")

        val client = HttpClient()
        val file = File("G:\\code\\javaProjects\\simbot-component-miyoushe\\test.png")
        val md5 = md5 { update(file.readBytes()) }.toHex()

        withContext(Dispatchers.Default) {
            val api = GetUploadImageParamsApi.create(md5, "png")
            val data = api.requestData(client, token)

            val newData = data.copy(params = data.params.copy(host = "http://localhost:8080/test"))

            val resp = newData.params.upload(client, file)
            println("Resp: $resp")
            println("Resp.Body: ${resp.bodyAsText()}")
        }

        delay(5000)

        server.stop()
    }

}
