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
        val original = parts[0]
        val translate = parts[1]
        val correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
        val word = Word(original, translate, correctAnswersCount)
        dictionary.add(word)
    }
    return dictionary
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
            "1" -> println("Выбран пункт 'Учить слова'")
            "2" -> {
                val learnedWords = dictionary.filter { it.correctAnswersCount >= 3 }
                val totalCount = dictionary.size
                val learnedCount = learnedWords.size
                val percent = if (totalCount > 0) (learnedCount.toDouble() / totalCount * 100).toInt() else 0
                println("Выучено $learnedCount из $totalCount слов | $percent%")
            }
            "0" -> {
                println("Выход из программы.")
                break
            }
            else -> println("Введите число 1, 2 или 0")
        }
        println()
    }
}