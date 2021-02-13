import androidx.compose.foundation.*
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.CoreTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test(_path: String) {
    var path = ""

    var Questions = arrayListOf<Question>()

    var http = HTTPRequests()

    var test: Firebase.Firestore.Document
    var questions: Firebase.Firestore.Collection
    var answerSheet: Firebase.Firestore.Document

    var data = ""
    var questionsData = ""

    var active = true

    init {
        path = _path
        test = firestore.Document("tests/$_path")
        questions = firestore.Collection("tests/$_path/questions")
        answerSheet = firestore.Document("users/${firebase.uid}")
        data = test.data

        questionsData = questions.list()

        val questionGson: HashMap<String, Any> = Gson().fromJson(questionsData, object : TypeToken<HashMap<String, Any>>(){}.type)

        questionGson.forEach {
            val splitQuestions = it.value.toString().removeSurrounding("[{", "}]").replace("}, {name", "|=|name").split("|=|")

            splitQuestions.forEach { questionData ->
                val dataName = Regex("""(?<=name=)projects/cssa-dev/databases/\(default\)/documents/tests/.*/questions/question.""").find(questionData)!!.value
                val dataType = Regex("""(?<=type=\{stringValue=)(mcq|msq|fitb|srq|lrq|mq)""").find(questionData)!!.value

                println("\n$questionData\n")

                println("$dataName, $dataType")

                when (dataType) {
                    "mcq" -> {
                        val dataOptions = arrayListOf<String>()
                        Regex("""(?<=options=\{arrayValue=\{values=\[).*?(?=])""").findAll(questionData).forEach { options ->
                            options.value.removeSurrounding("{", "}").replace("}, {", ",").replace("stringValue=", "").split(",").forEach { option ->
                                dataOptions.add(option)
                            }
                        }

                        val newQuestion = MCQ(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean(),
                            options = dataOptions
                        )
                        Questions.add(newQuestion)
                    }

                    "msq" -> {
                        val dataOptions = arrayListOf<String>()
                        Regex("""(?<=options=\{arrayValue=\{values=\[).*?(?=])""").findAll(questionData).forEach { options ->
                            options.value.removeSurrounding("{", "}").replace("}, {", ",").replace("stringValue=", "").split(",").forEach { option ->
                                dataOptions.add(option)
                            }
                        }

                        val newQuestion = MSQ(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean(),
                            options = dataOptions
                        )
                        Questions.add(newQuestion)
                    }

                    "fitb" -> {
                        val newQuestion = FITB(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean()
                        )
                        Questions.add(newQuestion)
                    }

                    "srq" -> {
                        val newQuestion = SRQ(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean()
                        )
                        Questions.add(newQuestion)
                    }

                    "lrq" -> {
                        val newQuestion = LRQ(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean()
                        )
                        Questions.add(newQuestion)
                    }

                    "mq" -> {
                        val dataOptionsA = arrayListOf<String>()
                        Regex("""(?<=optionsA=\{arrayValue=\{values=\[).*?(?=])""").findAll(questionData).forEach { options ->
                            options.value.removeSurrounding("{", "}").replace("}, {", ",").replace("stringValue=", "").split(",").forEach { option ->
                                dataOptionsA.add(option)
                            }
                        }

                        val dataOptionsB = arrayListOf<String>()
                        Regex("""(?<=optionsB=\{arrayValue=\{values=\[).*?(?=])""").findAll(questionData).forEach { options ->
                            options.value.removeSurrounding("{", "}").replace("}, {", ",").replace("stringValue=", "").split(",").forEach { option ->
                                dataOptionsB.add(option)
                            }
                        }

                        val newQuestion = MQ(
                            number = dataName.takeLast(1).toInt(),
                            type = dataType,
                            text = Regex("""(?<=text=\{stringValue=).*?(?=}+,)""").find(questionData)!!.value,
                            image = Regex("""(?<=image=\{)stringValue=.*?(?=}+,)""").find(questionData)!!.value.split("=")[1],
                            points = Regex("""(?<=value=\{integerValue=).*?(?=}+,)""").find(questionData)!!.value.toInt(),
                            tiebreaker = Regex("""(?<=tiebreaker=\{booleanValue=)(true|false)""").find(questionData)!!.value.toBoolean(),
                            optionsA = dataOptionsA,
                            optionsB = dataOptionsB
                        )
                        Questions.add(newQuestion)
                    }
                }
            }
        }
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

        var answer: String

        @Composable
        fun UI() {

        }
    }

    // Multiple Choice Question
    inner class MCQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val options: ArrayList<String>,
        override var answer: String = ""
    ) : Question {
        @Composable
        override fun UI() {
            super.UI()

            Row(Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)) {
                var selected by remember {
                    mutableStateOf("")
                }

                Column {
                    Row {
                        Text("$number. $text ($points point${if (points > 1) "s" else ""})")
                    }

                    Column {
                        options.forEach {
                            Row(Modifier
                                .fillMaxWidth(0.831f)
                                .selectable((it == selected), onClick = { selected = it; answer = it })
                                .padding(horizontal = 16.dp)
                            ) {
                                RadioButton((it == selected), onClick = { selected = it; answer = it })

                                Text(it, style = MaterialTheme.typography.body1.merge(), modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Multiple Select Question
    inner class MSQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val options: ArrayList<String>,
        override var answer: String = ""
    ) : Question {
        @Composable
        override fun UI() {
            super.UI()

            Row(Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)) {
                Column {
                    Row {
                        androidx.compose.material.Text("$number. $text ($points point${if (points > 1) "s" else ""})")
                    }

                    Column {
                        val answers = arrayListOf<String>()

                        options.forEach { option ->
                            Row(Modifier.padding(16.dp)) {
                                var checked by remember {
                                    mutableStateOf(false)
                                }

                                Checkbox(checked,
                                    onCheckedChange = {
                                        checked = !checked

                                        if (checked) {
                                            answers.remove(option)
                                        } else {
                                            answers.add(option)
                                        }

                                        answer = answers.toString()
                                    }
                                )

                                Text(option, Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Fill-In-The-Blank
    inner class FITB (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        override var answer: String = ""
    ) : Question {
        @InternalTextApi
        @Composable
        override fun UI() {
            super.UI()

            Row {
                var blankAnswer by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                val blankIndex = text.indexOf("|~~~~|")
                val half1 = text.substring(0, blankIndex)
                val half2 = text.substring(blankIndex + 6)

                Column {
                    Text("$number. Fill in the blank below. ($points point${if (points > 1) "s" else ""})")

                    Row {
                        Text(half1)
                        CoreTextField(
                            modifier = Modifier
                                .width(200.dp)
                                .height(25.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(2.dp)),
                            value = blankAnswer,
                            onValueChange = {
                                if (it.text.length < 25) {
                                    blankAnswer = it

                                    answer = blankAnswer.text
                                }
                            },
                        )
                        Text(half2)
                    }
                }
            }
        }
    }

    // Short Response Question
    inner class SRQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        override var answer: String = ""
    ) : Question {

    }

    // Long Response Question
    inner class LRQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        override var answer: String = ""
    ) : Question {
        @InternalTextApi
        @Composable
        override fun UI() {
            super.UI()

            Row {
                Column {
                    Row {
                        Text("$number. $text ($points point${if (points > 1) "s" else ""})")
                    }

                    Column {
                        var response by remember {
                            mutableStateOf(TextFieldValue(""))
                        }

                        CoreTextField(
                            modifier = Modifier.width(519.dp).heightIn(100.dp).border(1.dp, Color.Black, RoundedCornerShape(2.dp)),
                            value = response,
                            onValueChange = {
                                response = it

                                if (it.toString().length > 300) {
                                    response = TextFieldValue(response.toString().substring(0, 300))
                                }

                                answer = it.toString()
                            }
                        )
                    }
                }
            }
        }
    }

    // Matching Question
    inner class MQ (
        override val number: Int,
        override val type: String,
        override val text: String,
        override val image: String,
        override val points: Int,
        override val tiebreaker: Boolean,
        val optionsA: ArrayList<String>,
        val optionsB: ArrayList<String>,
        override var answer: String = ""
    ) : Question {
        @InternalTextApi
        @Composable
        override fun UI() {
            super.UI()

            Row(Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)) {
                Column {
                    Row {
                        Text("$number. $text ($points point${if (points > 1) "s" else ""})")
                    }

                    Row {
                        val answers by remember {
                            mutableStateOf(hashMapOf<Int, String>())
                        }

                        Column {
                            var count = 1

                            optionsA.forEach {
                                val index = count - 1
                                val counter = count

                                var optionAnswer by remember {
                                    mutableStateOf(TextFieldValue(""))
                                }

                                answers[index] = ""

                                Row(Modifier.padding(vertical = 10.dp)) {
                                    Row(Modifier
                                        .width(48.dp)
                                        .height(48.dp)
                                        .padding(end = 5.dp)
                                        .border(1.dp, Color.Black, RoundedCornerShape(2.dp)), Arrangement.Center) {
                                        CoreTextField(
                                            modifier = Modifier
                                                .width(50.dp)
                                                .height(50.dp)
                                                .padding(7.dp, 8.dp, 9.dp, 8.dp),
                                            textStyle = TextStyle(
                                                fontSize = 22.sp,
                                                textAlign = TextAlign.Center
                                            ),
                                            value = optionAnswer,
                                            onValueChange = {
                                                if (it.text.length < 2) {
                                                    optionAnswer = it

                                                    answers[index] = it.text
                                                    answer = answers.toString()
                                                }
                                            },
                                        )
                                    }

                                    Text(text = it,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        style = TextStyle(fontSize = 22.sp))
                                }

                                count++
                            }
                        }

                        Column(Modifier.padding(start = 50.dp)) {
                            var count = 1

                            optionsB.forEach {
                                val index = count - 1
                                val counter = count

                                Row(Modifier.padding(vertical = 10.dp).fillMaxWidth().height(50.dp)) {
                                    Text(text = "$count. $it",
                                        color = (if (answers.toString().contains(count.toString())) Color.Black else Color.DarkGray),
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        style = TextStyle(fontSize = 22.sp))
                                }

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
        ScrollableColumn(scrollState = rememberScrollState(), modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Questions.forEach {
                it.UI()
                println("Added UI for Question #${it.number}")
            }
        }

        GlobalScope.launch {
            while (active) {
                delay(90000)
                saveAnswer()
            }
        }
    }

    fun saveAnswer() {
        var documentData = """
            {
                "fields": {
        """

        Questions.forEach {
            documentData += """
                    "question${it.number}": {
                        "stringValue": "${it.answer}"
                    },
            """
        }

        documentData += """
                }
            }
        """

        println(documentData)
    }

    fun end() {
        active = false
        return GlobalScope.cancel()
    }

}