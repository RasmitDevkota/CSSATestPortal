class Test {
    var questions = arrayListOf<Question>()

    interface Question {
        val type: String
            get() = "Default"
    }

    // Multiple Choice Question
    class MCQ : Question {

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
}