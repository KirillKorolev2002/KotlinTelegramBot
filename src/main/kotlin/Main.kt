import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0
)

data class Statistics(
    val totalWords: Int,
    val learnedWords: Int
) {
    val learningProgress: Int = if (totalWords > 0) {
        (learnedWords.toDouble() / totalWords * 100).toInt()
    } else {
        0
    }
}

interface DictionaryStorage {
    fun loadWords(): MutableList<Word>
    fun saveWords(words: List<Word>)
}

class FileDictionaryStorage(private val filePath: String) : DictionaryStorage {

    override fun loadWords(): MutableList<Word> {
        val wordsFile = File(filePath)
        val dictionary = mutableListOf<Word>()

        if (wordsFile.exists()) {
            wordsFile.readLines().forEach { line ->
                val parts = line.split("|")
                if (parts.size >= 2) {
                    val original = parts[0]
                    val translate = parts[1]
                    val correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
                    dictionary.add(Word(original, translate, correctAnswersCount))
                }
            }
        }
        return dictionary
    }

    override fun saveWords(words: List<Word>) {
        val wordsFile = File(filePath)
        val lines = words.map { "${it.original}|${it.translate}|${it.correctAnswersCount}" }
        wordsFile.writeText(lines.joinToString("\n"))
    }
}

class QuestionHandler {
    fun generateQuestion(words: List<Word>): Pair<Word, List<Word>> {
        val notLearnedList = words.filter { it.correctAnswersCount < 3 }

        if (notLearnedList.isEmpty()) {
            return Pair(Word("", "", 0), emptyList())
        }
        val questionWords = notLearnedList.shuffled().take(4)
        val correctAnswer = questionWords.random()
        return Pair(correctAnswer, questionWords.shuffled())
    }

    fun checkAnswer(correctWord: Word, userAnswer: Int, questionWords: List<Word>): Boolean {
        if (userAnswer in 1..questionWords.size) {
            return questionWords[userAnswer - 1] == correctWord
        }
        return false
    }
}

fun QuestionHandler.displayQuestion(correctAnswer: Word, questionWords: List<Word>) {
    println()
    println("${correctAnswer.original}:")
    questionWords.forEachIndexed { index, word ->
        println(" ${index + 1} - ${word.translate}")
    }
}

fun getAnswer(): Int? {
    print("Введите номер ответа: ")
    return readLine()?.toIntOrNull()
}

class LearningManager(
    private val dictionaryStorage: DictionaryStorage,
    private val questionHandler: QuestionHandler
) {

    private var dictionary: MutableList<Word> = dictionaryStorage.loadWords()

    fun startLearning() {
        while (true) {
            val (correctAnswer, questionWords) = questionHandler.generateQuestion(dictionary)

            if (questionWords.isEmpty()) {
                println("Все слова в словаре выучены!")
                break
            }

            questionHandler.displayQuestion(correctAnswer, questionWords)

            val answer = getAnswer()
            if (answer != null) {
                val isCorrect = questionHandler.checkAnswer(correctAnswer, answer, questionWords)
                if (isCorrect) {
                    println("Верно!")
                    correctAnswer.correctAnswersCount++
                    dictionaryStorage.saveWords(dictionary)
                } else {
                    println("Неверно.")
                }
            } else {
                println("Некорректный ввод. Попробуйте еще раз.")
            }
        }
    }

    fun getStatistics(): Statistics {
        val learnedWords = dictionary.filter { it.correctAnswersCount >= 3 }
        return Statistics(dictionary.size, learnedWords.size)
    }
}

fun main() {
    val dictionaryStorage = FileDictionaryStorage("words.txt")
    val questionHandler = QuestionHandler()
    val learningManager = LearningManager(dictionaryStorage, questionHandler)

    while (true) {
        println("Меню:")
        println("1. Учить слова")
        println("2. Статистика")
        println("0. Выход")
        val input = readLine()

        when (input) {
            "1" -> learningManager.startLearning()
            "2" -> {
                val statistics = learningManager.getStatistics()
                println("Выучено ${statistics.learnedWords} из ${statistics.totalWords} слов | ${statistics.learningProgress}%")
            }
            "0" -> {
                println("Выход из программы.")
                break
            }
            else -> println("Введите число 1, 2 или 0")
        }
    }
}