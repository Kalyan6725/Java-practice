import java.util.*;
import java.util.stream.Collectors;

class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}

public class StreamToMap {
    public static void main(String[] args) {

        List<Student> students = Arrays.asList(
                new Student(3, "Kalyan"),
                new Student(1, "Ravi"),
                new Student(2, "Arun")
        );

        Map<Integer, String> hashMap =
                students.stream()
                        .collect(Collectors.toMap(
                                Student::getId,
                                Student::getName
                        ));

        Map<Integer, String> linkedHashMap =
                students.stream()
                        .collect(Collectors.toMap(
                                Student::getId,
                                Student::getName,
                                (oldVal, newVal) -> oldVal,
                                LinkedHashMap::new
                        ));

        Map<Integer, String> treeMap =
                students.stream()
                        .collect(Collectors.toMap(
                                Student::getId,
                                Student::getName,
                                (oldVal, newVal) -> oldVal,
                                TreeMap::new
                        ));

        System.out.println("HashMap");
        System.out.println(hashMap);

        System.out.println("\nLinkedHashMap");
        System.out.println(linkedHashMap);

        System.out.println("\nTreeMap");
        System.out.println(treeMap);
    }
}