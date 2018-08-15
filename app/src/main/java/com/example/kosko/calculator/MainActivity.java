package com.example.kosko.calculator;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private static final String RESULT_KEY = "result";
    private final String DIVIDE_BY_ZERO_MESSAGE = "Cannot divide by zero";
    private CalculatorViewModel calculatorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculatorViewModel = ViewModelProviders.of(this).get(CalculatorViewModel.class);
        TextView result = findViewById(R.id.resultView);

        String text = BigDecimal.ZERO.toString();
        if (savedInstanceState != null) text = savedInstanceState.getString(RESULT_KEY);

        result.setText(text);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView result = findViewById(R.id.resultView);
        outState.putString(RESULT_KEY, result.getText().toString());
    }

    public void concatDigit(View v) {
        Button numButton = (Button) v;
        TextView result = findViewById(R.id.resultView);
        String number = numButton.getText().toString();
        if (result.getText().toString().equals(BigDecimal.ZERO.toString()) || calculatorViewModel.isValueDirty()) {
            result.setText(number);
            calculatorViewModel.setValueDirty(false);
        }
        else result.setText(result.getText().toString().concat(number));
    }

    public void concatDecimal(View v) {
        TextView result = findViewById(R.id.resultView);
        if (calculatorViewModel.isValueDirty()) {
            result.setText(BigDecimal.ZERO.toString().concat("."));
            calculatorViewModel.setValueDirty(false);
        } else if (!result.getText().toString().contains(".")) result.setText(result.getText().toString().concat("."));
    }

    public void negateValue(View v) {
        TextView result = findViewById(R.id.resultView);
        if (result.getText().toString().startsWith("-")) {
            result.setText(result.getText().toString().replaceFirst("-", ""));
            calculatorViewModel.setValueDirty(false);
        } else if (!new BigDecimal(result.getText().toString()).equals(BigDecimal.ZERO)) {
            result.setText("-".concat(result.getText().toString()));
            calculatorViewModel.setValueDirty(false);
        }
    }

    public void operate(View v) {
        Button opButton = (Button) v;
        TextView result = findViewById(R.id.resultView);
        if (!calculatorViewModel.isValueDirty()) {
            if (calculatorViewModel.getCalculator().getCurrentOperation() == null) {
                calculatorViewModel.getCalculator().setTotal(new BigDecimal(result.getText().toString()));
            } else {
                try {
                    calculatorViewModel.getCalculator().performOperation(new BigDecimal(result.getText().toString()));
                } catch (ArithmeticException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), DIVIDE_BY_ZERO_MESSAGE, Toast.LENGTH_SHORT).show();
                    return;
                }
                String textRepresentation = calculatorViewModel.getCalculator().getTotal().toString();
                if (isIntegerValue(calculatorViewModel.getCalculator().getTotal())) {
                    textRepresentation = calculatorViewModel.getCalculator().getTotal().toBigInteger().toString();
                }
                result.setText(textRepresentation);
            }
        }

        if (opButton.getId() == R.id.buttonPlus) {
            calculatorViewModel.getCalculator().setCurrentOperation(Calculator.Operation.ADD);
        } else if (opButton.getId() == R.id.buttonMinus) {
            calculatorViewModel.getCalculator().setCurrentOperation(Calculator.Operation.SUBTRACT);
        } else if (opButton.getId() == R.id.buttonMultiply) {
            calculatorViewModel.getCalculator().setCurrentOperation(Calculator.Operation.MULTIPLY);
        } else if(opButton.getId() == R.id.buttonDivide) {
            calculatorViewModel.getCalculator().setCurrentOperation(Calculator.Operation.DIVIDE);
        }
        calculatorViewModel.setValueDirty(true);
    }

    public void equate(View v) {
        TextView result = findViewById(R.id.resultView);
        if(calculatorViewModel.getCalculator().getCurrentOperation() != null) {
            try {
                calculatorViewModel.getCalculator().performOperation(new BigDecimal(result.getText().toString()));
            } catch (ArithmeticException ex) {
                Toast.makeText(getApplicationContext(), DIVIDE_BY_ZERO_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }
            String textRepresentation = calculatorViewModel.getCalculator().getTotal().toString();
            if (isIntegerValue(calculatorViewModel.getCalculator().getTotal())) {
                textRepresentation = calculatorViewModel.getCalculator().getTotal().toBigInteger().toString();
            }
            result.setText(textRepresentation);
            calculatorViewModel.setValueDirty(true);
        }
    }

    public void deleteCurrentValue(View v) {
        if(calculatorViewModel.isValueDirty()) return;

        TextView result = findViewById(R.id.resultView);
        String resultString = BigDecimal.ZERO.toString();
        if (result.getText().length() > 1 && !result.getText().toString().substring(0, result.getText().length() - 1).equals("-")) {
            resultString = result.getText().toString().substring(0, result.getText().length() - 1);
        }
        result.setText(resultString);
    }

    public void clearCalculator(View v) {
        calculatorViewModel.setValueDirty(false);
        calculatorViewModel.getCalculator().resetState();
        TextView result = findViewById(R.id.resultView);
        result.setText(BigDecimal.ZERO.toString());
    }

    private boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }

}