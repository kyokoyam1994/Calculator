package com.example.kosko.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    private Operation currentOperation;
    private BigDecimal total;

    public Calculator() {
        resetState();
    }

    public Operation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(Operation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void resetState() {
        total = BigDecimal.ZERO;
        currentOperation = null;
    }

    public void performOperation(BigDecimal value) {
        if (currentOperation != null) {
            switch (currentOperation) {
                case ADD:
                    total = total.add(value).stripTrailingZeros();
                    break;
                case SUBTRACT:
                    total = total.subtract(value).stripTrailingZeros();
                    break;
                case MULTIPLY:
                    total = total.multiply(value).stripTrailingZeros();
                    break;
                case DIVIDE:
                    total = total.divide(value, 10, RoundingMode.HALF_UP).stripTrailingZeros();
                    break;
            }
            currentOperation = null;
        }
    }

}
