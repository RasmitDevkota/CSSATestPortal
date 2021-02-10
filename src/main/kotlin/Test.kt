import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test {
    var path = ""

    var questions = arrayListOf<Question>()

    var http = HTTPRequests()

    lateinit var test: Firebase.Firestore.Document
    lateinit var answerSheet: Firebase.Firestore.Document

    var active by remember {
        mutableStateOf(true)
    }

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

        fun saveAnswer(_answer: String) {

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

            Row {
                var selected by remember {
                    mutableStateOf("")
                }

                GlobalScope.launch {
                    var lastAnswer = ""

                    while (Test().active) {
                        delay(90000)

                        if (selected != lastAnswer) {
                            saveAnswer(selected)
                            lastAnswer = selected
                        }
                    }
                }

                Column {
                    Row {
                        Text("$number. $text ($points)")
                    }

                    Column {
                        options.forEach {
                            Row(Modifier
                                .fillMaxWidth(0.831f)
                                .selectable((it == selected), onClick = { selected = it })
                                .padding(horizontal = 16.dp)
                            ) {
                                RadioButton((it == selected), onClick = { selected = it })

                                Text(it, style = MaterialTheme.typography.body1.merge(), modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                    }
                }
            }
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
        override fun UI() {
            super.UI()

            Row {
                Column {
                    Row {
                        androidx.compose.material.Text("$number. $text ($points)")
                    }

                    Column {
                        options.forEach {
                            Row(Modifier.padding(16.dp)) {
                                var checked by remember {
                                    mutableStateOf(false)
                                }

                                Checkbox(checked,
                                    onCheckedChange = {
                                        checked = !checked
                                    }
                                )

                                Text(it, Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
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
        override fun UI() {
            super.UI()

            Row {
                Column {
                    Row {
                        Text("$number. $text ($points)")
                    }

                    Row {
                        val answers by remember {
                            mutableStateOf(hashMapOf<Int, String>())
                        }

                        Column {
                            var count = 1

                            optionsA.forEach {
                                answers[count] = ""

                                Row {
                                    TextField(
                                        modifier = Modifier.fillMaxWidth(0.15f),
                                        value = answers[count]!!,
                                        onValueChange = {
                                            answers[count] = it
                                        },
                                    )
                                    Text("$count. $it")
                                }

                                count++
                            }
                        }

                        Column {
                            var count = 1

                            optionsB.forEach {
                                Text("$count. $it", color = (if (answers[count]!!.trim() == "") Color.Black else Color.LightGray))

                                count++
                            }
                        }
                    }
                }
            }
        }
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
        test = firestore.Document("tests/$_path")
        answerSheet = firestore.Document("users/${firebase.uid}")
        return test.data
    }

    fun end() {
        active = false
        return GlobalScope.cancel()
    }

}