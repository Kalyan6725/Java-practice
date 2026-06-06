//Vowels.java
class Vowels {
    public static void main(String[] args){
        String s="Hello A WOrld";
        String Vowels="aeiouAEIOU";
        int k=1;
        for( int i=0;i<s.length()-1;i++){
            if(Vowels.indexOf(s.charAt(i)) != -1){
                System.out.println("Vowel " + k + ":" + s.charAt(i));
                k++;
            }
        }
    }
}