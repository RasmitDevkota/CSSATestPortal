class Firebase {
    var apiKey = "AIzaSyBVT22t-x2H76119AHG8SgPU0_A0U-N1uA"
    var authDomain = "my-scrap-project.firebaseapp.com"
    var databaseURL = "https://my-scrap-project.firebaseio.com"
    var projectId = "my-scrap-project"
    var storageBucket = "my-scrap-project.appspot.com"
    var messagingSenderId = "334998588870"
    var appId = "1:334998588870:web:e331335d3dc0acb4c536c7"
    var measurementId = "G-FS3QR29WMD"

    var authUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=$apiKey"
    var profileUpdateUrl = "tps://identitytoolkit.googleapis.com/v1/accounts:update?key=$apiKey"

    var http = HTTPRequests()

    inner class Firestore {
        fun get(path: String): HashMap<String, Any> {
            var getUrl = "https://firestore.googleapis.com/v1beta1/projects/my-scrap-project/databases/(default)/documents/$path?key=$apiKey";
            return hashMapOf()
        }

        var document = Collection("tests").getDocument("mytest")

        inner class Collection(_path: String, _parent: Collection? = null) {
            var name = ""
            var path = ""

            var parent: Collection? = null

            var children = hashMapOf<String, Document>()

            init {
                name = _path
                parent = _parent
                path = (parent?.path ?: "") + "/" + _path
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
                var listUrl = "https://firestore.googleapis.com/v1beta1/projects/my-scrap-project/databases/(default)/documents/$_path?key=$apiKey";
            }
        }

        inner class Document(_path: String, _parent: Collection) {
            var name = ""
            var path = ""
            lateinit var parent: Collection

            init {
                if (get(_path).size == 0) {
                    parent = _parent
                    name = _path
                    path = parent.path + "/" + _path
                    var createUrl = "https://firestore.googleapis.com/v1beta1/projects/my-scrap-project/databases/(default)/documents/$path/placeholder/..?documentId=$name&key=$apiKey";
                }
            }

            fun update(_data: String) {
                var updateUrl = "https://firestore.googleapis.com/v1beta1/projects/my-scrap-project/databases/(default)/documents/$path?updateMask.fieldPaths=$_data&key=$apiKey"
            }

            fun delete() {
                var deleteUrl = "https://firestore.googleapis.com/v1beta1/projects/my-scrap-project/databases/(default)/documents/$path?key=$apiKey";
                parent.deleteDocument(path)
            }

            fun getCollection(_path: String): Collection {
                return Collection(_path)
            }
        }
    }

    class HTTPRequests() {
        fun get(url: String) {

        }

        fun post(url: String) {

        }

        fun delete(url: String) {

        }
    }
}