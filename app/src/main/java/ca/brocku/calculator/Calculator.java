package ca.brocku.calculator;


import java.io.Serializable;
import java.util.*;
import java.lang.String;


public class Calculator implements Serializable {

    boolean scientific;
    boolean repeatCalc;

    int previousOppFlag; // 0 = nothing, 1 = operand, 2 = operator
    int OperandNum;

    String input;
    String currentOperand;
    String currentOperator;
    String output;

    Stack<Float> operandStack;
    Stack<Character> operatorStack;

    public Calculator() {

        operandStack = new Stack<Float>(); // 1 2 3
        operatorStack = new Stack<Character>(); // * - / +
        scientific = false;

        reset();
    }

    /**
     * resets all the variables
     * @return display string
     */
    public String reset() {

        operandStack.clear();
        operatorStack.clear();

        repeatCalc = false;

        input = "";
        currentOperand = "";
        currentOperator = "";
        output = "0";

        previousOppFlag = 0;
        OperandNum = 0;

        return "0";
    }


    /**
     * remove last operand or operator / remove last character
     */
    public String back(){

        if(scientific){
            if(input.length()>1) {
                input = input.substring(0, input.length() - 1);
            }
            output = input;
            return input;
        }else{

            if(previousOppFlag == 1 && currentOperand.length()>0){
                currentOperand = currentOperand.substring(0,currentOperand.length()-1);
                output = currentOperand;
                return currentOperand;
            }else if (previousOppFlag == 2){

                if(!operandStack.isEmpty()) {
                    currentOperand = String.valueOf(operandStack.peek());
                }
                previousOppFlag = 1;
                if(OperandNum>0) {
                    OperandNum--;
                }
                output = currentOperand;
                return currentOperand;
            }
        }
        return "";
    }

    public String getOutput(){

        return output;
    }



    /**
     * This method adds an operand's to the currentOperand string
     *
     * @param value .,0,1,2,3,4,5,6,7,8,9
     */
    public String addOperand(char value) {

        previousOppFlag = 1;

        if (scientific) {
            input += value;
            output = input;
            return input;
        }else {
            currentOperand += value;
            output = currentOperand;
            return currentOperand;
        }
    }


    /**
     * @param value *,/,+,-,(,)
     */
    public String addOperator(char value) {

        String returnSting = "";

        if (scientific) {
            input += " " + value;
            output = input;
            return input;
        }else if(value == '(' || value == ')'){
            output = currentOperand;
            return currentOperand;
        }else{

            saveOperand();
            OperandNum++;

            // if user has pressed: operand → operator → operand → operator
            if(OperandNum>1){
                OperandNum = 0;

                returnSting = calculate();   // this value is returned
            }

            // if user already pressed an operator
            if(previousOppFlag == 2 && !operatorStack.isEmpty()) {
                operatorStack.pop();    // overwrite operator to new one.
            }
            operatorStack.push(Character.valueOf(value)); // have to push after calculating or it will mess it up

            currentOperator += value;

        }
        previousOppFlag = 2;


        if(returnSting != ""){
            output = returnSting;
            return returnSting;
        }

        output = currentOperand;
        return currentOperand;
    }

    /**
     * This method converts temp string currentOperand into a float and push's it to a stack
     */
    public void saveOperand() {

        if (currentOperand != "") {
            operandStack.push(Float.valueOf(currentOperand));
            currentOperand = "";
        }
    }


    /**
     *
     * @param sci boolean for switching modes
     */
    public void switchMode(boolean sci) {
        reset();

        scientific = sci;
    }

    /**
     * This method performs the calculations
     * @param a 1st operand
     * @param b 2st operand
     * @param operator
     * @return
     */
    private float operate(float a, float b, char operator) throws ArithmeticException{

        switch (operator) {
            case '*':
                return a * b;
            case '/':
                if(b == 0){
                    throw new ArithmeticException();
                }
                return a / b;
            case '+':
                return a + b;
            case '-':
                return a - b;
        }
        return 0;
    }

    /**
     *
     * @return precedence of the operator following: b e dm as
     * Note this calculator doesn't support exponents so we skip
     */
    private int precedence(char a){

        switch (a) {
            case '(':
                return 3;
            case ')':
                return 3;
            case '*':
                return 2;
            case '/':
                return 2;
            case '+':
                return 1;
            case '-':
                return 1;
            default:
                return 0;
        }
    }


    /**
     * using the public variable input returns a postfix expression
     * @return String postfix expression
     */
    private String postFix(){

        input += " "; // adds escape character.

        int length = input.length();
        String post = "";
        char current;

        for(int i =0; i<length;i++){
            current = input.charAt(i);

            if(isOperator(current)){
                // operator

                // pop all the operators until reaching the corresponding bracket
                if(current == ')'){
                    char temp = ' ';
                    while (!operatorStack.isEmpty() && temp != '('){
                        temp = operatorStack.pop();

                        if(temp != '(') {
                            post += temp;
                        }
                    }
                }else{

                    int a = precedence(current);
                    int b = -1;

                    if(!operatorStack.isEmpty()){
                        b = precedence(operatorStack.peek());
                    }

                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(' && a<=b) {

                        char temp = operatorStack.pop();

                        post += temp;

                        if(!operatorStack.isEmpty()) {
                            b = precedence(operatorStack.peek());
                        }
                    }

                    operatorStack.push(current);
                }

            }else{
                //digit

                if(current == ' ') {
                    //escape character
                    if (currentOperand != "") {
                        saveOperand();

                        post += operandStack.pop() + " ";
                    }

                }else{
                    // digit
                    currentOperand += current;
                }
            }
        }

        // empty stack
        while (!operatorStack.isEmpty()) {

            post += operatorStack.pop();
        }

        return post;
    }


    /**
     * determines if a char is an operator
     * @param a character
     * @return boolean isOperator
     */
    private boolean isOperator(char a){
        return (a == '*' || a == '/' || a == '+' || a == '-' || a == '(' || a == ')');
    }


    /**
     * Takes in a post fix string and returns the answer
     * @param postFix string
     * @return answer String
     */
    private float CalcPostFix(String postFix){

        float op1, op2;
        char opAnd;
        float value = 0;

        operandStack.clear();
        operatorStack.clear();

        int length = postFix.length();
        String temp = "";
        char current;

        for(int i =0; i<length;i++) {
            current = postFix.charAt(i);

            if(isOperator(current)){
                // operator

                op2 = operandStack.pop();
                op1 = operandStack.pop();
                opAnd = current;


                float a = operate(op1, op2, opAnd);
                operandStack.push(a);

            }else{
                //Save digit to the stack.

                if(current == ' ') {
                    //escape character
                    if (currentOperand != "") {
                        saveOperand();
                    }
                }else{
                    // digit
                    currentOperand += current;
                }
            }
        }

        value = operandStack.pop();

        return value;
    }


    /**
     * Calculates the answer for basic mode
     * @return answer string
     */
    private float CalcBasic() {

        float op1, op2;
        char opAnd;
        float value = 0;

        previousOppFlag = 0;

        saveOperand();  // save previously entered value

        op2 = operandStack.pop();
        opAnd = operatorStack.pop();

        if (operandStack.isEmpty()) {
            // only one operator
            op1 = op2;
        } else {
            op1 = operandStack.pop();
        }

        value = operate(op1, op2, opAnd);
        operandStack.push(value);

        return value;
    }

    /**
     * Called when user clicks =
     *
     * @return string answer returns both numbers and errors to be displayed
     */
    public String calculate() {

        float value = 0;
        String Val = "";

        OperandNum = 0;

        if(!scientific) {

            if(operatorStack.isEmpty()){
                return "";
            }

            try {
                value = CalcBasic();
            }catch (Exception e){
                return "ERROR";
            }

        }else{

            String postVal = postFix();

            try {
                value = CalcPostFix(postVal);
            }catch (Exception e){
                return "ERROR";
            }


        }
        output = String.valueOf(value);
        return String.valueOf(value);
    }

}
