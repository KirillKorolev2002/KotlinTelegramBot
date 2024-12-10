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

data class Word(
    val original: String,
    val translation: String,
    var correctAnswersCount:Int = 0
)