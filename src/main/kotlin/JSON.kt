import com.google.gson.annotations.SerializedName

data class SignIn (
    var email: String = "",
    var password: String = "",
    var returnSecureToken: Boolean = true
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
    var event1: String = "None",
    var event2: String = "None",
    var event3: String = "None",
    var event4: String = "None",
    var role: String = ""
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
