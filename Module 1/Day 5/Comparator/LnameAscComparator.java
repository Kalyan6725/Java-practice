import java.util.Comparator;

class LnameAscComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getLname().compareTo(p2.getLname()); 
    }
}