import love.forte.simbot.component.miyoushe.internal.message.toMessages
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.event.SendMessage
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class MessageContentTests {

    @Test
    fun messageContentTest() {
        val jsonStr = """
            {
                "content": "{\"content\":{\"text\":\"加粗斜体删除线分割线\",\"entities\":[{\"offset\":0,\"length\":2,\"entity\":{\"type\":\"style\",\"font_style\":\"bold\"}},{\"offset\":0,\"length\":6,\"entity\":{\"type\":\"style\",\"font_style\":\"strikethrough\"}},{\"offset\":2,\"length\":5,\"entity\":{\"type\":\"style\",\"font_style\":\"italic\"}}]},\"mentionedInfo\":{\"type\":1,\"userIdList\":[\"bot_c85497cf9a086084d4b1\",\"339501\"]},\"quote\":{\"original_message_id\":\"C81T-F0M5-KSL8-MS0I\",\"original_message_send_time\":1683885001494,\"quoted_message_id\":\"C81T-F0M5-KSL8-MS0I\",\"quoted_message_send_time\":1683885001494},\"panel\":{\"template_id\":1}}",      
                "from_user_id": 0,  
                "send_at": 0,       
                "room_id": 0,       
                "object_name": 1,   
                "nickname": "",     
                "msg_uid": "",      
                "bot_msg_id": "",   
                "villa_id": 0,      
                "quote_msg": {      
                    "content": "",            
                    "msg_uid": "",            
                    "bot_msg_id": "",         
                    "send_at": 0,              
                    "msg_type": "",           
                    "from_user_id": 0,        
                    "from_user_nickname": "", 
                    "from_user_id_str": "",   
                    "images": [""]            
                }
            }
        """.trimIndent()

        val sendMessage = MiyousheVilla.DefaultJson.decodeFromString(SendMessage.serializer(), jsonStr)

        println(sendMessage)

        val messages = sendMessage.toMessages(MiyousheVilla.DefaultJson)

        messages.forEach { println(it) }
    }

}
