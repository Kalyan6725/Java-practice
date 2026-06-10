import java.util.ArrayDeque;
import java.util.Deque;
class MainStack{
    public static void main(String[] args){
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println(stack.pop());
        System.out.println(stack);

        Deque<String> stack1 = new ArrayDeque<>();

        stack1.push("sgs");
        stack1.push("gsdfg");
        stack1.push("rtbf");

        System.out.println(stack1.pop());
        System.out.println(stack1);
    }
}