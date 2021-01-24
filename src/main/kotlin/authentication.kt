import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class authentication {
    init {
        val url = URL("https://cssa-backend.herokuapp.com/check")
        val con = url.openConnection() as HttpURLConnection
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        val jsonInputString = Data("ok", "Hi Royce")
        con.outputStream.use { os ->
            val input: ByteArray = jsonInputString.getBytes("utf-8")
            os.write(input, 0, input.size)
        }
        BufferedReader(
            InputStreamReader(con.inputStream, "utf-8")
        ).use { br ->
            val response = StringBuilder()
            var responseLine: String? = null
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }
            println(response.toString())
        }
    }
}