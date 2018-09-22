
public class Pair<F, S> implements Comparable<Pair<Actor,Integer>> {
    private Actor first;
    private Integer second;

    public Pair(Actor first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public Actor getFirst() { return first; }
    public Integer getSecond() { return second; }

    @Override
    public int compareTo(Pair<Actor, Integer> o) {
        return this.getSecond() - (o.getSecond());
    }
}
