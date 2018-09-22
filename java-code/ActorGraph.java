

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ActorGraph {
    HashMap<String,Movie> moviemap;
    HashMap<String,Actor> actormap;
    ArrayList<Movie> movieList;
    int[] tree;

    public ActorGraph(){
        movieList = new ArrayList<>();
        moviemap = new HashMap<>();
        actormap = new HashMap<>();
        tree =  new int[15000];
    }

    HashMap<String, Movie> getMovieMap(){
        return moviemap;
    }

    HashMap<String, Actor> getActorMap(){
        return actormap;
    }

    public int find (Actor actor) {
        int temp = actor.index;
        if(tree[temp]==0) return temp;
        while(tree[temp]!=0)
            temp = tree[temp];
        tree[actor.index] = temp;
        return temp;
    }

    private void merge(Actor actor1, Actor actor2) {
        int node1 = find(actor1);
        int node2 = find(actor2);
        tree[node1] = node2;
    }

    public int load_Movie(int p) {
        //find the previous year.
        int preyear = movieList.get(p).year;
        //loop through the whole moviemap-sorted
        for(; p < movieList.size(); p++)
        {
            //don't allow the reading out of the file
            if(movieList.get(p).year != preyear)
            {
                return p;
            }
            for(int i = 0; i < movieList.get(p).adj.size()-1; i++)
            {
                if(tree[movieList.get(p).adj.get(i).index] == 0 && tree[movieList.get(p).adj.get(i+1).index] ==0){
                    merge(movieList.get(p).adj.get(i), movieList.get(p).adj.get(i+1));
                }
                else if(find(movieList.get(p).adj.get(i)) != find(movieList.get(p).adj.get(i+1))){
                    merge(movieList.get(p).adj.get(i), movieList.get(p).adj.get(i+1));
                }
            }
        }
        return p;
    }

    boolean loadFromFile(String movieFile)
    {
        // Initialize the file stream
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(movieFile));
            String s;
            //Object cur_act=null,cur_mov=null;

            bufferedReader.readLine();
            while((s=bufferedReader.readLine())!=null)
            {

                String[] data = s.split("\t");

                String actor_name = data[0];
                String movie_title = data[1];
                int movie_year = Integer.parseInt(data[2]);

                String key = movie_title+movie_year;

                //Create a new ActorNode if not exist
                if (!actormap.containsKey(actor_name))
                {
                    Actor newactor= new Actor(actor_name,0,movie_year);
                    actormap.put(actor_name, newactor);
                    //cur_act = got.next();
                }
                else if(actormap.get(actor_name).early > movie_year)
                {
                    actormap.get(actor_name).early=movie_year;
                }

                //Create a new MovieNode if not exist
                if(!moviemap.containsKey(key))
                {
                    Movie tempmovie = new Movie(movie_title, movie_year);
                    tempmovie.adj.add(actormap.get(actor_name));
                    moviemap.put(key, tempmovie);
                    //cur_mov = got2.next();
                }
                else
                {
                    Movie m = moviemap.get(key);
                    Actor a = actormap.get(actor_name);
                    m.adj.add(a);
                    //TODO: a will connect to itself;
                    for(int i = 0;i<m.adj.size()-1;i++) {
                        Edge edge = new Edge(a,m.adj.get(i),m);
                        m.adj.get(i).edges.add(edge);
                        a.edges.add(edge);
                    }
                }
            }

            for(String key: moviemap.keySet()){
                movieList.add(moviemap.get(key));
            }
            Collections.sort(movieList);


        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    boolean load(String movieFile) {
        int index = 0;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(movieFile));//"/Users/hhb/Desktop/movie_casts.tsv"
            String s;
            //Object cur_act=null,cur_mov=null;

            bufferedReader.readLine();
            while ((s = bufferedReader.readLine()) != null) {

                String[] data = s.split("\t");

                String actor_name = data[0];
                String movie_title = data[1];
                int movie_year = Integer.parseInt(data[2]);

                String key = movie_title + movie_year;

                if (!actormap.containsKey(actor_name)) {
                    Actor newactor = new Actor(actor_name, index++, movie_year);
                    actormap.put(actor_name, newactor);
                }
                //Create a new MovieNode if not exist
                if (!moviemap.containsKey(key)) {
                    Movie tempmovie = new Movie(movie_title, movie_year);
                    tempmovie.adj.add(actormap.get(actor_name));
                    moviemap.put(key, tempmovie);
                    //cur_mov = got2.next();
                } else {
                    Movie m = moviemap.get(key);
                    Actor a = actormap.get(actor_name);
                    m.adj.add(a);
                }
            }

            for (String k : moviemap.keySet()) {
                movieList.add(moviemap.get(k));
            }
            Collections.sort(movieList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }

}

