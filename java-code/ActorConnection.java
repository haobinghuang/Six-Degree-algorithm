import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;


public class ActorConnection {
    static boolean  Connection_BFS(String actor1, String actor2, ActorGraph graph, int year) {
        Queue<Actor> que = new LinkedList<>();
        Actor curr;
        Map<String, Actor> actor_map = graph.getActorMap();
        //first actor
        Actor start = actor_map.get(actor1);
        //second actor
        Actor dest = actor_map.get(actor2);
        //reset all nodes in actormap
        for (Actor it : actor_map.values()) {
            it.visited = false;
        }

        start.visited = true;
        //add start to the queue
        que.add(start);
        //loop is the queue still has elements
        while (!(que.isEmpty())) {
            curr = que.peek();
            que.poll();
            //test if we have reach the destination
            if (dest == curr)
                return true;
            for (int i = 0; i < curr.edges.size(); i++) {
                if (curr.edges.get(i).movie.year <= year) {
                    Actor temp_actor = (curr.edges.get(i).actor1 == curr) ? curr.edges.get(i).actor2 : curr.edges.get(i).actor1;
                    //test if the node is not visited.
                    if (!(temp_actor.visited)) {
                        temp_actor.visited = true;
                        //add the temp actor to the queue
                        que.add(temp_actor);
                    }
                }
            }
        }
        if (que.isEmpty()) {
            return false;
        }
        return true;

    }

    public static void main(String[] args) throws IOException {
        ActorGraph graph = new ActorGraph();
        System.out.print("Mode(bfs or ufind):");
        Scanner reader = new Scanner(System.in);
        String mode = reader.next();
        long T1 = System.currentTimeMillis();

        // load data in union-find mode
        if (mode.equals("ufind"))
            graph.load(args[0]);
        // load data in bfs mode
        if (mode.equals("bfs"))
            graph.loadFromFile(args[0]);

        //System.out.println(graph.actormap.size());

        //input file
        BufferedReader buffrd = new BufferedReader(new FileReader(args[1]));//"/Users/hhb/Desktop/test_pairs.tsv"

        //output file
        BufferedWriter buffwr = new BufferedWriter(new FileWriter(args[2]));//"/Users/hhb/Desktop/output_Connection.txt"

        //read from input file
        String s;
        buffrd.readLine();
        while ((s = buffrd.readLine())!=null) {

            //seprate one line to two strings
            String[] record = s.split("\t");
            //System.out.println(record.length);

            //check the size of record
            if (record.length != 2) {
                continue;
            }
            //find the two actors in the actormap
            Actor actor1 = graph.actormap.get(record[0]);
            Actor actor2 = graph.actormap.get(record[1]);

            boolean found = false;

            //BFS
            if (mode.equals("bfs")){
                //find the earlier year for two actors.
                int y = (actor1.early > actor2.early) ? actor2.early : actor1.early;
                int last_year = graph.movieList.get(graph.movieList.size()-1).year;
                while(y <= last_year){
                    //call the BFS2 function
                    if(Connection_BFS(record[0], record[1], graph, y)){
                        found = true;
                        break;
                    }
                    else
                        //increase the earlier year
                        y++;
                }
                if(!found){
                    //append 9999 if not found
                    buffwr.write(record[0] + "\t" + record[1] + '\t' + "9999\n");
                    System.out.println(record[0] + "\t" + record[1] + '\t' + "9999\n");
                }
                else{
                    //append the earliest year if found.
                    buffwr.write(record[0] + "\t" + record[1] + '\t' + y+'\n');
                    System.out.println(record[0] + "\t" + record[1] + '\t' + y);
                }
            }

            int start_point = 0;
            int prev_year = 0;
            //Union Find
            if (mode.equals("ufind")) {
                while (start_point < graph.movieList.size()) {
                    prev_year = graph.movieList.get(start_point).year;
                    start_point = graph.load_Movie(start_point);
                    //if two actors have the same node, then found = true
                    if (graph.find(actor1) == graph.find(actor2)) {
                        found = true;
                        break;
                    }
                }
                //append 9999 if not found
                if (!found) {
                    buffwr.write(record[0] + "\t" + record[1] + '\t' + "9999\n");
                    System.out.println(record[0] + "\t" + record[1] + '\t' + "9999\n");
                }
                //append the earliest year if found.
                else {
                    buffwr.write(record[0] + "\t" + record[1] + '\t' + prev_year + '\n');
                    System.out.println(record[0] + "\t" + record[1] + '\t' + prev_year);
                }
                graph.tree = new int[15000];


            }
        }
        long T2 = System.currentTimeMillis();
        System.out.println("Time(ms):"+ (T2-T1));
        buffwr.close();


    }
}
