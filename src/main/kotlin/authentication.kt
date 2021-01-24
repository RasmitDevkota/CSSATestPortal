import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class authentication(username: String, password: String) {
    init {
        val url = URL("https://cssa-backend.herokuapp.com/check")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.requestMethod = "POST";
        con.doOutput = true;

        val data = """
            {
                "Unknown": "$username",
                "Password": "$password"
            }
            """.trimIndent()
        val jsonData = Json.encodeToJsonElement(data)

        con.outputStream.use { os ->
            val input: ByteArray = data.toByteArray()

            os.write(input, 0, input.size)
        }

        BufferedReader(
            InputStreamReader(con.inputStream, "utf-8")
        ).use { br ->
            val response = StringBuilder()
            var responseLine: String?
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }
            println(response.toString())
        }
    }
}