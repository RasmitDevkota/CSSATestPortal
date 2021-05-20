import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class Authentication() {
    var email: String = ""
    var username: String = ""
    var fName: String = ""
    var lName: String = ""
    var password: String = ""
    var hashedPassword: String = ""

    var clientID = "834594227639-b7pj2rqb1eijd2pbfvice7bp0ndsdp7i.apps.googleusercontent.com"
    var clientSecret = "KZdxDA3r_gF18eCACoFUdapt"
    var redirectUri = "https://127.0.0.1:8310/"
    val authUrl = "https://backend.cssa.dev"

    fun manualSignIn(_username: String, _password: String): Int {
        username = _username
        password = _password

        val url = URL("$authUrl/check")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")

        con.requestMethod = "POST"
        con.doOutput = true

        val data = Gson().toJson(BackendCredentials(username, password))

        con.outputStream.use { os ->
            val input: ByteArray = data.toByteArray()

            os.write(input, 0, input.size)
        }

        con.inputStream.bufferedReader().use { br ->
            val response = StringBuilder()
            var responseLine: String?

            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }

            val backendUser = Gson().fromJson(response.toString(), BackendUser().javaClass)

            return when (val code = backendUser.code) {
                0 -> {
                    val backendData = backendUser.info
                    email = backendData[0]
                    username = backendData[1]
                    fName = backendData[2]
                    lName = backendData[3]
                    hashedPassword = backendData[4]

                    val tryFirebaseAuthenticate = firebaseAuth.authenticate(email, hashedPassword)

                    tryFirebaseAuthenticate
                }

                else -> {
                    code
                }
            }
        }
    }

    fun createAccount(_fName: String, _lName: String, _username: String, _email: String, _password: String): ArrayList<Any> {
        username = _username
        email = _email
        fName = _fName
        lName = _lName
        password = _password

        val url = URL("$authUrl/registration")
        val con = url.openConnection() as HttpURLConnection

        con.setRequestProperty("Content-Type", "application/json; utf-8")
        con.setRequestProperty("Accept", "application/json")

        con.requestMethod = "POST"
        con.doOutput = true

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

        con.inputStream.bufferedReader().use { br ->
            val response = StringBuilder()
            var responseLine: String?

            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim { it <= ' ' })
            }

            val tryFirebaseCreate = firebaseAuth.create(email, password)
            if (tryFirebaseCreate != 0) {
                return arrayListOf(false, tryFirebaseCreate)
            }

            return arrayListOf(response.toString() == "1")
        }
    }

    fun badInput(params: ArrayList<String>): Boolean {
        val badRegex = Regex("""
            ( )
        """.trimIndent())

        params.forEach {
            if (badRegex.matches(it)) {
                println("Parameter $it contains bad input!")
                return true
            }
        }

        return false
    }
}