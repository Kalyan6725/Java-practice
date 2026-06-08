class Long {
    public static void main(String[] args) {
        long a [] = new long[3];
        a[0] = a[1] = a[2] = 10000000000L; // We need to add 'L' at the end of the number to indicate that it is a long literal
        System.out.println(a[0]);
        System.out.println(a[1]);
        System.out.println(a[2]);
        System.out.println("------------------------");
        long a2[]={
            10000000000L,20000000000L,30000000000L
        };
        for(int i=0;i<a2.length;i++){
            System.out.println(i+"th value:"+a2[i]);
        }
        System.out.println("------------------------");
        for(long value:a2){
            System.out.println(value);
        }
    }
}