package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO dao;
	private SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo;
	private List<Driver> vertici;
	private Map<Integer,Driver> idMap;
	private List<Arco> archi;
	private List<Integer> stagioni;
	private Set<Driver> best;
	private int tasso;
	
	public Model() {
		this.dao = new FormulaOneDAO();
		
	}
	
	
	public void creaGrafo(int anno) {
		
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>(this.dao.getAllDriver(anno));
		
		this.idMap = new HashMap<>();
		
		for(Driver d: this.vertici) {
			this.idMap.put(d.getDriverId(), d);
		}
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		this.archi = new ArrayList<>(this.dao.getAllArchi(anno, this.idMap));
		
		for(Arco a: this.archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
		}
		
		
		
	}


	public List<Integer> getStagioni() {
		this.stagioni = new ArrayList<>(this.dao.getSeasons());
		return this.stagioni;
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
		
		
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Driver getMigliorPilota() {
		
		Driver migliore = null;
		int differenza = -100000;
		
		for(Driver d: this.vertici) {
			int nU = this.grafo.outDegreeOf(d);
			int nE = this.grafo.inDegreeOf(d);
			int diff = nU-nE;
			
			if(diff>differenza) {
				differenza = diff;
				migliore = d;
			}
		}
		
		
		return migliore;
	}
	
	
	public void run(int k) {
		
		this.best = new HashSet<>();
		this.tasso = 1000000;
		
		Set<Driver> parziale = new HashSet<>();
		
		ricorsione(parziale,k,0);
		
		
	}


	private void ricorsione(Set<Driver> parziale, int k, int livello) {
		
		
		if(parziale.size()>k) {
			return;
		}
		
		if(parziale.size()==k) {
			int nuovoTasso = calcolaTasso(parziale);
			if(nuovoTasso<this.tasso) {
				this.best = new HashSet<>(parziale);
				this.tasso = nuovoTasso;
				System.out.println(parziale+"  "+tasso);
			}
			return;
		}
		
		if(livello==this.vertici.size()) {
			return;
		}
		
		
		Driver prossimo = this.vertici.get(livello);
		
		parziale.add(prossimo);
		ricorsione(parziale, k, livello+1);
		parziale.remove(prossimo);
		
		
		ricorsione(parziale,k,livello+1);
		
		
	}


	public int getTasso() {
		return this.tasso;
	}

	private int calcolaTasso(Set<Driver> parziale) {
		
		int tas = 0;
		
		List<Driver> piloti = new ArrayList<>(this.vertici);
		
		piloti.removeAll(parziale);
		
		for(Driver p: piloti) {
			for(Driver d: parziale) {
				if(this.grafo.containsEdge(p, d)) {
					tas+= this.grafo.getEdgeWeight(this.grafo.getEdge(p, d));
				}
			}
		}
		
		return tas;
	}


	public Set<Driver> getBest() {
		return best;
	}
	
	

}
