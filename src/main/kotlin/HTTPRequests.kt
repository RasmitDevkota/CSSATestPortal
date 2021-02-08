import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class HTTPRequests {
    fun get(url: String, token: String = ""): String {
        var response = ""

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            doOutput = true

            if (token != "") {
                setRequestProperty("Authorization", "Bearer $token")
            }

            setRequestProperty("Content-Length", "0")

            println("\nSent '$requestMethod' request to URL : $url; Response Code : $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }

    fun post(url: String, data: String = "", token: String = ""): String {
        var response = ""

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true

            setRequestProperty("Content-Type", "application/json")

            if (token != "") {
                setRequestProperty("Authorization", "Bearer $token");
            }

            if (data != "") {
                val out = data.toByteArray()
                val stream = outputStream
                stream.write(out)
            }

            println("Sent '$requestMethod' request to URL : $url; Response Code : $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }

    fun delete(url: String): String {
        var response = ""

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "DELETE"
            doOutput = true

            println("\nSent '$requestMethod' request to URL : $url; Response Code : $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }
}