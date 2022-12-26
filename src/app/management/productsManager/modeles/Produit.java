package app.management.productsManager.modeles;

public class Produit implements Comparable<Produit>  {
	protected int id;
	protected String nom;
	protected double prix;
	private static int nbProduit;

	public Produit(int id, String nom, double prix) {
		this (nom, prix);
		this.id = id;
	}

	public Produit(String nom, double prix) {
		this.nom = nom;
		this.setPrix(prix);
		setId(++nbProduit);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		if (prix >= 0) this.prix = prix;
	}
	
	public static void resetNbProduit(){
		nbProduit = 0;
	}

	@Override
	public String toString() {
		return "Produit{" +
				"id=" + id +
				", nom='" + nom + '\'' +
				", prix=" + prix +
				'}';
	}

	@Override
	public int compareTo(Produit o) {
		return Integer.compare(getId(), o.getId());
	}
}