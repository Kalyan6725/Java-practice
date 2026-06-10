import java.util.List;
import java.util.ArrayList;
class Department {
    private String name;
    private String hqlocation;
    private List<Employee> employees;

    Department(String name, String hqlocation) {
        this.name = name;
        this.hqlocation = hqlocation;
        this.employees = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getHQLocation() {
        return this.hqlocation;
    }

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public void deleteEmployee(int index) {
        System.out.println(employees.remove(index));
    }

    public void getDetails(){
        System.out.println("Dept name:"+name+"\nHQ location :"+hqlocation+"\n");
        System.out.println(employees);
    }
}