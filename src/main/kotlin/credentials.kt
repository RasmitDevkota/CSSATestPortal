import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlin.*

@Serializable
data class Data(val a: Int, val b: String)

fun main() {
    val data = Data(1, "Hi Royce")
    println(Json.encodeToString(data))
}