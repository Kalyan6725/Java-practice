public class TypeConversions {
    public static void main(String[] args) {

        String strInt = "100";
        String strDouble = "10.5";

        int i = Integer.parseInt(strInt);
        double d = Double.parseDouble(strDouble);

        System.out.println("String to int: " + i);
        System.out.println("String to double: " + d);

        String s1 = Integer.toString(i);
        String s2 = Double.toString(d);

        System.out.println("Int to String: " + s1);
        System.out.println("Double to String: " + s2);
 
        int intVal = 50;
        double doubleVal = intVal; // int -> double  IMPLICIT CASTING

        System.out.println("Int to Double (implicit): " + doubleVal);

        double dVal = 99.99;
        int intFromDouble = (int) dVal; // double -> int  EXPLICIT CASTING

        System.out.println("Double to Int (explicit): " + intFromDouble);

        byte b = 10;
        short s = b;       
        int num = s;       
        long l = num;      

        System.out.println("Byte to Short: " + s);
        System.out.println("Short to Int: " + num);
        System.out.println("Int to Long: " + l);

        long longVal = 1000L;
        int intVal2 = (int) longVal;
        short shortVal = (short) intVal2;
        byte byteVal = (byte) intVal2;

        System.out.println("Long to Int: " + intVal2);
        System.out.println("Int to Short: " + shortVal);
        System.out.println("Int to Byte: " + byteVal);

        char ch = 'A';
        int charToInt = ch;

        System.out.println("Char to Int (ASCII): " + charToInt);

        int numToChar = 66;
        char intToChar = (char) numToChar;

        System.out.println("Int to Char: " + intToChar);

        boolean bool = true;
        //int Inte =(int)bool;  //Boolean cannot be converted to int
        //System.out.println("Boolean to int: "+ Inte);
        String boolStr1 = String.valueOf(bool);
        String boolStr = Boolean.toString(bool);

        System.out.println("Boolean to String: " + boolStr);
        System.out.println("Boolean to String with valueOf: " + boolStr1);

        String strBool = "true";// true ,True ,TRUE
        boolean parsedBool = Boolean.parseBoolean(strBool);

        System.out.println("String to Boolean: " + parsedBool);
    }
}