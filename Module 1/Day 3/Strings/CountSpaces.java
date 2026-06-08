// CountSpaces.java
class CountSpaces {
    public static void main(String[] args){
        String s="Hel lo World";
        s=s.toLowerCase();
        int k=0;
        for( int i=0;i<s.length()-1;i++){
            if(s.charAt(i)==' '){
                k++;
            }
        }
        System.out.println("Total spaces: " + k);
    }
}