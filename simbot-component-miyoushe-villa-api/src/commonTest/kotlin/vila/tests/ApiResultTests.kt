package vila.tests

import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.api.ApiResult
import kotlin.test.*


/**
 *
 * @author ForteScarlet
 */
class ApiResultTests {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun emptyApiResultSerTest() {
        with(json.decodeFromString(ApiResult.emptySerializer(), """{"retcode":0}""")) {
            assertEquals(0, retcode)
            assertNull(message)
            assertTrue(isSuccess)
            assertNotNull(data)
            assertEquals(Unit, data)
        }

        with(json.decodeFromString(ApiResult.emptySerializer(), """{"retcode":0,"data":null}""")) {
            assertEquals(0, retcode)
            assertNull(message)
            assertTrue(isSuccess)
            assertNotNull(data)
            assertEquals(Unit, data)
        }

        with(json.decodeFromString(ApiResult.emptySerializer(), """{"retcode":0,"data":{}}""")) {
            assertEquals(0, retcode)
            assertNull(message)
            assertTrue(isSuccess)
            assertNotNull(data)
            assertEquals(Unit, data)
        }

        with(json.decodeFromString(ApiResult.emptySerializer(), """{"retcode":0,"data":{"name":"forte"}}""")) {
            assertEquals(0, retcode)
            assertNull(message)
            assertTrue(isSuccess)
            assertNotNull(data)
            assertEquals(Unit, data)
        }

        with(json.decodeFromString(ApiResult.emptySerializer(), """{"retcode":1}""")) {
            assertEquals(1, retcode)
            assertNull(message)
            assertFalse(isSuccess)
            assertNull(data)
        }
    }

}
