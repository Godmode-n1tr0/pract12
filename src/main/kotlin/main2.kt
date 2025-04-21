import kotlinx.coroutines.*
import kotlin.system.exitProcess

fun main() = runBlocking {
    println("=== Анализатор участников ===")
    val memberService = MemberService()

    val inputJob = launch(Dispatchers.IO) {
        while (true) {
            printMenu()
            when (readLine()?.trim()?.lowercase()) {
                "1" -> addMemberAsync(memberService)
                "2" -> showMembers(memberService)
                "3" -> memberService.clearMembers().also { println("Список очищен") }
                "4" -> {
                    memberService.cancel()
                    exitProcess(0)
                }
                else -> println("Неверный ввод")
            }
        }
    }

    inputJob.join()
}

private suspend fun addMemberAsync(service: MemberService) = coroutineScope {
    launch {
        try {
            print("Введите имя участника: ")
            val name = withContext(Dispatchers.IO) { readLine()?.trim() ?: "" }
            require(name.isNotBlank()) { "Имя не может быть пустым" }

            print("Введите количество репозиториев: ")
            val repos = withContext(Dispatchers.IO) {
                readLine()?.trim()?.toIntOrNull()
            }
            require(repos != null && repos >= 0) { "Некорректное количество репозиториев" }

            service.addMember(name, repos)
            println("Участник $name добавлен")
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
        }
    }
}

private fun showMembers(service: MemberService) {
    val members = service.members.value
    if (members.isEmpty()) {
        println("Нет участников для отображения")
        return
    }

    println("\nСписок участников (сортировка по количеству репозиториев):")
    println("----------------------------------------")
    members.forEachIndexed { index, member ->
        println("${index + 1}. $member")
    }
}

private fun printMenu() {
    println("\nМеню:")
    println("1. Добавить участника")
    println("2. Показать всех участников")
    println("3. Очистить список")
    println("4. Выход")
    print("Выберите действие: ")
}