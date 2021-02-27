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

    lateinit var user: User

    inner class Authentication {
        fun authenticate(email: String, password: String): Int {
            val signInData = Gson().toJson(SignIn(email, password))

            val signInResponse = http.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey", data = signInData)

            println(signInResponse)

            return if (signInResponse.contains("|()|")) {
                val ErrorJson = Gson().fromJson(signInResponse.split("|()|")[2], Error().javaClass)

                when (ErrorJson.error.message) {
                    "MISSING_EMAIL" -> {
                        4
                    }

                    "MISSING_PASSWORD" -> {
                        5
                    }

                    "EMAIL_NOT_FOUND" -> {
                        6
                    }

                    "INVALID_EMAIL" -> {
                        7
                    }

                    "INVALID_PASSWORD" -> {
                        println(password)

                        8
                    }

                    "USER_DISABLED" -> {
                        9
                    }

                    else -> {
                        1
                    }
                }
            } else {
                val AuthJson = Gson().fromJson(signInResponse, Auth().javaClass)

                uid = AuthJson.localId
                userToken = AuthJson.idToken

                user = User(firestore.get("users/${uid}"))

                0
            }
        }

        fun create(email: String, password: String): Int {
            val signUpData = Gson().toJson(SignIn(email, password))

            val signUpResponse = http.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey", data = signUpData)

            return if (signUpResponse.contains("|()|")) {
                val ErrorJson = Gson().fromJson(signUpResponse.split("|()|")[2], Error().javaClass)

                when (ErrorJson.error.message) {
                    "MISSING_EMAIL" -> {
                        0
                    }

                    "MISSING_PASSWORD" -> {
                        0
                    }

                    "EMAIL_EXISTS" -> {
                        2
                    }

                    "INVALID_EMAIL" -> {
                        0
                    }

                    "INVALID_PASSWORD" -> {
                        0
                    }

                    "TOO_MANY_ATTEMPTS_TRY_LATER" -> {
                        0
                    }

                    else -> {
                        0
                    }
                }
            } else {
                val AuthJson = Gson().fromJson(signUpResponse, Auth().javaClass)

                uid = AuthJson.localId
                userToken = AuthJson.idToken

                user = User(firestore.get("users/${uid}"))

                0
            }
        }
    }

    inner class Firestore {
        fun get(_path: String): String {
            val getResponse = http.get("https://firestore.googleapis.com/v1beta1/projects/$projectId/databases/(default)/documents/$_path?key=$apiKey&access_token=$userToken", token = userToken)

            return if (getResponse.contains("|()|")) {
                val ErrorJson = Gson().fromJson(getResponse.split("|()|")[2], Error().javaClass)

                when (ErrorJson.error.message) {
                    "ABORTED" -> {
                        "~Error|Looks like you're doing too many actions, please slow down!"
                    }

                    "INTERNAL", "RESOURCE_EXHAUSTED", "UNAVAILABLE" -> {
                        "~Error|Looks like there's an error with our database, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                    }

                    "INVALID_ARGUMENT" -> {
                        "~Error|Looks like there's a problem accessing our database, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                    }

                    "NOT_FOUND" -> {
                        "~Error|Looks like something is missing, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                    }

                    "PERMISSION_DENIED" -> {
                        "~Error|Looks like you don't have permission to do this, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                    }

                    "UNAUTHENTICATED" -> {
                        "~Error|Looks like there's a problem with your account, please trying signing out and in again or restart the testing portal!"
                    }

                    else -> {
                        "~Error|Unknown error occurred, please contact crewcssa@gmail.com or join our Discord server at bit.ly/cssa-discord for assistance!"
                    }
                }
            } else {
                getResponse
            }
        }

        inner class Collection(_path: String, _parent: Document? = null) {
            var name = ""
            var path = ""
            var parent: Document? = null

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
                    _data = firestore.get(path)

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