import net.jemzart.jsonkraken.JsonKraken
import net.jemzart.jsonkraken.JsonValue
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

class Authentication() {
    var email: String = ""
    var username: String = ""
    var fName: String = ""
    var lName: String = ""
    var password: String = ""

    var clientID = "834594227639-b7pj2rqb1eijd2pbfvice7bp0ndsdp7i.apps.googleusercontent.com";
    var clientSecret = "KZdxDA3r_gF18eCACoFUdapt";
    var redirectUri = "https://127.0.0.1:8310/"

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

        firebaseAuth.authenticate("EmailPasswordSignIn", arrayListOf(username, password))

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

            return if (response.toString() == "false") {
                false;
            } else {
                val json: JsonValue = JsonKraken.deserialize(response.toString())
                email = json["info"][0].cast<String>()
                username = json["info"][1].cast<String>()
                fName = json["info"][2].cast<String>()
                lName = json["info"][3].cast<String>()
                true;
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

        firebaseAuth.authenticate("EmailPasswordSignUp", arrayListOf(username, password))

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

    fun googleSignIn(code: String) {
        with(URL("https://oauth2.googleapis.com/token?code=$code&client_id=$clientID&client_secret=$clientSecret&redirect_uri=$redirectUri&grant_type=authorization_code").openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true

            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            setRequestProperty("Content-Length", "0")

            println("\nSent '$requestMethod' request to URL : $url; Response Code : $responseCode ($responseMessage)")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }

            disconnect()
        }
    }

    fun googleHttpServer() {
        val server: HttpServer = HttpServer.create(InetSocketAddress(8310), 0)
        server.createContext("/", GoogleHttpHandler())
        server.executor = null
        server.start()
    }

    class GoogleHttpHandler : HttpHandler {
        override fun handle(t: HttpExchange) {
            var clientID = "834594227639-b7pj2rqb1eijd2pbfvice7bp0ndsdp7i.apps.googleusercontent.com";
            var clientSecret = "KZdxDA3r_gF18eCACoFUdapt";
            var authorizationEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
            var redirectUri = "http://127.0.0.1:8310/"

            var authorizationString = "$authorizationEndpoint?response_type=code&scope=openid%20profile&redirect_uri=$redirectUri&client_id=$clientID"

            val url = URL(authorizationString)

            var response = """
                <!DOCTYPE html>
                <html>
                    Loading
                    
                    <script>
                        let url = new URL(window.location.href);
                        let params = new URLSearchParams(url.search);
                        window.location.href = "http://localhost:8081/get-code/" + params.get('code');
                    </script>
                </html>
            """.trimIndent()

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                inputStream.bufferedReader().use {
                    t.sendResponseHeaders(200, response.length.toLong())
                    val os = t.responseBody
                    os.write(response.toByteArray())
                    os.close()
                }
            }
        }
    }
}