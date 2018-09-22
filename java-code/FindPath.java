import java.io.*;
import java.util.*;

public class FindPath {
    public static void main(String[] args) throws IOException {

        ActorGraph graph = new ActorGraph();

        // call loadFromFile to build the graph
        graph.loadFromFile(args[0]);//"/Users/hhb/Desktop/movie_casts.tsv"
        //System.out.println(graph.actormap.size());
        //System.out.println(graph.movieList.size());


        //input file
        BufferedReader buffrd = new BufferedReader(new FileReader(args[1]));//"/Users/hhb/Desktop/test_pairs.tsv"
        //output file
        BufferedWriter buffwr = new BufferedWriter(new FileWriter(args[2]));//"/Users/hhb/Desktop/output.txt"
        Queue<String> q = new LinkedList<>();
        //boolean have_header = false;
        //read from input file
        String s;
        buffrd.readLine();
        while ((s = buffrd.readLine())!=null) {
            //String s = buffrd.readLine();
            //System.out.println(s);
//            if (!have_header) {
//                have_header = true;
//                continue;
//            }
            //seprate one line to two strings
            String[] record = s.split("\t");
            //System.out.println(record.length);

            //check the size of record
            if (record.length != 2) {
                continue;
            }
            //choose to call BFS or dijkstra
            //String mode = args[2];
//            if(mode.equals('u'))
//                bfs(graph, q, record[0],record[1]);
//            else if(mode.equals('u'))
            //dijkstra(graph, q, record[0], record[1]);
            findPathByBFS(graph,q,record[0],record[1]);
//            else
//                System.out.print("Wrong input argument: second argument must be 'u' or 'w'!\n");
        }
        buffwr.write("(actor)--[movie#@year]-->(actor)--...\n");

        while (!(q.isEmpty())) {
            buffwr.write(q.peek());
            System.out.print(q.peek());
            q.poll();
        }

        buffwr.close();
    }

    public static void dijkstra(ActorGraph graph, Queue output, String actor1, String actor2){
        HashMap<String, Actor> actormap = graph.getActorMap();
        //prority queue used to choose the weight
        PriorityQueue<Pair<Actor,Integer>> pq=new PriorityQueue<>();


        //initiate all the actors stored in actormap
        for(Actor it: actormap.values()){
            it.prev = null;
            it.dist = Integer.MAX_VALUE;
            it.visited = false;
        }

        //find two input actors
        Actor first = actormap.get(actor1);
        Actor second = actormap.get(actor2);
        int temp_weight;
        pq.add(new Pair<>(first, 0));
        first.dist = 0;
        Actor curr;

        //loop through the whole priority queue
        while(!(pq.isEmpty())){
            curr = pq.peek().getFirst();
            pq.poll();
            if(second == curr) break;

            //check if the value in the priority queue visited
            if(!(curr.visited)){
                curr.visited = true;
                for(int i = 0;i<curr.edges.size();i++){
                    Actor temp = (curr.edges.get(i).actor1 == curr)? curr.edges.get(i).actor2 : curr.edges.get(i).actor1;

                    temp_weight = curr.dist + curr.edges.get(i).weight;
                    if(temp_weight < temp.dist){
                        temp.dist = temp_weight;
                        temp.prev = curr;
                        pq.add(new Pair<>(temp, temp_weight));
                    }

                }
            }

        }
        int minweight;
        Stack<String> s = new Stack<>();
        curr = second;
        Actor prio;
        //back trace the graph
        while(curr != null){
            Edge edge = null;
            minweight = Integer.MAX_VALUE;
            s.push(curr.name);
            prio = curr.prev;
            //loop the edge of the node
            for (int i = 0; i < (curr.edges).size(); i++){
                if(curr.edges.get(i).actor1 == prio || curr.edges.get(i).actor2 == prio){
                    if(curr.edges.get(i).weight < minweight){
                        minweight = curr.edges.get(i).weight;
                        edge = curr.edges.get(i);
                    }
                }
            }
            if(edge!=null){
                //push the stack with smovie year and movie title
                s.push(Integer.toString(edge.movie.year));
                s.push(edge.movie.name);
            }
            //update current actor
            curr = curr.prev;
        }
        //print output
        while(!s.empty()){
            output.add("("+s.peek()+")");
            s.pop();
            if(s.empty()) break;
            output.add("--["+s.peek()+"#@");
            s.pop();
            output.add(s.peek()+"]-->");
            s.pop();
        }
        output.add("\n");

    }

    public static void findPathByBFS(ActorGraph graph, Queue<String> output, String actor1, String actor2){
        HashMap<String, Actor> actormap = graph.getActorMap();
        Queue<Actor> actorQueue= new LinkedList<Actor>();

        //initiate all the actors stored in actormap
        for(Actor it: actormap.values()){
            it.prev = null;
            it.dist = Integer.MAX_VALUE;
            it.visited = false;
        }

        //find two input actors
        Actor firstActor = actormap.get(actor1);
        Actor secondActor = actormap.get(actor2);
        firstActor.visited = true;
        firstActor.dist = 0;
        actorQueue.add(firstActor);
        Actor currActor;
        while(!(actorQueue.isEmpty())) {
            currActor = actorQueue.poll();
            if(secondActor == currActor) break;
            for(int i = 0;i<currActor.edges.size();i++){
                Actor tempActor = (currActor.edges.get(i).actor1 == currActor)? currActor.edges.get(i).actor2 : currActor.edges.get(i).actor1;
                if(!tempActor.visited){
                    tempActor.visited = true;
                    tempActor.dist = currActor.dist+1;
                    tempActor.prev = currActor;
                    actorQueue.add(tempActor);
                }
            }
        }

        Stack<String> s = new Stack<String>();
        //back trace the graph
        while(secondActor != null){
            s.push(secondActor.name);
            currActor =secondActor.prev;
            for (int i = 0; i < (secondActor.edges).size(); i++){
                if(secondActor.edges.get(i).actor1 == currActor || secondActor.edges.get(i).actor2 == currActor){
                    s.push(Integer.toString(secondActor.edges.get(i).movie.year));
                    s.push(secondActor.edges.get(i).movie.name);
                    break;
                }
            }
            secondActor= secondActor.prev;
        }

        //print output
        while(!s.empty()){
            output.add("("+s.peek()+")");
            s.pop();
            if(s.empty()) break;
            output.add("--["+s.peek()+"#@");
            s.pop();
            output.add(s.peek()+"]-->");
            s.pop();
        }
        output.add("\n");
    }
}
