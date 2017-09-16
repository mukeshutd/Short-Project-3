package cs6301.g40;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;

import java.util.Iterator;

/**
 * Created by mukku on 9/14/2017.
 */
public class TopologicalOrder extends GraphAlgorithm  {
	int time;
	int cno;
	int topNum;
	
	
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
	
	
	
	TopologicalOrder(Graph g){
	super(g);
	node = new TopologicalOrder.TopVertex[g.size()];
		
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
			if(!((TopologicalOrder.TopVertex) node[u.getName()]).seen){
				this.cno++;
			 list=	DFSVisit(u,list);
			}
		}
		List<Graph.Vertex> slist = list;
		return slist;
	}
	
	LinkedList<Graph.Vertex> DFSVisit(Graph.Vertex u, LinkedList<Graph.Vertex> list ){
		((TopologicalOrder.TopVertex) node[u.getName()]).seen = true;
		((TopologicalOrder.TopVertex) node[u.getName()]).dis = ++this.time;
		((TopologicalOrder.TopVertex) node[u.getName()]).cno = this.cno;
		
		for (Graph.Edge e: u.adj
			 ) {
			Graph.Vertex v = e.otherEnd(u);
			if(!((TopologicalOrder.TopVertex) node[v.getName()]).seen){
				((TopologicalOrder.TopVertex) node[v.getName()]).parent = u;
				list = DFSVisit(v,list);
			}
		}
		((TopologicalOrder.TopVertex) node[u.getName()]).finish = ++this.time;
		((TopologicalOrder.TopVertex) node[u.getName()]).top = this.topNum--;
		list.addFirst(u);
		return list;
	}
	
	List<Graph.Vertex> toplogicalOrder1(Graph g)  {
		Queue<Graph.Vertex> q = new LinkedList<>();
		List<Graph.Vertex> list = new LinkedList<>();
		int topNum =0;
		/*for (Graph.Vertex u:g
				) {
			node[u.getName()]= new TopVertex(u);
		}*/
		
		for (Graph.Vertex v:g
			 ) {
			node[v.getName()] = new TopVertex(v,v.revAdj.size());
			if(v.revAdj.size()==0)
				q.add(v);
		}
		while(!q.isEmpty()){
			Graph.Vertex u = q.remove();
			TopVertex k = (TopologicalOrder.TopVertex) node[u.getName()];
			k.top = ++topNum;
			list.add(u);
			for (Graph.Edge e: u.adj
				 ) {
			 	Graph.Vertex v = e.otherEnd(u);
				((TopologicalOrder.TopVertex)node[v.getName()]).inDegree -=1;
				if(((TopologicalOrder.TopVertex)node[v.getName()]).inDegree==0)
					q.add(v);
				
			}
		}
		if(topNum<g.size()) throw new java.lang.RuntimeException("Not a DAG");
		else return list;
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
		TopologicalOrder TopOrd = new TopologicalOrder(g);
		List list = TopOrd.toplogicalOrder1(g);
		for (Object v:list
			 ) {
			System.out.print(((Graph.Vertex) v).getName());
			//System.out.println(node[v.getName()]));
		}
		
		
	}
}
