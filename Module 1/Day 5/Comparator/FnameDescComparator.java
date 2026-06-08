import java.util.Comparator;

class FnameDescComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p2.getFname().compareTo(p1.getFname());
    }
}