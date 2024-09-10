import java.util.ArrayList;

public class AllPathsFromSourceToTarget {
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

        graph[0].add(new Edge(0,1));
        graph[0].add(new Edge(0,2));

        graph[1].add(new Edge(1,0));
        graph[1].add(new Edge(1,3));

        graph[2].add(new Edge(2,0));
        graph[2].add(new Edge(2,4));

        graph[3].add(new Edge(3,1));
        graph[3].add(new Edge(3,4));
        graph[3].add(new Edge(3,5));

        graph[4].add(new Edge(4,2));
        graph[4].add(new Edge(4,3));
        graph[4].add(new Edge(4,5));

        graph[5].add(new Edge(5,3));
        graph[5].add(new Edge(5,4));
        graph[5].add(new Edge(5,6));

        graph[6].add(new Edge(6,5));
    }

    //we dont use visited array to mark the visited vertices because, 
    //there is no rule that one vertex should be visited only once
    //tracing a vertex we mark vertices as visited and coming back, we unmark them so that they can be used in some other path
    public static void allPaths(ArrayList<Edge>[] graph,int curr,boolean[] visited,String path,int target){    //complexity exponential:O(V^V)
        if(curr==target){
            System.out.println(path);
            return;
        }

        //call dfs for neighbours
        for(int j=0;j<graph[curr].size();j++){
            Edge e=graph[curr].get(j); 
            if(!visited[e.dest]){ //if neighbour is not visited
                visited[curr]=true;
                allPaths(graph, e.dest, visited, path+e.dest, target);
                visited[curr]=false;
            }
        }

    }
    public static void main(String[] args) {
        int V=7;
        ArrayList<Edge>[] graph=new ArrayList[V];
        createUnweightedGraph(graph);
        int src=0,tar=5;
        allPaths(graph, src,new boolean[V], "0", tar);
    }
}
