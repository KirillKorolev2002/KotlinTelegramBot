import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0
)

fun loadDictionary(): MutableList<Word> {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    wordsFile.readLines().forEach { line ->
        val parts = line.split("|")
        val original = parts[0].trim()
        val translate = parts[1].trim()
        val correctAnswersCount = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: 0
        val word = Word(original, translate, correctAnswersCount)
        dictionary.add(word)
    }
    return dictionary
}

fun saveDictionary(dictionary: List<Word>) {
    val wordsFile = File("words.txt")
    wordsFile.writeText(dictionary.joinToString("\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
}

fun main() {
    val dictionary = loadDictionary()

    while (true) {
        println("Меню:")
        println("1. Учить слова")
        println("2. Статистика")
        println("0. Выход")

        val input = readLine()
        when (input) {
            "1" -> {
                while (true) {
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < LEARNING_THRESHOLD }
                    if (notLearnedList.isEmpty()) {
                        println("Все слова в словаре выучены")
                        break
                    }

                    val questionWords = notLearnedList.shuffled().take(NUMBER_OF_QUESTION_WORDS)
                    val correctAnswer = questionWords.random()
                    val correctAnswerId = questionWords.indexOf(correctAnswer) + 1

                    println()
                    println("${correctAnswer.original}:")

                    questionWords.shuffled().forEachIndexed { index, word ->
                        println(" ${index + 1} - ${word.translate}")
                    }

                    println("0 - Меню")

                    val answerInput = readLine()

                    try {
                        val answer = answerInput?.toIntOrNull()

                        if (answer == 0) break

                        if (answer != null && answer in 1..NUMBER_OF_QUESTION_WORDS) {
                            if (answer == correctAnswerId) {
                                correctAnswer.correctAnswersCount++
                                println("Правильно!")
                                saveDictionary(dictionary)
                            } else {
                                println("Неправильно! ${correctAnswer.original} - это ${correctAnswer.translate}")
                            }

                        } else {
                            println("Пожалуйста, введите число от 0 до $NUMBER_OF_QUESTION_WORDS")
                        }
                    } catch (e: NumberFormatException) {
                        println("Неверный формат ввода. Введите число.")
                    }
                }
            }

            "2" -> {
                val learnedWords = dictionary.filter { it.correctAnswersCount >= LEARNING_THRESHOLD }
                val totalCount = dictionary.size
                val learnedCount = learnedWords.size
                val percent = if (totalCount > 0) (learnedCount.toDouble() / totalCount * 100).toInt() else 0
                println("Выучено $learnedCount из $totalCount слов | $percent%")
            }

            "0" -> {
                println("Выход из программы")
                break
            }

            else -> println("Введите число 1, 2 или 0")
        }
    }
}

const val LEARNING_THRESHOLD = 3
const val NUMBER_OF_QUESTION_WORDS = 4