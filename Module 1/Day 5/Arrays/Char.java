class Char {
    public static void main(String[] args) {
        char c[] = new char[3];
        c[0] = c[1] = c[2] = 'A';
        System.out.println(c[0]);
        System.out.println(c[1]);
        System.out.println(c[2]);
        System.out.println("------------------------");
        char[] c2 = {'H','E','L','L','O'};
        for(int i=0;i<c2.length;i++){
            System.out.println(i+"th value:"+c2[i]);
        }
        System.out.println("------------------------");
        for(char value:c2){
            System.out.println(value);
        }
    }
}