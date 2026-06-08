import java.util.Comparator;

class LnameDescComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p2.getLname().compareTo(p1.getLname());
    }
}