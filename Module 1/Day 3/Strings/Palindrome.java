
class Palindrome {
    public static void main(String[] args) {
        String original = "Hell olleH";

        for (int i = 0; i < original.length() / 2; i++) {
            if(original.charAt(i) == original.charAt(original.length()-i-1)){
                continue;
            }
            else{
                System.out.println("Not a Palindrome");
                return;
            }
        }
        System.out.println("Palindrome");
        // String reversed = "";
        // for (int i = original.length() - 1; i >= 0; i--) {
        //     reversed += original.charAt(i);
        // }
        // System.out.println("Original string: " + original);
        // System.out.println("Reversed string: " + reversed);
        // System.out.println("Is palindrome: " + original.equals(reversed));
    }
}