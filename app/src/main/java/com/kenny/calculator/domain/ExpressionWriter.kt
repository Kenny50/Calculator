package com.kenny.calculator.domain

class ExpressionWriter {

    var expression = ""

    fun processAction(action: CalculatorAction) {
        when (action) {
            CalculatorAction.Calculate -> {
                val parser = ExpressionParser(prepareForCalculate())
                val evaluator = ExpressionEvaluator(parser.parse())
                expression = evaluator.evaluate().toString()
            }
            CalculatorAction.Clear -> expression = ""
            CalculatorAction.Decimal -> {
                if (canEnterDecimal()) {
                    expression += "."
                }
            }
            CalculatorAction.Delete -> expression = expression.dropLast(1)
            is CalculatorAction.Number -> expression += action.number
            is CalculatorAction.Op -> {
                if (canEnterOperator(action.operation)) {
                    expression += action.operation.symbol
                }
            }
            CalculatorAction.Parentheses -> {
                processParentheses()
            }
        }
    }

    private fun prepareForCalculate(): String {
        val newExpression = expression.takeLastWhile {
            it in "$operationSymbols(."
        }

        if (newExpression.isEmpty()) {
            return "0"
        }
        return newExpression
    }

    private fun processParentheses() {
        val openingCount = expression.count { it == '(' }
        val closingCount = expression.count { it == ')' }

        expression += when {
            expression.isEmpty() ||
                    expression.last() in "$operationSymbols(" -> "("
            expression.last() in "1234567890)" &&
                    openingCount == closingCount -> return
            else -> ")"
        }
    }

    private fun canEnterDecimal(): Boolean {
        if (expression.isEmpty() || expression.last() in "$operationSymbols.()") {
            return false
        }
        // make sure 4+5.5 ok, 4+5.5.5 invalid
        return !expression.takeLastWhile {
            it in "1234567890."
        }.contains(".")
    }

    private fun canEnterOperator(operation: Operation): Boolean {
        if (operation in listOf(Operation.ADD, Operation.SUBTRACT)) {
            return expression.isEmpty() || expression.last() in "$operationSymbols()1234567890"
        }
        return expression.isNotEmpty() || expression.last() in "123457890)"
    }
}