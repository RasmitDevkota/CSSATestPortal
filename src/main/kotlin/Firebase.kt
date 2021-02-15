import com.google.gson.Gson

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

    inner class Authentication {
        fun authenticate(email: String, password: String) {
            val signInResponse = http.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey", data = """
                {
                    "email": "$email",
                    "password": "$password",
                    "returnSecureToken": true
                }
            """.trimIndent())

            val AuthJson = Gson().fromJson(signInResponse, Auth().javaClass)
            println(AuthJson)

            uid = AuthJson.localId
            userToken = AuthJson.idToken

            println("\n$uid\n$userToken\n")
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

            fun Document(_path: String): Document {
                return if (children.contains(_path)) {
                    children[_path]!!
                } else {
                    children[_path] = Document(_path, this)
                    this@Firestore.Document(_path, this)
                }
            }

            fun deleteDocument(_path: String) {
                children.remove(_path)
            }

            fun list(): String {
                return http.get("https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path?key=$apiKey&access_token=$userToken", token = userToken)
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

            fun update(_data: String): String {
                return http.patch("https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path?key=$apiKey&access_token=$userToken", data = _data, token = userToken)
            }

            fun delete() {
                var deleteUrl = "https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$path?key=$apiKey"
                parent.deleteDocument(path)
            }

            fun Collection(_path: String): Collection {
                return this@Firestore.Collection("$path/$_path")
            }
        }
    }
}