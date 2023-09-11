import love.forte.simbot.utils.toHex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


fun hmacSha256(pubKey: String, botSecret: String): String {
    val signingKey = SecretKeySpec(pubKey.toByteArray(Charsets.UTF_8), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(signingKey)
    val rawHmac = mac.doFinal(botSecret.toByteArray(Charsets.UTF_8))
    return rawHmac.toHex()
}
