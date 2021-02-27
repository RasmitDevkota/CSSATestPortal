data class SignIn (
    var email: String = "",
    var password: String = "",
    var returnSecureToken: Boolean = true
)

data class BackendCredentials (
    var Unknown: String = "",
    var Password: String = ""
)

data class BackendUser (
    var code: Int = 1,
    var info: ArrayList<String> = ArrayList()
)

data class Auth (
    var localId: String = "",
    var email: String =  "",
    var displayName: String =  "",
    var idToken: String =  "",
    var registered: Boolean =  true,
    var refreshToken: String =  "",
    var expiresIn: String =  ""
)

data class User (
    var event1: String? = null,
    var event2: String? = null,
    var event3: String? = null,
    var event4: String? = null,
    var role: String? = null
)

data class QuestionList (
    var documents: ArrayList<QuestionDocument> = ArrayList()
)

data class QuestionDocument (
    var name: String = "",
    var fields: HashMap<String, Any> = HashMap(),
    var createTime: String = "",
    var updateTime: String = "",
)

data class AnswerDocument (
    var fields: HashMap<String, HashMap<String, String>> = HashMap()
)

data class Error (
    val error: ErrorBody = ErrorBody()
)

data class ErrorBody (
    val code: Int = 200,
    val errors: ArrayList<Errors> = ArrayList(),
    val message: String = "",
    val status: String = ""
)

data class Errors (
    val domain: String = "",
    val message: String = "",
    val reason: String = ""
)

fun stringValue(json: String?): String {
    return json!!.removeSurrounding("{stringValue=", "}")
}

fun integerValue(json: String?): Int {
    return json!!.removeSurrounding("{integerValue=", "}").toInt()
}

fun booleanValue(json: String?): Boolean {
    return json!!.removeSurrounding("{booleanValue=", "}").toBoolean()
}

fun arrayValue(json: String?): ArrayList<String> {
    val array = ArrayList<String>()
    json!!.removeSurrounding("{arrayValue={values=[", "]}}").split(", ").forEach {
        when (it.removeSurrounding("{", "}").split("=")[0]) {
            "stringValue" -> array.add(stringValue(it))
        }
    }
    return array
}
