import java.io.File

fun main() {
    val dictionary = loadDictionary() 
    while (true) {
        println("Меню:")
        println("1 – Учить слова")
        println("2 – Статистика")
        println("0 – Выход")

        val input = readln()

        when (input) {
            "1" -> println("Выбран пункт 'Учить слова'")
            "2" -> println("Выбран пункт 'Статистика'")
            "0" -> break
            else -> println("Введите число 1, 2 или 0")
        }
    }
}
fun loadDictionary() {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    wordsFile.forEachLine { it ->
        val parts = it.split("|")
        val original = parts[0]
        val translation = parts[1]
        val correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
        dictionary.add(Word(original, translation, correctAnswersCount))
    }
    dictionary.forEach { println("${it.original} - ${it.translation} - ${it.correctAnswersCount}") }
}
data class Word(
    val original: String,
    val translation: String,
    var correctAnswersCount:Int = 0
)