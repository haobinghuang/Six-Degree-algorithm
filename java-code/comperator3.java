import java.util.Comparator;

public class comperator3 implements Comparator<Pair<Actor, Integer>>{
    @Override
    public int compare(Pair<Actor, Integer> o1, Pair<Actor, Integer> o2) {
        return o1.getSecond() - o2.getSecond();
    }
}
