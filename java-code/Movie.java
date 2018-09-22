import java.util.ArrayList;

public class Movie implements Comparable<Movie>{
    String key;
    String name;
    int year;
    ArrayList<Actor>  adj;

    public Movie(String name, int year) {
        this.key=name+Integer.toString(year);
        this.name=name;
        this.year=year;
        adj = new ArrayList<>();
    }

    @Override
    public int compareTo(Movie m) {
        return this.year - m.year;
    }
}
