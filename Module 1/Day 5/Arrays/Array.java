import java.util.Arrays;
class Array {
    public static void main(String[] args) {
        int[] arr=new int[5];
        // arr[0]=1;
        // arr[1]=2;
        // arr[2]=3;
        // arr[3]=4;
        // arr[4]=5;
        Arrays.fill(arr, 10); // This will fill the entire array with the value 10

        for(int i=0;i<5;i++){
            System.out.println(i+"th value:"+arr[i]);
        }



        int[] arr2={1,2,3,4,5};
        System.out.println(Arrays.toString(arr2)); // This will print the entire array in a readable format
        arr2[0]=10; //Arrays are mutable, we can change the values of the array
        for(int i=0;i<arr2.length;i++){
            System.out.println(i+"th value:"+arr2[i]);
        }

        for(int value:arr2){
            System.out.println(value);
        }
    }
}