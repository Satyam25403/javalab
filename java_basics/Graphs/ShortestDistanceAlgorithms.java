import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShortestDistanceAlgorithms {
    static class Edge{
        int src,dest,weight;
       
        //weighted
        public Edge(int s,int d,int w){
            src=s;
            dest=d;
            weight=w;
        }
    }
    public static void createWeightedGraph(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,1,2));
        graph[0].add(new Edge(0,2,4));

        graph[1].add(new Edge(1,3,17));
        graph[1].add(new Edge(1,2,1));

        graph[2].add(new Edge(2,4,3));

        graph[3].add(new Edge(3,5,1));

        graph[4].add(new Edge(4,3,2));
        graph[4].add(new Edge(4,5,5));
    }
    public static void createWeightedGraphForBellman(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,1,2));
        graph[0].add(new Edge(0,2,4));

        graph[1].add(new Edge(1,2,-4));

        graph[2].add(new Edge(2,3,2));

        graph[3].add(new Edge(3,4,4));

        graph[4].add(new Edge(4,1,-1));//replace this weight with -10 to check for negative weight cycle
    }
    public static class Pair implements Comparable<Pair>{
        //to make priority queue sort and store the pairs on the basis of distance
        int node,dist;
        public Pair(int n,int d){
            node=n;dist=d;
        }
        @Override
        public int compareTo(Pair p2) {
            return this.dist-p2.dist;
        }
    }
    public static void dijkstras(ArrayList<Edge>[] graph,int src,int V){ //O(E{traversing edges}+Elog(v){sorting inside priority queue})
        //only for positive weighted edges
        PriorityQueue<Pair> pq=new PriorityQueue<>();
        int[] dis=new int[V]; 
        for(int i=0;i<V;i++){
            if(i!=src){
                //for all nodes except 0,initialize mindistance array to infinity
                dis[i]=Integer.MAX_VALUE;
            }
        }

        boolean[] vis=new boolean[V];
        pq.add(new Pair(0,0));

        while(!pq.isEmpty()){
            Pair curr=pq.remove();      //this will give the pair having the smallest distance first
            if(!vis[curr.node]){

                vis[curr.node]=true;        //mark this node visited
                //for neighbours of current node relax each neighbour 
                for(int i=0;i<graph[curr.node].size();i++){
                    Edge e=graph[curr.node].get(i);
                    int u=e.src;
                    int v=e.dest;
                    //relax the edge
                    if(dis[u]+e.weight<dis[v]){
                        dis[v]=dis[u]+e.weight;
                        //if relaxed, add to the priority queue the updated distance of the node
                        pq.add(new Pair(v,dis[v]));
                    }
                }
            }
        }
        for(int i=0;i<V;i++){
            System.out.print(dis[i]+" ");
        }
        System.out.println();
    }
    public static void bellManFord(ArrayList<Edge>[] graph,int src,int V){      //O()
        //based on dynamic programming with greater time complexity than dijkstras
        //negative numbers represent lesser cost; here value is given preference over magnitude
        int[] dist=new int[V];
        for(int i=0;i<V;i++){
            if(i!=src){
                dist[i]=Integer.MAX_VALUE;
            }
        }
        //the reason we run outer loop v-1 times is that in any graph,the longest possible route from a node to any other node 
        //passes through v-1 nodes (given v nodes are there in graph)
        for(int k=0;k<V-1;k++){     //O(V)
            //O(E)
            for(int i=0;i<V;i++){   //for nodes
                for(int j=0;j<graph[i].size();j++){     //for neighbours
                    Edge e=graph[i].get(j);
                    int u=e.src;
                    int v=e.dest;
                    if(dist[u]!=Integer.MAX_VALUE && dist[u]+e.weight<dist[v]){
                        dist[v]=dist[u]+e.weight;
                    }

                }
            }
        }
        for(int i=0;i<dist.length;i++){
            System.out.print(dist[i]+" ");
        }
        System.out.println();
        //there exists a case for which even bellman ford algorithm doesnt work: i.e for negative weight cycles
        //neg wt cycle=sum of edges of cycle<0
        //we cannot define shortest distance for the graphs containing negative weight cycles because it doesnt make sense at all
        //more number of times,shortest distance is calculated for the nodes, the more negative, distances will become
        //this can be checked by running the innerloop of code one again as below

        for(int i=0;i<V;i++){  
            for(int j=0;j<graph[i].size();j++){     
                Edge e=graph[i].get(j);
                int u=e.src;
                int v=e.dest;
                if(dist[u]!=Integer.MAX_VALUE && dist[u]+e.weight<dist[v]){
                    //even if updation happens atleast once this code will detect the negative weighted cycle
                    System.out.println("negative weighted cycle present");
                }

            }
        }
    }
    

    public static void main(String[] args) {
        int V=6;
        ArrayList<Edge>[] graph=new ArrayList[V];
        createWeightedGraph(graph);
        dijkstras(graph, 0, V);

        int V1=5;
        ArrayList<Edge>[] graph1=new ArrayList[V];
        createWeightedGraphForBellman(graph1);
        bellManFord(graph1, 0, V1);
    }
}
