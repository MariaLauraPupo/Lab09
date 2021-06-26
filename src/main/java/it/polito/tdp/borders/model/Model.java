package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

import it.polito.tdp.borders.db.BordersDAO;


public class Model {
	
	private Graph<Country,DefaultEdge> grafo;
	private BordersDAO dao;
    private Map<Integer,Country> idMap;

	public Model() {
		
		dao = new BordersDAO();
		idMap = new HashMap<Integer,Country>();
		//riempio l'idMap
		dao.loadAllCountries(idMap);

	}
	
	public void creaGrafo(int x) {
		grafo = new Multigraph<>(DefaultEdge.class);
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getVertici(x, idMap));
		//aggiungo gli archi
		for(Border b : dao.getBorders(x, idMap)) {
			
			DefaultEdge e = this.grafo.getEdge(b.getC1(),b.getC2());
			if (e==null) {
				Graphs.addEdgeWithVertices(grafo, b.getC1(), b.getC2());
			}
		}
		System.out.println("numero Vertici: " +grafo.vertexSet().size());
		System.out.println("numero Archi: " +grafo.edgeSet().size());

	}

	public Set<Country> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public int getGrado(Country c){
	
		 return this.grafo.degreeOf(c);
		
	}
	

}
