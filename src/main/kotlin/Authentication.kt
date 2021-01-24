import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Authentication() {
    var email: String = ""
    var username: String = ""
    var password: String = ""
    var profilePicUrl: String = ""
    var accountAge: Int = 0

    fun usernameSignIn(_username: String, _password: String): String {
        username = _username
        password = _password

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
            return response.toString()
        }
    }

    fun usernameSignUp(_email: String, _username: String, _password: String): String {
        email = _email
        username = _username
        password = _password

        return ""
    }

    fun googleSignIn(params: Any): String {
        return ""
    }
}