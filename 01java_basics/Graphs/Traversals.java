import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Traversals{
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
    public static void bfsTraversal(ArrayList<Edge>[] graph,int V){  //visit immediate neighbours first: O(V+E)
        //even works if graph has more than one connected component 
        Queue<Integer> q=new LinkedList<>();
        boolean vis[]=new boolean[V];

        //if has more than one connected components,use this outer loop; else, code from q.add() is suffecient
        for(int i=0;i<V;i++){
            //to search starting nodes of components
            if(vis[i]==false){


                q.add(i);
                while(!q.isEmpty()){
                    int curr=q.remove();
                    if(vis[curr]==false){
                        System.out.print(curr+" ");
                        vis[curr]=true;
        
                        //enqueue all neighbours of current vertex
                        for(int j=0;j<graph[curr].size();j++){
                            Edge e=graph[curr].get(j); 
                            q.add(e.dest);
                        }
                    }
                }


            }
        }
        
        
    }
    public static void dfsTraversal(ArrayList<Edge>[] graph,int curr,boolean[] visited){    //keep going to the very 1st neighbour
        System.out.print(curr+" ");
        visited[curr]=true;

        //call dfs for neighbours
        for(int j=0;j<graph[curr].size();j++){
            Edge e=graph[curr].get(j); 
            if(visited[e.dest]==false){ //if neighbour is not visited
                dfsTraversal(graph,e.dest,visited);
            }
        }

    }
    
    public static void main(String[] args){
        int V=7;
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
        createUnweightedGraph(graph);
        bfsTraversal(graph, V);
        System.out.println();



        //since dfs involves use of recursion,it is better to declare visited array  outside of the recursive function
        boolean[] vis=new boolean[V];
        //if graph has disconnected components
        for(int i=0;i<V;i++){
            if(vis[i]==false){
               dfsTraversal(graph,i,vis); 
            }
        }

        
    }
}