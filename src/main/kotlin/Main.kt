import java.io.File

fun main() {
    val wordsFile: File = File("words.txt")
    val lines = wordsFile.readLines()
    lines.forEach { line ->
        println(line)
    }
}