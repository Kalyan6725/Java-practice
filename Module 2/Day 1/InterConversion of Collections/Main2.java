class Main2 {
    public static void main(String[] args) {
        int[] intArray = {1, 2, 3, 4, 5};
        List<Integer> intList = Arrays.asList(intArray);
        System.out.println("List: " + intList);
        Integer[] newArray = intList.toArray(new Integer[0]);
        System.out.println("Array: " + Arrays.toString(newArray));
    }
}