package calculator

import java.math.BigInteger


class Calculator {

    private val _assignedValues = mutableMapOf<String, String>()

    val isOperator: Regex = "([\\^/*()]|\\++|-+)".toRegex()
    val isValue: Regex = "(\\d+|[a-zA-Z])".toRegex()

    fun getValue(variable: String) = _assignedValues[variable] ?: variable

    fun doAssignment(key: String, value: String) = when {
        !key.matches(validVariableName) -> throw Error("Invalid identifier")
        value.matches("\\d+".toRegex()) -> _assignedValues.put(key, value)
        _assignedValues.containsKey(value) -> _assignedValues.put(key, getValue(value))
        else -> throw Error("Invalid assignment")
    }

    fun postfixToAnswer(postfix: String): String {
        val itr = postfix.trim().split("\\s+".toRegex()).iterator()
        val stack = ArrayDeque<String>(listOf<String>("0"))

        while (itr.hasNext()) {
            val next = itr.next()
            when {
                next.matches(isOperator) -> stack.add(doOperation(stack.removeLast(), stack.removeLast(), next))
                else -> stack.add(next)
            }
        }
        return stack.last()
    }

    fun doOperation(a: String, b: String, o: String): String = when (o) {
        "^" -> a.toBigInteger().pow(b.toInt())
        "*" -> b.toBigInteger().times(a.toBigInteger())
        "/" -> b.toBigInteger().div(a.toBigInteger())
        "+" -> b.toBigInteger().add(a.toBigInteger())
        "-" -> b.toBigInteger().minus(a.toBigInteger())
        else -> throw Error("Invalid operator: $o")
    }.toString()

    fun infixToPostfix(infix: String): String {

        assertValidInfix(infix)

        val itr = "( $infix )".replace(isOperator, " $1 ").replace(")", " ) ").trim().split("\\s+".toRegex())
            .map { getValue(it) }.iterator()
        val result = mutableListOf<String>()
        val operators = ArrayDeque<String>()

        while (itr.hasNext()) {
            val next = itr.next().sanitiseOperator()

            when {
                next == "(" -> operators.add(next)
                next == ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        result.add(operators.removeLast())
                    }
                    if (operators.lastOrNull() == "(") operators.removeLast()
                }

                next.matches(isValue) -> result.add(next)
                !next.matches(isOperator) -> throw Error("Invalid operator: $next")

                else -> {
                    while (operators.isNotEmpty() && operators.last() != "(" && !next.hasHigherPriorityThan(operators.last())) {
                        result.add(operators.removeLast())
                    }
                    operators.add(next)
                }
            }
        }
        result.add(operators.reversed().joinToString(" "))
        return result.joinToString(" ").trim()
    }

    fun String.hasHigherPriorityThan(operator: String): Boolean {
        val operators = mapOf<String, Int>("^" to 1, "/" to 2, "*" to 2, "+" to 3, "-" to 3)

        return (operators[this] ?: 0) < (operators[operator] ?: 0)
    }

    fun infixToAnswer(infix: String) = postfixToAnswer(infixToPostfix(infix))
    fun assertValidInfix(infix: String) {
        if (infix.filter { it == '(' }.length != infix.filter { it == ')' }.length) throw Error("Invalid expression")
    }
}

fun String.sanitiseOperator() = when {
    this.matches(Regex("\\++")) -> "+"
    this.matches(Regex("-{2,}")) -> if (this.length % 2 == 0) "+" else "-"
    else -> this
}
