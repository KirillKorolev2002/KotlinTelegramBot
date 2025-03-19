import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0
)

const val LEARNING_THRESHOLD = 3
const val NUMBER_OF_QUESTION_WORDS = 4

fun loadDictionary(): MutableList<Word> {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    if (wordsFile.exists()) {
        wordsFile.readLines().forEach { line ->
            val parts = line.split("|")
            if (parts.size >= 2) {
                val original = parts[0].trim()
                val translate = parts[1].trim()

                if (original.isNotEmpty() && translate.isNotEmpty()) {
                    val correctAnswersCount = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: 0
                    dictionary.add(Word(original, translate, correctAnswersCount))
                } else {
                    println("Предупреждение: Пропущена некорректная строка в words.txt: $line")
                }
            } else {
                println("Предупреждение: Пропущена некорректная строка в words.txt: $line")
            }
        }
    }
    return dictionary
}

fun saveDictionary(dictionary: List<Word>) {
    val wordsFile = File("words.txt")
    try {
        wordsFile.writeText(dictionary.joinToString("\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    } catch (e: Exception) {
        println("Ошибка при сохранении словаря: ${e.message}")
    }
}

fun main() {
    val dictionary = loadDictionary()

    if (dictionary.isEmpty()) {
        println("Словарь пуст. Добавьте слова в файл words.txt в формате 'оригинал|перевод'.")
    }

    while (true) {
        println("Меню:")
        println("1. Учить слова")
        println("2. Статистика")
        println("3. Добавить слово")
        println("0. Выход")

        val input = readLine()

        when (input) {
            null -> {
                println("Ошибка ввода. Выход из программы.")
                return
            }

            else -> when (input) {
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

                        val shuffledQuestionWords = questionWords.shuffled()
                        shuffledQuestionWords.forEachIndexed { index, word ->
                            println(" ${index + 1} - ${word.translate}")
                        }

                        println("0 - Меню")

                        when (val answerInput = readlnOrNull()) {
                            null -> {
                                println("Ошибка ввода. Возврат в меню.")
                                break // Возврат в главное меню
                            }

                            else -> {
                                when (val answer = answerInput.toIntOrNull()) {
                                    0 -> break  // Выход из режима обучения
                                    in 1..NUMBER_OF_QUESTION_WORDS -> {
                                        if (answer == correctAnswerId) {
                                            correctAnswer.correctAnswersCount++
                                            println("Правильно!")
                                            saveDictionary(dictionary)
                                        } else {
                                            println("Неправильно! ${correctAnswer.original} - это ${correctAnswer.translate}")
                                        }
                                    }

                                    else -> println("Пожалуйста, введите число от 0 до $NUMBER_OF_QUESTION_WORDS")
                                }
                            }
                        }
                    }
                }

                "2" -> {
                    val learnedWords = dictionary.filter { it.correctAnswersCount >= LEARNING_THRESHOLD }
                    val totalCount = dictionary.size
                    val learnedCount = learnedWords.size
                    val remainingCount = totalCount - learnedCount
                    val percent = if (totalCount > 0) (learnedCount.toDouble() / totalCount * 100).toInt() else 0
                    println("Выучено $learnedCount из $totalCount слов ($percent%). Осталось выучить: $remainingCount")
                }

                "3" -> {
                    println("Введите слово на оригинальном языке:")
                    val original = readlnOrNull()?.trim()
                    println("Введите перевод:")
                    val translate = readlnOrNull()?.trim()

                    if (original != null && translate != null && original.isNotEmpty() && translate.isNotEmpty()) {
                        dictionary.add(Word(original, translate))
                        saveDictionary(dictionary)
                        println("Слово '$original' добавлено в словарь.")
                    } else {
                        println("Ошибка: Некорректный ввод. Слово не добавлено.")
                    }
                }

                "0" -> {
                    println("Выход из программы.")
                    return
                }

                else -> println("Введите число 1, 2, 3 или 0")
            }
        }
    }
}