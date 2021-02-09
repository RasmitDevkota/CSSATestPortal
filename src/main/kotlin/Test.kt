import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

class Test {
    var path = ""

    var questions = arrayListOf<Question>()

    var http = HTTPRequests()

    interface Question {
        val number: Int
            get() = 0

        val type: String
            get() = "Default"

        val text: String
            get() = ""

        val image: String
            get() = ""

        val points: Int
            get() = 5

        val tiebreaker: Boolean
            get() = false

        fun answer(_answer: String) {

        }

        @Composable
        fun UI() {

        }
    }

    // Multiple Choice Question
    class MCQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val options: ArrayList<String>
    ) : Question {
        @Composable
        override fun UI() {
            super.UI()
        }
    }

    // Multiple Select Question
    class MSQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val options: ArrayList<String>
    ) : Question {

    }

    // Fill-In-The-Blank
    class FITB (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean
    ) : Question {

    }

    // Short Response Question
    class SRQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean
    ) : Question {

    }

    // Long Response Question
    class LRQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean
    ) : Question {

    }

    // Matching Question
    class MQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val optionsA: ArrayList<String>,
        val optionsB: ArrayList<String>
    ) : Question {

    }

    @Composable
    fun UI() {
        Column {
            questions.forEach {
                it.UI()
            }
        }
    }

    fun loadTestFromFirebase(_path: String): String {
        return firestore.get("tests/$_path")
    }
}