package it.polito.tdp.meteo.bean;

import java.util.List;

public class Citta {

	private String nome;
	private List<Rilevamento> rilevamenti;
	private int counter = 0;
	private int counterBis=0;
	private boolean visited;
	
	public Citta(String nome) {
		this.nome = nome;
		this.visited=false;
	}
	
	public Citta(String nome, List<Rilevamento> rilevamenti) {
		this.nome = nome;
		this.rilevamenti = rilevamenti;
		this.visited=false;
	}

	public String getNome() {
		return nome;
	}
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(Boolean that) {
		this.visited=that;
		return;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Rilevamento> getRilevamenti() {
		return rilevamenti;
	}

	public void setRilevamenti(List<Rilevamento> rilevamenti) {
		this.rilevamenti = rilevamenti;
	}

	public int getCounter() {
		return counter;
	}
	public int getCounterB() {
		return counterBis;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	public void setCounterB(int counter) {
		this.counterBis = counter;
	}
	
	public void increaseCounter() {
		this.counter += 1;
	}
	public void decreaseCounter() {
		this.counter -= 1;
	}
	public void increaseCounterB() {
		this.counterBis += 1;
	}
	public void decreaseCounterB() {
		this.counterBis -=1;
	}
	public void resetCounterB() {
		this.counterBis =0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
	
}
