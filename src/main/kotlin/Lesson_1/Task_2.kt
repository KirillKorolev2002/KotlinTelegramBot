package Lesson_1

import java.io.File

fun main () {
    val wordsFile = File("theTask_1")
    wordsFile.createNewFile()
    val lines = wordsFile.readLines()
    for (line in lines) {
        println(line)
    }
}