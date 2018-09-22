public class Edge {
    Actor actor1;
    Actor actor2;
    Movie movie;
    int weight;
    boolean delete_or_not;
    Edge(Actor actor1, Actor actor2, Movie movie){
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.movie = movie;
        weight = 2015 - movie.year+1;
        delete_or_not = false;

    }
}