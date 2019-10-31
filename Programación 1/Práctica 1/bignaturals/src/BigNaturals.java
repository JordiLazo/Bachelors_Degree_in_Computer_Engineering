import acm.program.CommandLineProgram;

import java.util.Arrays;

/**
 * Implementations of infinite precision naturals using arrays of ints
 * <p>
 * - Each position in the array corresponds to a digit (between 0 and 9)
 * - Least significative digit on pos 0
 * - No 0's allowed on the front (except for the case of number 0)
 *
 * @author jmgimeno
 */

public class BigNaturals extends CommandLineProgram {

    public int[] zero() {

        int[] array_zero = {0};

        return array_zero;
    }

    public int[] one() {

        int[] array_one = {1};

        return array_one;
    }

    public boolean equals(int[] number1, int[] number2) {

        int i = 0;

        boolean array_equals = true;

        if (number1.length == number2.length){

            while (i < number1.length && array_equals == true){

                if (number1[i] == number2[i]){

                    array_equals = true;

                }else{

                    array_equals = false;
                }

                i++;
            }

        }else{

            array_equals = false;
        }

        return array_equals;
    }

    public int[] add(int[] num1, int[] num2) {

        int positions_sum = 0, array_length; int [] array_num;

        if (num1.length > num2.length){

            positions_sum = num1.length + 1;

            array_length = num1.length;

            array_num = num1;

        }else{

            positions_sum = num2.length + 1;

            array_length = num2.length;

            array_num = num2;
        }

        int carry=0, i=0,j,k_aux=0; int[] array_add = new int[positions_sum];

        while (i < num1.length && i < num2.length) {

            array_add[i] = num1[i] + num2[i] + carry;

            carry = 0;

            if (array_add[i] >= 10) {

                array_add[i] -= 10;

                carry = 1;
            }

            i++;
        }
        for ( j = i; j < array_length; j++) {

            array_add[j] = array_num[j] + carry;

            carry = 0;

            if (array_add[j] >= 10) {

                array_add[j] -= 10;

                carry = 1;
            }
        }

        if (carry != 0) {

            array_add[j] +=carry;
        }

        if (array_add[array_add.length - 1] == 0 && array_add.length > 1) {

            for (i = array_add.length - 1; i > 0 && array_add[i] == 0; i--) {

                k_aux++;

            }

            int[] array_add_aux = new int[array_add.length - k_aux];

            for (i = 0; i < array_add_aux.length; i++) {

                array_add_aux[i] = array_add[i];
            }

            return array_add_aux;

        } else {

            return array_add;
        }
    }

     public int[] shiftLeft(int[] number, int positions) {

        if (equals(number, zero())) {
            return number;
        }

        int[] array_shiftleft = new int[positions + number.length];

        for (int i = positions; i < array_shiftleft.length; i++) {
            array_shiftleft[i] = number[i - positions];
        }

        return array_shiftleft;
    }

    public int[] multiplyByDigit(int[] number, int digit) {

        if (digit == 0) {

            return zero();
        }

        int[] array_multiplybydigit = zero();

        while (digit > 0) {

            array_multiplybydigit = add(array_multiplybydigit, number);

            digit--;
        }
        return array_multiplybydigit;
    }

    public int[] multiply(int[] number1, int[] number2) {

        int[] array_multiply = zero(), array_multiply_aux;

        for (int i = 0; i < number2.length; i++) {

            array_multiply_aux = multiplyByDigit(number1, number2[i]);

            array_multiply_aux = shiftLeft(array_multiply_aux, i);

            array_multiply = add(array_multiply, array_multiply_aux);

        }
        return array_multiply;
    }

    public int[] factorial(int[] number) {

        if (equals(number, zero())) {

            return one();

        } else {

            int[] array_factorial = number, factor = one();

            while (equals(factor, number) == false) {

                array_factorial = multiply(array_factorial, factor);

                factor = add(factor, one());
            }

            return array_factorial;

        }
    }

    public void run() {

        testFromString();
        testAsString();
        testZero();
        testOne();
        testEquals();
        testAdd();
        testShiftLeft();
        testMultiplyByDigit();
        testMultiply();
        testFactorial();
    }

    public static void main(String[] args) {
        new BigNaturals().start(args);
    }

    // -----
    // TESTS 
    // -----

    // Functions for simplifying vector tests.

    public int[] fromString(String number) {
        // "25"  -> {5, 2}
        // "1"   -> {1}
        int[] digits = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            String digit = number.substring(i, i + 1);
            digits[number.length() - i - 1] = Integer.parseInt(digit);
        }
        return digits;
    }

    private void testFromString() {
        println("Inicio de las pruebas de fromString");
        if (!Arrays.equals(new int[]{5, 2}, fromString("25"))) {
            println("Error en el caso \"25\"");
        }
        if (!Arrays.equals(new int[]{1}, fromString("1"))) {
            println("Error en el caso \"1\"");
        }
        println("Final de las pruebas de fromString");
    }

    public String asString(int[] ints) {
        // {1}    -> "1"
        // {5, 2} -> "25"
        char[] intToChar =
                new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] chars = new char[ints.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = intToChar[ints[ints.length - i - 1]];

        }
        return new String(chars);
    }

    private void testAsString() {
        println("Inicio de las pruebas de asString");
        if (!"25".equals(asString(new int[]{5, 2}))) {
            println("Error en el caso int[] {5, 2}");
        }
        if (!"1".equals(asString(new int[]{1}))) {
            println("Error en el caso int[] {1}");
        }
        println("Final de las pruebas de asString");
    }

    private void testZero() {
        println("Inicio de las pruebas de zero");
        if (!"0".equals(asString(zero()))) {
            println("Error en la función zero");
        }
        println("Final de las pruebas de zero");
    }

    private void testOne() {
        println("Inicio de las pruebas de one");
        if (!"1".equals(asString(one()))) {
            println("Error en la función one");
        }
        println("Final de las pruebas de one");
    }

    private void testEquals() {
        println("Inicio de las pruebas de equals");
        if (equals(zero(), one())) {
            println("Error en el caso 0 != 1");
        }
        if (!equals(one(), one())) {
            println("Error en el caso 1 = 1");
        }
        if (!equals(fromString("12345"), fromString("12345"))) {
            println("Error en el caso 12345 = 12345");
        }
        println("Final de las pruebas de equals");
    }

    private boolean checkAdd(String number1, String number2, String result) {
        return Arrays.equals(add(fromString(number1), fromString(number2)),
                fromString(result));
    }

    private void testAdd() {
        println("Inicio de las pruebas de add");
        if (!checkAdd("1", "1", "2")) {
            println("Error en la suma 1 + 1 = 2");
        }
        if (!checkAdd("5", "5", "10")) {
            println("Error en la suma 5 + 5 = 10");
        }
        if (!checkAdd("99", "999", "1098")) {
            println("Error en la suma 99 + 999 = 1098");
        }
        if (!checkAdd("999", "99", "1098")) {
            println("Error en la suma 999 + 99 = 1098");
        }
        if (!checkAdd("5", "0", "5")) {
            println("Error en la suma 5 + 0 = 5");
        }
        println("Final de las pruebas de add");
    }

    private boolean checkShiftLeft(String number, int exponent, String result) {
        return Arrays.equals(shiftLeft(fromString(number), exponent),
                fromString(result));
    }

    private void testShiftLeft() {
        println("Inicio de las pruebas de shiftLeft");
        if (!checkShiftLeft("54", 1, "540")) {
            println("Error en 54 1 posición a la izquierda = 540");
        }
        if (!checkShiftLeft("54", 3, "54000")) {
            println("Error en 54 3 posiciones a la izquierda = 54000");
        }
        if (!checkShiftLeft("0", 3, "0")) {
            println("Error en 0 3 posiciones a la izquierda = 0");
        }
        println("Final de las pruebas de shiftLeft");
    }

    private boolean checkMultiplyByDigit(String number, int digit, String result) {
        return Arrays.equals(multiplyByDigit(fromString(number), digit),
                fromString(result));
    }

    private void testMultiplyByDigit() {
        println("Inicio de las pruebas de multiplyByDigit");
        if (!checkMultiplyByDigit("24", 2, "48")) {
            println("Error en 24 * 2 = 48");
        }
        if (!checkMultiplyByDigit("54", 3, "162")) {
            println("Error en 54 * 3 = 162");
        }
        if (!checkMultiplyByDigit("0", 3, "0")) {
            println("Error en 0 * 3 = 0");
        }
        if (!checkMultiplyByDigit("24", 0, "0")) {
            println("Error en 24 * 0 = 0");
        }
        println("Final de las pruebas de multiplyByDigit");
    }

    // multiply

    private boolean checkMultiply(String number1, String number2, String result) {
        return Arrays.equals(multiply(fromString(number1), fromString(number2)),
                fromString(result));
    }

    private void testMultiply() {
        println("Inicio de las pruebas de multiply");
        if (!checkMultiply("2", "3", "6")) {
            println("Error en 2 * 3 = 6");
        }
        if (!checkMultiply("999", "888", "887112")) {
            println("Error en 999 * 888 = 887112");
        }
        if (!checkMultiply("10", "5", "50")) {
            println("Error en 10 * 50 = 50");
        }
        if (!checkMultiply("12", "555443535", "6665322420")) {
            println("Error en 12 * 555443535 = 6665322420");
        }
        if (!checkMultiply("555443535", "12", "6665322420")) {
            println("Error en 555443535 * 12 = 6665322420");
        }
        if (!checkMultiply("999", "10", "9990")) {
            println("Error en 99 * 10 = 90");
        }
        if (!checkMultiply("100", "5", "500")) {
            println("Error en 10 * 50 = 50");
        }
        if (!checkMultiply("5", "17", "85")) {
            println("Error en 5 * 17 = 85");
        }
        if (!checkMultiply("24", "5", "120")) {
            println("Error en 24 * 5 = 120");
        }
        if (!checkMultiply("0", "888", "0")) {
            println("Error en 0 * 888 = 0");
        }
        if (!checkMultiply("20397882081197443358640281739902897356800000000",
                "40",
                "815915283247897734345611269596115894272000000000")) {
            println("Error en último producto de 40!");
        }
        if (!checkMultiply("815915283247897734345611269596115894272000000000",
                "41",
                "33452526613163807108170062053440751665152000000000")) {
            println("Error en último producto de 41!");
        }
        println("Final de las pruebas de multiply");
    }


    // factorial

    private boolean checkFactorial(String number, String result) {
        return Arrays.equals(factorial(fromString(number)), fromString(result));
    }

    private void testFactorial() {
        println("Inicio de las pruebas de factorial");
        if (!checkFactorial("5", "120")) {
            println("Error en 5! = 120");
        }
        if (!checkFactorial("6", "720")) {
            println("Error en 6! = 720");
        }
        if (!checkFactorial("7", "5040")) {
            println("Error en 7! = 5040");
        }
        if (!checkFactorial("8", "40320")) {
            println("Error en 8! = 720");
        }
        if (!checkFactorial("9", "362880")) {
            println("Error en 9! = 720");
        }
        if (!checkFactorial("11", "39916800")) {
            println("Error en 11! = 720");
        }
        if (!checkFactorial("10", "3628800")) {
            println("Error en 10! = 3628800");
        }
        if (!checkFactorial("15", "1307674368000")) {
            println("Error en 15! = 1307674368000");
        }
        if (!checkFactorial("20", "2432902008176640000")) {
            println("Error en 20! = 2432902008176640000");
        }
        if (!checkFactorial("40", "815915283247897734345611269596115894272000000000")) {
            println("40! ---------> " + asString(factorial(fromString("40"))));
            println("Error en 40! = 815915283247897734345611269596115894272000000000");
        }
        if (!checkFactorial("41", "33452526613163807108170062053440751665152000000000")) {
            println("41! ---------> " + asString(factorial(fromString("41"))));
            println("Error en 41! = 33452526613163807108170062053440751665152000000000");
        }
        if (!checkFactorial("42", "1405006117752879898543142606244511569936384000000000")) {
            println("42! ---------> " + asString(factorial(fromString("42"))));
            println("Error en 42! = 1405006117752879898543142606244511569936384000000000");
        }
        println("Final de las pruebas de factorial");
    }
}
