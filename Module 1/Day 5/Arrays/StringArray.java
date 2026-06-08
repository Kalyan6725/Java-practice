import java.util.Arrays;
class StringArray{
    public static void main(String[] args) {
        String[] arr = new String[5];
        arr[0]="Kalyan";
        arr[1]="Harsh";
        arr[2]="Apuroop";
        arr[3]="Vishnu";
        arr[4]="Kedar";
        for(String val:arr){
            System.out.println(val);
        }
        System.out.println("After sorting the array:");
        Arrays.sort(arr); // This will sort the array in alphabetical order
        for(String val:arr){
            System.out.println(val);
        }
        System.out.println(Arrays.toString(arr)); // This will print the entire array in a readable format

        String sentance="vdsjb,dgsd,sdsdf,sdvd ,dvsd";
        String[] str=sentance.split(",");
        for(String val2:str){
            System.out.println(val2);
        }
        System.out.println(Arrays.toString(str)); // This will print the entire array in a readable format
    }
}