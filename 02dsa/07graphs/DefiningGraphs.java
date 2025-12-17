
import java.util.ArrayList;

class DefiningGraphs{
    static class Edge{
        int src,dest,weight;
        //unweighted
        public Edge(int s,int d){
            src=s;
            dest=d;
        }
        //weighted
        public Edge(int s,int d,int w){
            src=s;
            dest=d;
            weight=w;
        }
    }
    public static void createUnweightedGraph(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,2));

        graph[1].add(new Edge(1,2));
        graph[1].add(new Edge(1,3));

        graph[2].add(new Edge(2,0));
        graph[2].add(new Edge(2,1));
        graph[2].add(new Edge(2,3));

        graph[3].add(new Edge(3,1));
        graph[3].add(new Edge(3,2));
    }
    public static void createWeightedGraph(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,2,2));

        graph[1].add(new Edge(1,2,10));
        graph[1].add(new Edge(1,3,0));

        graph[2].add(new Edge(2,0,2));
        graph[2].add(new Edge(2,1,20));
        graph[2].add(new Edge(2,3,-1));

        graph[3].add(new Edge(3,1,0));
        graph[3].add(new Edge(3,2,-1));
    }
    public static void main(String[] args){
        int V=4;
        ArrayList<Edge>[] graph=new ArrayList[V];
        createWeightedGraph(graph);

        //print 2's neighbours
        for(int i=0;i<graph[2].size();i++){
            Edge e=graph[2].get(i); //get ith list
            System.out.println(e.dest+","+e.weight);
        }
    }
}