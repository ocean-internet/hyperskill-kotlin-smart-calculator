package calculator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CalculatorTest {

    @Test
    fun testPostfixToAnswer() {

        val calculator = Calculator()
        val postfix = "10 2 8 * + 3 -"
        val result = calculator.postfixToAnswer(postfix)

        assertEquals("23", result)
    }

    @Test
    fun testInfixToPostfix() {
        val calculator = Calculator()
        val infix = "10 + 2 * 8 - 3"
        val result = calculator.infixToPostfix(infix)

        assertEquals("10 2 8 * + 3 -", result)
    }

    @Test
    fun testItHandlesDuplicateAdditionOperator() {
        val calculator = Calculator()
        val infix = "1 ++ 1"
        val result = calculator.infixToPostfix(infix)

        assertEquals("1 1 +", result)
    }

    @Test
    fun testItHandlesDuplicateSubtractOperator() {
        val calculator = Calculator()
        val infix = "1 -- 1 --- 1"
        val result = calculator.infixToPostfix(infix)

        assertEquals("1 1 + 1 -", result)
    }

    @Test
    fun testInfixToAnswer() {
        val calculator = Calculator()
        val infix = "2 - 2 + 3"
        val result = calculator.infixToAnswer(infix)

        assertEquals("3", result)
    }

    @Test
    fun testHandlesBrackets() {
        val calculator = Calculator()
        val infix = "8 * 3 + 12 * (4 - 2)"

        assertEquals("8 3 * 12 4 2 - * +", calculator.infixToPostfix(infix))

        val result = calculator.infixToAnswer(infix)
        assertEquals("48", result)
    }

    @Test
    fun testHandlesNoSpaces() {
        val calculator = Calculator()
        val infix = "a*2+b*3+c*(2+3)"
        val result = calculator.infixToPostfix(infix)

        assertEquals("a 2 * b 3 * + c 2 3 + * +", result)
    }

    @Test
    fun testHandlesBigIntegers() {
        val calculator = Calculator()
        val infix = "112234567890 + 112234567890 * (10000000999 - 999)"
        val result = calculator.infixToAnswer(infix)

        assertEquals("1122345679012234567890", result)
    }
}