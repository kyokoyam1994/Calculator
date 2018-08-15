package com.example.kosko.calculator;

import android.arch.lifecycle.ViewModel;

public class CalculatorViewModel extends ViewModel {

    private Calculator calculator;
    private boolean valueDirty;

    public CalculatorViewModel() {
        super();
        calculator = new Calculator();
        valueDirty = false;
    }

    public Calculator getCalculator() {
        return calculator;
    }

    public boolean isValueDirty() {
        return valueDirty;
    }

    public void setValueDirty(boolean valueDirty) {
        this.valueDirty = valueDirty;
    }

}
