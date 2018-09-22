import java.util.ArrayList;


public class Actor implements Comparable<Actor>{
    String name;
    Boolean visited;
    int dist;
    int index;
    int early;
    Actor prev;
    Movie prevm;
    ArrayList<Edge> edges;

    public Actor(String name, int index, int early){
        this.index = index;
        this.early = early;
        this.name = name;
        this.visited = false;
        this.edges = new ArrayList<>();
        dist = Integer.MAX_VALUE;
        prev = null;
        prevm = null;
    }

    @Override
    public int compareTo(Actor o) {
        return this.dist - o.dist;
    }

}

