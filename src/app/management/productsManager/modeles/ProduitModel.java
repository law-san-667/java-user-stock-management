package app.management.productsManager.modeles;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ProduitModel extends AbstractTableModel {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Vector<Object[]> rows = new Vector<>();
	private final List<Produit> produits = new ArrayList<>();
	protected String [] columns = {"Id", "Nom", "Prix (FCFA)", "Solde (yes or No)"};
	
	@Override	
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {		
		Object [] obj = rows.get(rowIndex);
		return obj[columnIndex];
	}

	@Override
	public String getColumnName(int arg0) {
		return columns[arg0];
	}

	public void setProduits(List<Produit> produits) {
		clear ();
		this.produits.addAll(produits);
		
		for (Produit produit : produits ) {
			rows.add( new Object [] {
				produit.getId(),
				produit.getNom(),
				produit.getPrix(),
				produit instanceof ProduitSolde ? "Yes" : "No"
			});
		}
		fireTableDataChanged();
	}
	
	public void clear () {
		rows.clear();
		produits.clear();

		fireTableDataChanged();
	}

}
