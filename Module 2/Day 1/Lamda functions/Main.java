import java.util.function.BiFunction;
import java.util.function.Function;
class Main {
    public static void main(String[] args) {
        Function<String, Integer> d=(String s)->{ return s.length(); };
        System.out.println(d.apply("Hello"));

        BiFunction<Integer,Integer,Integer> c=(Integer n1, Integer n2)->{ return n1+n2; };
        System.out.println(c.apply(5, 10));
    }
}