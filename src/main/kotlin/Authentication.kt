import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import net.jemzart.jsonkraken.JsonKraken
import net.jemzart.jsonkraken.JsonValue
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStream

import java.io.IOException

import com.sun.net.httpserver.HttpExchange

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.lang.Exception

import java.net.InetSocketAddress
import java.util.Collections

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets

import com.google.api.client.auth.oauth2.Credential







class Authentication() {
    var email: String = ""
    var username: String = ""
    var fName: String = ""
    var lName: String = ""
    var password: String = ""

    fun manualSignIn(_username: String, _password: String): Boolean {
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
            if(response.toString() == "false") {
                return false;
            } else {
                val json: JsonValue = JsonKraken.deserialize(response.toString())
                email = json["info"][0].cast<String>()
                username = json["info"][1].cast<String>()
                fName = json["info"][2].cast<String>()
                lName = json["info"][3].cast<String>()
                return true;
            }
        }
    }

    fun checkUsername(_username: String): Boolean {
        username = _username

        val url = URL("https://cssa-backend.herokuapp.com/checkUsername")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.requestMethod = "POST";
        con.doOutput = true;

        val data = username


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
            return response.toString() != "1"
        }
    }

    fun checkEmail(_email: String): Boolean {
        email = _email

        val url = URL("https://cssa-backend.herokuapp.com/checkEmail")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.requestMethod = "POST";
        con.doOutput = true;

        val data = email


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
            return response.toString() != "1"
        }
    }

    fun createAccount(_fName: String, _lName: String, _username: String, _email: String, _password: String): Boolean {
        username = _username
        email = _email
        fName = _fName
        lName = _lName
        password = _password

        val url = URL("https://cssa-backend.herokuapp.com/registration")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.requestMethod = "POST";
        con.doOutput = true;

        val data = """
            {
                "Email": "$username",
                "Username": "$username",
                "First": "$fName",
                "Last": "$lName",
                "Password": "$password"
            }
            """.trimIndent()


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
            return response.toString() == "1"
        }
    }

    fun googleSignIn() {
        val server: HttpServer = HttpServer.create(InetSocketAddress(8310), 0)
        server.createContext("/", GoogleHttpHandler())
        server.executor = null
        server.start()
    }

    internal class GoogleHttpHandler : HttpHandler {
        override fun handle(t: HttpExchange) {
            var clientID = "834594227639-b7pj2rqb1eijd2pbfvice7bp0ndsdp7i.apps.googleusercontent.com";
            var clientSecret = "KZdxDA3r_gF18eCACoFUdapt";
            var authorizationEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
            var tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
            var userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
            var redirectUri = "http://127.0.0.1:8310/"

            var authorizationString = "$authorizationEndpoint?response_type=code&scope=openid%20profile&redirect_uri=$redirectUri&client_id=$clientID"

            val url = URL(authorizationString)

            var response = "<!DOCTYPE html><html>Loading<script>let url = new URL(window.location.href); let params = new URLSearchParams(url.search); window.location.href = \"http://localhost:8081/get-code/\" + params.get('code');</script></html>"

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->

                    }

                    t.sendResponseHeaders(200, response.length.toLong())
                    val os = t.responseBody
                    os.write(response.toByteArray())
                    os.close()
                    print(response)
                }
            }
        }
    }
}