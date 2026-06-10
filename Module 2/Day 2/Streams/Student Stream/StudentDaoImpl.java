import java.util.ArrayList;
import java.util.List;

class StudentDaoImpl implements StudentDao {
    private List<Student> students = new ArrayList<>();

    @Override 
    public void addStudent(Student student){
        students.add(student);
    }

    @Override
    public List<Student> getAllStudents(){
        return new ArrayList<>(students);
    }

    @Override
    public Student maxMarksMaths() {
        return students.stream()
        .max((s1, s2) -> s1.getMATHSmarks()-s2.getMATHSmarks())
        .get();
    }

    @Override
    public Student maxMarksPhy() {
        return students.stream()
        .max((s1, s2) -> Integer.compare(s1.getPHYmarks(), s2.getPHYmarks()))
        .get();
        
    }

    @Override
    public Student maxMarksChe() {
        return students.stream()
        .max((s1, s2) -> Integer.compare(s1.getCHEmarks(), s2.getCHEmarks()))
        .get();
    }

    @Override
    public int avgMarksMaths() {
        //return (int) students.stream().mapToInt(Student::getMATHSmarks).average().orElse(0);
        return students.stream().collect(Collectors.averagingInt(Student::getMATHSmarks)).intValue();
    }

    @Override
    public int avgMarksPhy() {
        //return (int) students.stream().mapToInt(Student::getPHYmarks).average().orElse(0);
        
        int value = students.stream().collect(Collectors.summingInt(Student::getPHYmarks)).intValue();
        return value/students.size();
    }

    @Override
    public int avgMarksChe() {
        //return (int) students.stream().mapToInt(Student::getCHEmarks).average().orElse(0);
        return students.stream().collect(Collectors.averagingInt(Student::getCHEmarks)).intValue();
    }

    @Override
    public List<Student> aboveAvgPhy() {
        int averagePhy = avgMarksPhy();
        return students.stream()
        .filter(student -> student.getPHYmarks() > averagePhy)
        .toList();
    }

    @Override
    public Student topper(){
        return students.stream()
        .max((s1, s2) -> Integer.compare(
            (s1.getPHYmarks() + s1.getCHEmarks() + s1.getMATHSmarks() + s1.getHistoryMarks() + s1.getGeographyMarks()),
            (s2.getPHYmarks() + s2.getCHEmarks() + s2.getMATHSmarks() + s2.getHistoryMarks() + s2.getGeographyMarks())
        ))
        .get();
    }
}