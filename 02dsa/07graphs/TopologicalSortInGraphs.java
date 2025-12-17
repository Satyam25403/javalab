import java.util.ArrayList;
import java.util.Stack;
 
public class TopologicalSortInGraphs {
    //TOPOLOGICAL SORTING:works for directed acyclic graphs with no cycles
    //it is linear ordering of vertices such that every directed edge u->v, u comes befor v
    //used in dependencies:1.buy laptop 2.install os 3.install code editor 4.install java 5.write code
    //for example 1,2 and then 4 are important for 5

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

        
    
        graph[2].add(new Edge(2,3));
        
        graph[3].add(new Edge(3,0));

        graph[4].add(new Edge(4,0));
        graph[4].add(new Edge(4,1));

        graph[5].add(new Edge(5,0));
        graph[5].add(new Edge(5,2));
       
    }

    //approach: modified dfs::first add neighbours then add the node to the stack
    static void topSortUtil(ArrayList<Edge>[] graph,boolean[] vis,int curr,Stack<Integer> stack){
        vis[curr]=true;

        for(int j=0;j<graph[curr].size();j++){
            Edge e=graph[curr].get(j); 

            if(!vis[e.dest]){ //if neighbour is not visited
                topSortUtil(graph,vis,e.dest,stack);
            }
        }//once we have called dfs for neighbours, on way to returning back, we push back the current node

        stack.push(curr);
    }
    static void topSort(ArrayList<Edge>[] graph,int V){
        boolean[] visited=new boolean[V];
        Stack<Integer> stack=new Stack<>();

        //until all nodes are visited
        for(int i=0;i<V;i++){
            if(!visited[i]){
                topSortUtil(graph, visited, i, stack);
            }
        }
        while(!stack.isEmpty()){
            System.out.print(stack.pop()+" ");
        }
    }
    public static void main(String[] args){
        int V=6;
        /*
         *
         *  1---3
         * /    |\
         * 0    | 5--6
         * \    |/
         *  2---4
         * 
         */
        ArrayList<Edge>[] graph=new ArrayList[V];
        createDirectedGraph(graph);
        topSort(graph, V);
      
        System.out.println();
    }
}
