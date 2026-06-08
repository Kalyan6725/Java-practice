class IntegerWrapper{
    public static void main(String[] args) {
        Integer []  i = new Integer[3];
        i[0] = i[1] = i[2] = 10;
        i[0] = 20; // Integer is immutable, we cannot change the value of the object, but we can change the reference of the object
        System.out.println(i[0]);
        System.out.println(i[1]);
        System.out.println(i[2]);
        System.out.println("------------------------");
        Integer[] i2 = {1,2,3};
        for(int j=0;j<i2.length;j++){
            System.out.println(j+"th value:"+i2[j]);
        }
        System.out.println("------------------------");
        for(Integer value:i2){
            System.out.println(value);
        }
    }
}