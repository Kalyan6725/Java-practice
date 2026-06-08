
class Functions{
    String s1="Hello";
    String s2="Hello";
    String s=new String("Hello");
    void viewFunctions(){
        // s1 = s1.toUpperCase();
        // System.out.println(s1);

        // System.out.println(s1.length());
        System.out.println(s1==s2); // true
        System.out.println(s1==s); // false
        System.out.println(s.intern()==s2); // true
        s2=s2+"world";
        System.out.println(s1==s2); // false

        //String functions
        System.out.println(s1.charAt(0)); // H
        System.out.println(s1.charAt(s1.length()-1)); // o
        System.out.println(s1.indexOf('o')); // 4
        System.out.println(s1.substring(1,4)); // ell
        System.out.println(s1.replace('l', 'p')); // Heppo
        System.out.println(s1.toLowerCase()); // hello
        System.out.println(s1.toUpperCase()); // HELLO
        System.out.println(s1.trim()); // Hello
        System.out.println(s1.split("e")[0]); // H
        System.out.println(s1.split("e")[1]); // llo
        System.out.println(s1.contains("lo")); // true
        System.out.println(s1.startsWith("He")); // true
        System.out.println(s1.endsWith("lo")); // true
        System.out.println(s1.equals("Hello")); // true
        System.out.println(s1.equalsIgnoreCase("hello")); // true
        System.out.println(s1.compareTo("Hello")); // 0
        System.out.println(s1.lastIndexOf("Hello")); // 0

        
    }
    
}