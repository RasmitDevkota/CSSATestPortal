class Firebase {
    var apiKey = "AIzaSyAPTvz8weUBIMyjl6ekC1uegX-j4u2Z1sc"
    var authDomain = "cssa-dev.firebaseapp.com"
    var databaseURL = "https://cssa-dev-default-rtdb.firebaseio.com"
    var projectId = "cssa-dev"
    var storageBucket = "cssa-dev.appspot.com"
    var messagingSenderId = "921024173703"
    var appId = "1:921024173703:web:46f4a35d815964ddf44a22"
    var measurementId = "G-WBN11JNGTN"

    var http = HTTPRequests()

    var uid = ""
    var userToken = ""

    inner class Authentication() {
        fun authenticate(authType: String, credentials: ArrayList<Any>) {
            when (authType) {
                "EmailPasswordSignIn" -> {
                    var signInResponse = http.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey", data = """
                        {
                            "email": "${credentials[0]}",
                            "password": "${credentials[1]}",
                            "returnSecureToken": true
                        }
                    """.trimIndent())

                    var localIdSubstring = signInResponse.substring(signInResponse.indexOf("\"localId\": \"") + 12)
                    uid = localIdSubstring.substring(0, localIdSubstring.indexOf("\""))

                    var idTokenSubstring = signInResponse.substring(signInResponse.indexOf("\"idToken\": \"") + 12)
                    userToken = idTokenSubstring.substring(0, idTokenSubstring.indexOf("\""))
                }

                "EmailPasswordSignUp" -> {
                    var signUpResponse = http.post("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$apiKey", data = """
                        {
                            "email": "${credentials[0]}",
                            "password": "${credentials[1]}",
                            "returnSecureToken": true
                        }
                    """.trimIndent())

                    var localIdSubstring = signUpResponse.substring(signUpResponse.indexOf("\"localId\": \"") + 12)
                    uid = localIdSubstring.substring(0, localIdSubstring.indexOf("\""))

                    var idTokenSubstring = signUpResponse.substring(signUpResponse.indexOf("\"idToken\": \"") + 12)
                    userToken = idTokenSubstring.substring(0, idTokenSubstring.indexOf("\""))
                }
            }
        }
    }

    inner class Firestore {
        fun get(_path: String): String {
            return http.get("https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$_path?key=$apiKey&access_token=$userToken", token = userToken)
        }

        inner class Collection(_path: String, _parent: Collection? = null) {
            var name = ""
            var path = ""
            var parent: Collection? = null

            var children = hashMapOf<String, Document>()

            init {
                name = _path
                parent = _parent
                path = "${if (parent?.path != null) (parent?.path + "/") else ""}$_path"
            }

            fun getDocument(_path: String): Document {
                return if (children.contains(_path)) {
                    children[_path]!!
                } else {
                    children[_path] = Document(_path, this)
                    Document(_path, this)
                }
            }

            fun deleteDocument(_path: String) {
                children.remove(_path)
            }

            fun list(_path: String) {
                var listUrl = "https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$_path?key=$apiKey"
            }
        }

        inner class Document() {
            var name = ""
            var path = ""
            lateinit var parent: Collection

            private var _data = ""
            val data: String
                get() {
                    if (_data == "") {
                        _data = firestore.get(path)
                    }

                    return _data
                }

            constructor(_path: String, _parent: Collection) : this() {
                parent = _parent
                name = _path
                path = "${parent.path}/$_path"
            }

            constructor(_path: String) : this() {
                parent = Collection(_path.substring(0, _path.lastIndexOf("/")))
                name = _path.substring(_path.lastIndexOf("/") + 1)
                path = _path
            }

            fun create(_data: String) {
                var createUrl = "https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path/placeholder/..?documentId=$name&key=$apiKey"
            }

            fun update(_data: String) {
                var updateUrl = "https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path?updateMask.fieldPaths=$_data&key=$apiKey"
            }

            fun delete() {
                var deleteUrl = "https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path?key=$apiKey"
                parent.deleteDocument(path)
            }

            fun getCollection(_path: String): Collection {
                return Collection("$path/$_path")
            }
        }

    }

//    @Serializable
//    data class DocumentModel(
//        val type: String = "Default",
//        val name: String = "",
//        val fields: FieldsModel = FieldsModel(),
//        val createTime: String = "",
//        val updateTime: String = ""
//    )
//
//    @Serializable
//    data class FieldsModel(
//        // User Fields
//        val event1: String = "",
//        val event2: String = "",
//        val event3: String = "",
//        val event4: String = "",
//        val role: String = "",
//
//        // Test Fields
//        val event: String = "",
//        val competition: String = "",
//    )
}