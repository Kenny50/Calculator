package com.kenny.calculator.domain

class ExpressionParser(
    private val calculation: String
) {
    fun parse(): List<ExpressionPart> {
        val result = mutableListOf<ExpressionPart>()

        var index = 0
        while (index < calculation.length) {
            val curChar = calculation[index]
            when {
                curChar in operationSymbols -> {
                    result.add(
                        ExpressionPart.Op(operationFromSymbol(curChar))
                    )
                }
                curChar.isDigit() -> {
                    index = parseNumber(index, result)
                    continue
                }
                curChar in "()" -> {
                    parseParentheses(curChar, result)
                }
            }
            index++
        }

        return result
    }

    private fun parseNumber(startIndex: Int, result: MutableList<ExpressionPart>): Int {
        var i = startIndex

        val numberAsString = buildString {
            while (i < calculation.length && calculation[i] in "1234567890.") {
                append(calculation[i])
                i++
            }
        }

        result.add(
            ExpressionPart.Number(numberAsString.toDouble())
        )
        return i
    }

    private fun parseParentheses(curChar: Char, result: MutableList<ExpressionPart>) {
        result.add(
            ExpressionPart.Parentheses(
                type = when (curChar) {
                    '(' -> ParenthesesType.Opening
                    ')' -> ParenthesesType.Closing
                    else -> throw IllegalArgumentException("Invalid paretheses type")
                }
            )
        )
    }
}