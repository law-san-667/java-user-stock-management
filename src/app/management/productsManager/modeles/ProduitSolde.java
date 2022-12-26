package app.management.productsManager.modeles;

public class ProduitSolde extends Produit {
	
	private int solde;
	private static int nbProduitSolde;

	public ProduitSolde(int id, String nom, double prix, int solde) {
		super(id, nom, prix);
		this.setSolde(solde);

		nbProduitSolde ++;
	}

	public ProduitSolde(String nom, double prix, int solde) {
		super(nom, prix);
		this.setSolde(solde);
		
		nbProduitSolde ++;
	}

	public int getSolde() {
		return solde;
	}

	public void setSolde(int solde) {
		if (solde >= 10 && solde <= 70) this.solde = solde;
		else this.solde = 10;
	}

	public double getPrixNoSolde() {
		return prix;
	}

	@Override
	public double getPrix() {
		return prix * (1 - getSolde() / 100.0);
	}

	public static void deleteOneProduit(){
		nbProduitSolde--;
	}
	public static void resetNbProduit(){
		nbProduitSolde = 0;
	}

	@Override
	public String toString() {
		return "ProduitSolde{" +
				"solde=" + solde +
				", id=" + id +
				", nom='" + nom + '\'' +
				", prix=" + prix +
				'}';
	}
}
	/*
	public ProduitSolde(int id, String nom, double prix, int solde) {
		this(nom, prix, solde);
		this.setId(id);
		this.setSolde(solde);
	}
	*/