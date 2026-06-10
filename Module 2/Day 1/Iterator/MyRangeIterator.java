import java.util.Iterator;

class MyRangeIterator implements Iterator<Integer> {
    private int start;
    private int end;

    public MyRangeIterator(int start,int end){
        this.start=start;
        this.end=end;
    }
    @Override
    public boolean hasNext(){
        return start<=end;
    }

    @Override
    public Integer next(){
        int old=start;
        start+=1;
        return old;
    }
}