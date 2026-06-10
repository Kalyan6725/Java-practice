import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
class Map1 {
    public static void main(String[] args) {
        //Map<Integer,String> mpp=new HashMap<>(); //no order
        Map<Integer,String> mpp=new LinkedHashMap<>(); //insertion order
        //Map<Integer,String> mpp=new TreeMap<>(); //sorted order based on keys
        mpp.put(1,"A");
        mpp.put(5,"E");
        mpp.put(3,"C");
        mpp.put(3,"Overriden C");
        mpp.put(4,"D");
        mpp.put(2,"B");
        // If key is null then it will throw NullPointerException only in treemap
        for(Integer key: mpp.keySet()){
            System.out.println(key+":" + mpp.get(key));
        }
        System.out.println("---------Entry Set----------");
        for(Map.Entry<Integer,String> entry: mpp.entrySet()){
            System.out.println(entry.getKey()+":" + entry.getValue());
        }
        mpp.keySet().stream().forEach((key)->System.out.println(key+":"+mpp.get(key)));

        //System.out.println(mpp);
    }
}