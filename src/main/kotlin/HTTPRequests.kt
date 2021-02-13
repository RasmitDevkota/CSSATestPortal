import java.net.HttpURLConnection
import java.net.URL

class HTTPRequests {
    fun get(_url: String, token: String = ""): String {
        var response = ""

        var url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            doOutput = true

            if (token != "") {
                setRequestProperty("Authorization", "Bearer $token")
            }

//            println("\nSent '$requestMethod' request to URL : $url; Response Code: $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }

    fun post(_url: String, data: String = "", token: String = ""): String {
        var response = ""

        var url = _url.replace(" ", "%20")

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

//            println("Sent '$requestMethod' request to URL : $url; Response Code: $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }

    fun patch(_url: String): String {
        var response = ""

        var url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "PATCH"
            doOutput = true

//            println("\nSent '$requestMethod' request to URL : $url; Response Code: $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    response += "$line\n"
                }
            }

            disconnect()
        }

        return response
    }

    fun delete(_url: String): String {
        var response = ""

        var url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "DELETE"
            doOutput = true

//            println("\nSent '$requestMethod' request to URL : $url; Response Code: $responseCode ($responseMessage)")

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