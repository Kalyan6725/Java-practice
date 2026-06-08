class Main {
    public static void main(String[] args) {
        StringBuilder empty = new StringBuilder("");
        empty.capacity(); // default capacity is 16
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World");
        System.out.println(sb); // Output: Hello World
        sb.insert(0, "Hi, ");
        System.out.println(sb); // Output: Hi, Hello World
        sb.replace(4, 9, "Java");
        System.out.println(sb); // Output: Hi, Java World   
        sb.delete(0, 2);
        System.out.println(sb); // Output: Java World

        StringBuffer sbf = new StringBuffer("Hello");
        sbf.append(" World");
        System.out.println(sbf); // Output: Hello World
        sbf.insert(0, "Hi, ");
        System.out.println(sbf); // Output: Hi, Hello World
        sbf.replace(4, 9, "Java");
        System.out.println(sbf); // Output: Hi, Java World   
        sbf.delete(0, 2);
        System.out.println(sbf); // Output: Java World

        //Diff between StringBuilder and StringBuffer
        //StringBuilder is not synchronized, so it's faster than StringBuffer.
        //StringBuffer is synchronized, so it's thread-safe.
        //StringBuilder is recommended for single-threaded applications, while StringBuffer is recommended for multi-threaded applications.
    }
}