import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

class Test {
    var path = ""

    var questions = arrayListOf<Question>()

    var http = HTTPRequests()

    interface Question {
        val type: String
            get() = "Default"

        fun QuestionUI() {

        }
    }

    // Multiple Choice Question
    class MCQ : Question {
        override fun QuestionUI() {
            super.QuestionUI()
        }
    }

    // Multiple Select Question
    class MSQ : Question {

    }

    // Fill-In-The-Blank
    class FITB : Question {

    }

    // Short Response Question
    class SRQ : Question {

    }

    // Long Response Question
    class LRQ : Question {

    }

    // Matching Question
    class MQ : Question {

    }

    fun TestUI() {
        Column {
            questions.forEach {
                it.QuestionUI()
            }
        }
    }

    fun loadTestFromFirebase(_path: String) {
//        Firebase().Firestore().Document("tests/example")
        var getUrl = "https://firestore.googleapis.com/v1beta1/projects/cssa-dev/databases/(default)/documents/$_path?key=AIzaSyAPTvz8weUBIMyjl6ekC1uegX-j4u2Z1sc";
    }
}