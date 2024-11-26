import java.io.File
fun main () {
    val wordsFile = File("words.txt")
    wordsFile.forEachLine {
        println(it)
    }
}