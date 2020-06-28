package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;


public class Model {
	
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	private List<ArtObject> vertici;
	private Map<Integer,ArtObject> idMap;
	private List<Arco> archi;
	private List<ArtObject> best;
	private int pesoTotale;
	private String classificazione;
	
	
	
	
	public Model() {
		dao = new ArtsmiaDAO();
	}
	
	public void creaGrafo() {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>(this.dao.listObjects());
		
		this.idMap = new HashMap<>();
		
		for(ArtObject a: this.vertici) {
			this.idMap.put(a.getId(), a);
		}
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		this.archi = new ArrayList<>(this.dao.listArchi(this.idMap));
		
		for(Arco a: this.archi) {
			
			if(this.grafo.containsEdge(a.getA1(), a.getA2())==false) {
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
			
		}

	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<ArtObject> getVertici() {
		return vertici;
	}

	public Map<Integer, ArtObject> getIdMap() {
		return idMap;
	}
	
	public int determinaComponenteConnessa(ArtObject art) {
		
		ConnectivityInspector<ArtObject,DefaultWeightedEdge> graf = new ConnectivityInspector<>(this.grafo);
		int dimensione = graf.connectedSetOf(art).size();
		
		return dimensione;
		
	}
	
	
	public void trovaPercoso(int sorgente , int max) {
		
		this.best = null;
		this.pesoTotale = 0;
		
		List<ArtObject> parziale  = new ArrayList<>();
		parziale.add(this.idMap.get(sorgente));
		this.classificazione = this.idMap.get(sorgente).getClassification();
		System.out.println(this.classificazione+"\n");
		
		ricorsione(parziale, max);
	}

	public void ricorsione(List<ArtObject> parziale, int max) {
		
		ArtObject ultimo = parziale.get(parziale.size()-1);
		
		if(!ultimo.getClassification().equals(this.classificazione)) {
			return;
		}
		
		if(parziale.size()==max) {
			int peso = calcolaPeso(parziale);
			if(peso>this.pesoTotale) {
				this.pesoTotale=peso;
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		
		
		List<ArtObject> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(ArtObject v: vicini) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				ricorsione(parziale,max);
				parziale.remove(parziale.size()-1);
				
			}
		}
		
		
		
	}

	public int calcolaPeso(List<ArtObject> parziale) {
		
		int peso = 0;
		List<ArtObject> tempP = new ArrayList<>(parziale);
		ArtObject precedente  = tempP.get(0);
		tempP.remove(precedente);
		for(ArtObject p: tempP) {
			peso+= this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, p));
			precedente  = p;
		}
		
		
		return peso;
	}

	public List<ArtObject> getBest() {
		Collections.sort(this.best);
		return best;
	}

	public int getPesoTotale() {
		return pesoTotale;
	}

}
