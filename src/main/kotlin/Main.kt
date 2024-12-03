import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    wordsFile.forEachLine { it ->
        val parts = it.split("|")
        val original = parts[0]
        val translation = parts[1]
        val correctAnswersCount = parts.toIntOrNull() ?: 0
        dictionary.add(Word(original, translation, correctAnswersCount))
    }
    dictionary.forEach { println("${it.original} - ${it.translation} - ${it.correctAnswersCount}") }
}
data class Word(
    val original: String,
    val translation: String,
    var correctAnswersCount:Int = 0
)
