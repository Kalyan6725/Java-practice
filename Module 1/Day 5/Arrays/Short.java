class Short {
    public static void main(String[] args) {
        short sh[] = new short[3];
        sh[0] = sh[1] = sh[2] = 5;
        System.out.println(sh[0]);
        System.out.println(sh[1]);      
        System.out.println(sh[2]);

        short[] sh2 = {1,2,3};
        for(int i=0;i<sh2.length;i++){
            System.out.println(i+"th value:"+sh2[i]);
        }

        for(short value:sh2){
            System.out.println(value);
        }
    }
}