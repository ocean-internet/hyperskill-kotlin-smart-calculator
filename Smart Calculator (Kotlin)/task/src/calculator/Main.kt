package calculator

import java.util.Scanner

const val HELP_TEXT = "The program calculates the sum of numbers"
val validVariableName = "[a-zA-Z]+".toRegex()

fun main() {

    val calculator = Calculator()

    val input = Scanner(System.`in`)

    while (!Thread.currentThread().isInterrupted) {
        val line = input.nextLine()
        when {
            line.trim().isEmpty() -> continue
            line.startsWith('/') -> doCommand(line)
            line.contains('=') -> line.split("=").map { it.trim() }.apply { calculator.doAssignment(this[0], this[1]) }
            line.matches(validVariableName) -> println(calculator.getValue(line) ?: "Unknown variable")
            else -> try {
                println(calculator.infixToAnswer(line))
            } catch (e: Error) {
                println(e.message)
            } catch (e: Throwable) {
                println("Invalid expression")
            }
        }
    }
    println("Bye!")
}

fun doCommand(command: String) = when {
    command == "/help" -> println(HELP_TEXT)
    command == "/exit" -> Thread.currentThread().interrupt()
    else -> println("Unknown command")
}

