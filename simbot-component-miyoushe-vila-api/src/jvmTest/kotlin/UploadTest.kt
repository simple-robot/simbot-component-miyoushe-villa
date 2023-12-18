import io.ktor.client.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.protobuf.ProtoBuf
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.MiyousheVillaApiToken
import love.forte.simbot.miyoushe.api.image.GetUploadImageParamsApi
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

}
