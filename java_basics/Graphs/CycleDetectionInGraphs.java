import java.util.ArrayList;

public class CycleDetectionInGraphs {
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
    public static void createDirectedGraph(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,2));

        graph[1].add(new Edge(1,0));
    
        graph[2].add(new Edge(2,3));
        
        graph[3].add(new Edge(3,0));   
    }
    public static void createUnDirectedGraph(ArrayList<Edge> graph[]){
        //firstly theree are all null values stored in graph: we need to make those empty spaces 
        //as adding values directly to null values will produce an error
        for(int i=0;i<graph.length;i++){
            graph[i]=new ArrayList<Edge>();
        }
        //if this was not there, null pointer exception will be thrown

        graph[0].add(new Edge(0,1));
        graph[0].add(new Edge(0,4));

        graph[1].add(new Edge(1,0));
        graph[1].add(new Edge(1,2));
        // graph[1].add(new Edge(1,4));
    
        graph[2].add(new Edge(2,1));
        graph[2].add(new Edge(2,3));
        
        graph[3].add(new Edge(3,2));

        graph[4].add(new Edge(4,0));
        // graph[4].add(new Edge(4,1));
        graph[4].add(new Edge(4,5));

        graph[5].add(new Edge(5,4));
       
    }
    static boolean cycleInUndirectedGraph(ArrayList<Edge>[] graph,boolean[] vis,int curr,int par){
        //a node which is not parent of current node(i.e we have not come to this cuurent node from that node) but is already visited
        vis[curr]=true;
        for(int i=0;i<graph[curr].size();i++){
            Edge e=graph[curr].get(i);
            if(vis[e.dest]==true && e.dest!=par){
                return true;
            }
            else if(!vis[e.dest] && cycleInUndirectedGraph(graph, vis, e.dest, curr)){
                return true;
            }
        }
        return false;
    }
    
    static boolean cycleInDirectedGraph(ArrayList<Edge>[] graph,boolean[] vis,int curr,boolean[] rec){
        //using modified dfs:complexity: O(V+E)
        //undirected graph approach fails in directed graphs when it comes to cycle detection
        //approach:modified dfs(first neighbour first)
        //unlike in case of undirected graphs(using concept of parent node) for cycle detection, we use recursion stack
        //i.e. if we try reaching a node which is already present in recursion stack, a cycle will be detected
        vis[curr]=true;
        rec[curr]=true;
        for(int j=0;j<graph[curr].size();j++){
            Edge e=graph[curr].get(j); 
            if(rec[e.dest]){
                return true;
            }
            else if(!vis[e.dest] && cycleInDirectedGraph(graph, vis, e.dest, rec)){
                return true;
            }
        }
        rec[curr]=false;
        return false;
    }
    
    public static void main(String[] args) {
        int V=4;
        ArrayList<Edge>[] graph=new ArrayList[V];
        createDirectedGraph(graph);
        System.out.println(cycleInDirectedGraph(graph, new boolean[V], 0, new boolean[V]));


        //if disconnected components present use this
        // boolean[] vis=new boolean[V];
        // boolean[] rec=new boolean[V];

        // for(int i=0;i<V;i++){
        //     if(!vis[i]){
        //         boolean isCycle=cycleInDirectedGraph(graph, vis, i, rec);
        //         if(isCycle){
        //             System.out.println(true);
        //             break;
        //         }
        //     }
        // }

        
        int V1=6;
        ArrayList<Edge>[] graph1=new ArrayList[V1];
        createUnDirectedGraph(graph1);
        System.out.println(cycleInUndirectedGraph(graph1, new boolean[V1], 0, -1));
    }
}
