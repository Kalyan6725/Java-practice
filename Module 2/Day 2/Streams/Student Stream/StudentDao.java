import java.util.List;

public interface StudentDao {
    void addStudent(Student student);
    List<Student> getAllStudents();
    Student maxMarksMaths();
    Student maxMarksPhy();
    Student maxMarksChe();
    Student topper();
    int avgMarksMaths();
    int avgMarksPhy();
    int avgMarksChe();
    List<Student> aboveAvgPhy();

}