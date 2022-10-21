package com.kenny.calculator.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExpressionParserTest {

    private lateinit var parser: ExpressionParser

    @Test
    fun `Simple expression is properly parsed`() {
        //1. Given
        parser = ExpressionParser("3+5-3x4/3")

        //2. do something with whats given
        val actual = parser.parse()

        //3. assert expected == actual
        val expected = listOf(
            ExpressionPart.Number(3.0),
            ExpressionPart.Op(Operation.ADD),
            ExpressionPart.Number(5.0),
            ExpressionPart.Op(Operation.SUBTRACT),
            ExpressionPart.Number(3.0),
            ExpressionPart.Op(Operation.MULTIPLY),
            ExpressionPart.Number(4.0),
            ExpressionPart.Op(Operation.DIVIDE),
            ExpressionPart.Number(3.0),
        )

        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `expression with parentheses is properly parsed`() {
        parser = ExpressionParser("4.21-(4x5)")

        val actual = parser.parse()

        val expected = listOf(
            ExpressionPart.Number(4.21),
            ExpressionPart.Op(Operation.SUBTRACT),
            ExpressionPart.Parentheses(ParenthesesType.Opening),
            ExpressionPart.Number(4.0),
            ExpressionPart.Op(Operation.MULTIPLY),
            ExpressionPart.Number(5.0),
            ExpressionPart.Parentheses(ParenthesesType.Closing),
        )

        assertThat(expected).isEqualTo(actual)
    }
}