package it.polito.tdp.formulaone.model;

public class Arco {
	
	private Driver d1;
	private Driver d2;
	private Double peso;
	public Arco(Driver d1, Driver d2, Double peso) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.peso = peso;
	}
	public Driver getD1() {
		return d1;
	}
	public void setD1(Driver d1) {
		this.d1 = d1;
	}
	public Driver getD2() {
		return d2;
	}
	public void setD2(Driver d2) {
		this.d2 = d2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
		result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
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
		Arco other = (Arco) obj;
		if (d1 == null) {
			if (other.d1 != null)
				return false;
		} else if (!d1.equals(other.d1))
			return false;
		if (d2 == null) {
			if (other.d2 != null)
				return false;
		} else if (!d2.equals(other.d2))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Arco [d1=" + d1 + ", d2=" + d2 + ", peso=" + peso + "]";
	}
	
	

}
