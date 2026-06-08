class Byte {
    public static void main(String[] args) {
        byte[] b = new byte[3];
        b[0] =b[1] = b[2] = 3;

        System.out.println(b[0]);
        System.out.println(b[1]);
        System.out.println(b[2]);


        byte[] b2 = {1,2,3};
        for(int i=0;i<b2.length;i++){
            System.out.println(i+"th value:"+b2[i]);
        }

        for(byte value:b2){
            System.out.println(value);
        }
    }
}