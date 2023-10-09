package com.example.exercise_1_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView resultTv,expressionTV;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;

    private String previousResult = "";
    private String currentExpression = "";
    private boolean isNewCalculation = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        expressionTV= findViewById(R.id.expression_TV);


        assignId(buttonC,R.id.button_C);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonEquals,R.id.button_equals);
        assignId(button0,R.id.button_0);
        assignId(button1,R.id.button_1);
        assignId(button2,R.id.button_2);
        assignId(button3,R.id.button_3);
        assignId(button4,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);
        assignId(buttonAC,R.id.button_AC);
        assignId(buttonDot,R.id.button_dot);
    }

    void assignId(MaterialButton btn,int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = expressionTV.getText().toString();

        if (buttonText.equals("AC")) {
            expressionTV.setText("");
            resultTv.setText("0");
            isNewCalculation = true;
            return;
        }

        if (buttonText.equals("=")) {
            // Evaluate the expression
            String result = getResult(dataToCalculate);
            resultTv.setText(result);

            previousResult = result;
            isNewCalculation = true;
            return;
        }

        // Handle number and operator button clicks
        if (isNewCalculation) {
            // If it's a new calculation, clear the expression TextView
            dataToCalculate = "";
            isNewCalculation = false;
        }
        // 0/0 not a number
        if (buttonText.equals("C")) {
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
        } else if (buttonText.equals(".")) {
            // Handle decimal point input
            if (!dataToCalculate.endsWith(".")) {
                // If expression does not already end with a decimal point, append it
                dataToCalculate += buttonText;
            }
        } else if (buttonText.matches("[0-9]")) {
            // Handle digit input
            dataToCalculate += buttonText;
            resultTv.setText("0");
            previousResult = "";
        } else {
            // Handle operator input
            dataToCalculate += previousResult + buttonText  ;
        }

        expressionTV.setText(dataToCalculate);

        if (dataToCalculate.isEmpty()) {
            // If dataToCalculate is empty, set resultTv to "0".
            expressionTV.setText("");
            resultTv.setText("0");
        }
    }
    // Inside getResult method
    String getResult(String data) {
        try {
            // Handle division by zero
            if (data.contains("/")) {
                String[] parts = data.split("/");
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);

                if (denominator == 0) {
                    // Check for 0/0 and display "Not a number"
                    if (numerator == 0) {
                        return "Not a number";
                    } else {
                        return "Cannot divide by zero";
                    }
                }
            }

            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Error: Invalid expression";
        }
    }

}
