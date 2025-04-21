import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun getData(id: Int): String {
    val n = Random.nextLong(500, 2000)
    delay(n)
    return "Результат $id, (Задержа ${n} ms)"
}

suspend fun main() = coroutineScope {
    print("Сколько функций запустить? ")
    val n = readLine()!!.toInt()
    if (n <= 0){
        println("Значение n не может быть отрицательным или равняться 0")
        return@coroutineScope
    }
    println("\nВсе результаты:")
    for (i in 1..n) {
        println(async { getData(i) }.await())
    }
}