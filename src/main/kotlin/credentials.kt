import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Data(val a: Int, val b: String)

fun main() {
    val data = Data(1, "Hi Royce")
    println(Json.encodeToString(data))
}