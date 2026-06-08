import java.util.Comparator;

class FnameAscComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getFname().compareTo(p2.getFname()); 
    }
}