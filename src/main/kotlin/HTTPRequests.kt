import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.OkHttpClient

class HTTPRequests {
    fun get(_url: String, token: String = ""): String {
        var response = ""

        val url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            doOutput = true

            if (token != "") {
                setRequestProperty("Authorization", "Bearer $token")
            }

            if (responseCode != 200) {
                println("Sent '$requestMethod' request to URL: $url; Response Code: $responseCode ($responseMessage)")

                response = "$responseCode|()|$responseMessage|()|"

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += line
                    }
                }
            } else {
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += "$line\n"
                    }
                }
            }

            disconnect()
        }

        return response
    }

    fun post(_url: String, data: String = "", token: String = "", length: Boolean = false): String {
        var response = ""

        val url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true

            setRequestProperty("Content-Type", "application/json")

            if (length) {
                setRequestProperty("Content-Length", "0")
            }

            if (token != "") {
                setRequestProperty("Authorization", "Bearer $token");
            }

            if (data != "") {
                val out = data.toByteArray()
                val stream = outputStream
                stream.write(out)
            }

            println("Sent '$requestMethod' request to URL: $url; Response Code: $responseCode ($responseMessage)")

            if (responseCode != 200) {
                response = "$responseCode|()|$responseMessage|()|"

                errorStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += line
                    }
                }
            } else {
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        response += "$line\n"
                    }
                }
            }

            disconnect()
        }

        return response
    }

    fun patch(_url: String, data: String = "", token: String = ""): String {
        var response = ""

        val url = _url.replace(" ", "%20")

        val client = OkHttpClient()

        val body = RequestBody.create(MediaType.parse("application/json;"), data)
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Content-Length", "${data.length}")
            .addHeader("Authorization", "Bearer $token")
            .addHeader("X-HTTP-Method-Override", "PATCH")
            .post(body)
            .build()

        client.newCall(request).execute().use {
            val message = it.message()
            val code = it.code()
            response = it.body()!!.string()
            println("$code (Message: $message)\nResponse: $response")
        }

        return response
    }

    fun delete(_url: String): String {
        var response = ""

        val url = _url.replace(" ", "%20")

        with(URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "DELETE"
            doOutput = true

            if (responseCode != 200) {
                println("Sent '$requestMethod' request to URL: $url; Response Code: $responseCode ($responseMessage)")
            }

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