class MainObject{
    public static void main(String[] args) {
        // Object obj1 = new Object();
        // Object obj2 = new Object();
        Person p1 = new Person("kal", "sams", 21);
        Person p2 = new Person("qwer", "ty", 32);
        Object p3 = p1;
        System.out.println(p1==p2);
        //System.out.println(p1==p3);
        System.out.println(p1.equals(p2));
        System.out.println(p1.equals(p3));

        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
        System.out.println(p3.hashCode());
        System.out.println(p1.hashCode()==p2.hashCode());
        System.out.println(p1.hashCode()==p3.hashCode());
        System.out.println(p1.getClass().getSimpleName());
        System.out.println(p2.getClass().getName());
        System.out.println(p2.toString());
        System.out.println(p2);
    }
}