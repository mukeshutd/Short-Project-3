package cs6301.g40;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;

import java.util.Iterator;

/**
 * Created by mukku on 9/14/2017.
 */
public class DAG extends GraphAlgorithm  {
	int time;
	int cno;
	int topNum;
	DAG.DAGEdge [][] edgeList;
	
	static class TopVertex {
		int top;
		int finish;
		int dis;
		int cno;
		int inDegree;
		boolean seen;
		Graph.Vertex parent;
		
		TopVertex(Graph.Vertex u, int inDegree) {
			top = 0;
			this.inDegree=inDegree;
		}
		
		TopVertex(Graph.Vertex u, boolean seen) {
			top = 0;
			this.seen=seen;
		}
	}
	
	static class DAGEdge {
		int type;  //1-tree, 2-back, 3-cross
		DAGEdge(Graph.Edge e,int type){
			this.type = type;
		}
	}
	
	DAG(Graph g){
		super(g);
		node = new DAG.TopVertex[g.size()];
		
	}
	
	List<Graph.Vertex> toplogicalOrder2(Graph g) {
		Iterator<Graph.Vertex> it = g.iterator();
		this.topNum = g.size();
		this.time =0;
		this.cno =0;
		
		LinkedList<Graph.Vertex> list = new LinkedList<>();
		
		for (Graph.Vertex v :g
				) {
			node[v.getName()] = new TopVertex(v,false);
		}
		
		while(it.hasNext()){
			Graph.Vertex u = it.next();
			if(!((DAG.TopVertex) node[u.getName()]).seen){
				this.cno++;
				list=	DFSVisit(u,list);
			}
		}
		List<Graph.Vertex> slist = list;
		return slist;
	}
	
	LinkedList<Graph.Vertex> DFSVisit(Graph.Vertex u, LinkedList<Graph.Vertex> list ){
		((DAG.TopVertex) node[u.getName()]).seen = true;
		((DAG.TopVertex) node[u.getName()]).dis = ++this.time;
		((DAG.TopVertex) node[u.getName()]).cno = this.cno;
		
		for (Graph.Edge e: u.adj
				) {
			Graph.Vertex v = e.otherEnd(u);
			if(!((DAG.TopVertex) node[v.getName()]).seen){
				((DAG.TopVertex) node[v.getName()]).parent = u;
				list = DFSVisit(v,list);
			}
		}
		((DAG.TopVertex) node[u.getName()]).finish = ++this.time;
		((DAG.TopVertex) node[u.getName()]).top = this.topNum--;
		list.addFirst(u);
		return list;
	}
	
	boolean isDAG(Graph g) {
		List list = this.toplogicalOrder2(g);
		edgeList = new DAG.DAGEdge[g.size()][g.size()];
		int countEdge=0;
		for (Object v:list
				) {
			System.out.println(((Graph.Vertex) v).getName()+"-> ");
			countEdge += ((DAG.TopVertex)node[((Graph.Vertex) v).getName()]).inDegree;
		}
		System.out.println();
		
		
		for (Graph.Vertex u:g
			 ) {
			for (Graph.Edge e:u.adj
				 ) {
				Graph.Vertex v = e.otherEnd(u);
				int start1 = ((DAG.TopVertex) node[u.getName()]).dis;
				int start2 = ((DAG.TopVertex) node[v.getName()]).dis;
				
				int finsh1 = ((DAG.TopVertex) node[u.getName()]).finish;
				int finsh2 = ((DAG.TopVertex) node[v.getName()]).finish;
				int type=-1;
				//1-tree edge, 2-back edge, 3-cross edge
				if(start1<start2 && finsh1>finsh2)  //1-tree edge
				  type=1;
				else if((start1>start2 && finsh1<finsh2)) //2- back edge
					type=2;
				else type=3;    //3 - cross edge
				
				edgeList[u.getName()][v.getName()]= new DAG.DAGEdge(e,type);
			}
		}
		for (DAG.DAGEdge[] edge:edgeList
			 ) {
			for (DAG.DAGEdge e: edge
				 ) {
				
				if(e!=null && e.type ==2) return false;
			}
		}
		return true;
	}
	
	public static void main(String [] args) throws java.io.FileNotFoundException {
		int evens = 0;
		Scanner in;
		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		Graph g = Graph.readGraph(in,true);
		DAG dag = new DAG(g);
		
		Boolean b = dag.isDAG(g);
		System.out.print(b);
	}
}
	