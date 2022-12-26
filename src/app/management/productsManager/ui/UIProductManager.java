package app.management.productsManager.ui;

import java.awt.*;

import java.io.Serial;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import app.management.productsManager.modeles.Produit;
import app.management.productsManager.modeles.ProduitModel;
import app.management.productsManager.modeles.ProduitSolde;
import app.management.usersManager.ui.UIMenuAdmin;
import app.utils.Utilitaire;
import dataBaseManagement.interfaces.Dao;
import app.exceptions.BadFormatException;
import app.exceptions.DAOException;
import app.management.loginManager.UIConnexion;
import dataBaseManagement.classes.ProduitImp;
import app.exceptions.MissingDataException;

import static java.lang.String.valueOf;

public class UIProductManager extends JFrame {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField nomTF;
	private JTextField prixTF;
	private JTextField tauxTF;
	private JPanel tauxPanel;
	private JRadioButton soldeBR;
	private JRadioButton nonSoldeBR;
	private JLabel nombreProduitSolde;
	private JLabel nombreTotalProduit;
	private ProduitModel produitModel;
	private JButton supprimer;
	private JButton modifier;
	public JButton adminExit;
	private Dao<Produit> dataProducts;
	public JButton quitter;

	public UIProductManager() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		try{
			createInstanceComponents();
			initComponents();
		}catch (DAOException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		Utilitaire.setLookAndFeel(this);
		Utilitaire.center(this, getSize());

	}

	private void createInstanceComponents() {
		nombreProduitSolde = new JLabel("0");
		nombreTotalProduit = new JLabel("0");
		table = new JTable();
		nomTF = new JTextField();
		prixTF = new JTextField();
		dataProducts = new ProduitImp();
		
		soldeBR = new JRadioButton("Soldé");
		nonSoldeBR = new JRadioButton("Non Soldé");
		nonSoldeBR.setSelected(true);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(soldeBR);
		buttonGroup.add(nonSoldeBR);
		
		tauxPanel = new JPanel();
		tauxPanel.setVisible(false);
		tauxTF = new JTextField();
		produitModel = new ProduitModel();

		adminExit = new JButton();
		modifier = new JButton("Modifier") ;
		supprimer = new JButton("Supprimer");
	}

	private void initComponents () throws DAOException {
		setTitle("Gestion du stock de produits - version 3.0");
		setSize(new Dimension(569, 300));
		setResizable(false);

		JPanel southPanel = new JPanel();
		southPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout fl_southPanel = (FlowLayout) southPanel.getLayout();
		fl_southPanel.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		modifier.addActionListener(addActionEvent -> onEditClicked());
		modifier.setPreferredSize(new Dimension(75, 23));
		southPanel.add(modifier);

		supprimer.addActionListener(arg0 -> onDeleteClicked ());
		supprimer.setPreferredSize(new Dimension(82, 23));
		supprimer.setEnabled(false);
		southPanel.add(supprimer);

		JButton ajouter = new JButton("Ajouter");
		ajouter.addActionListener(addActionEvent -> onAjouterClicked());
		ajouter.setPreferredSize(new Dimension(75, 23));
		southPanel.add(ajouter);

		JButton clear = new JButton("Clear");
		clear.addActionListener(clearActionEvent -> onClearClicked());
		clear.setPreferredSize(new Dimension(75, 23));
		southPanel.add(clear);
		
		quitter = new JButton("Quitter");
		quitter.addActionListener(quitActionEvent -> onQuitterClicked ());
		quitter.setPreferredSize(new Dimension(75, 23));
		southPanel.add(quitter);

		adminExit.setText("Exit");
		adminExit.addActionListener(addActionEvent -> onAdminExit());
		adminExit.setPreferredSize(new Dimension(95, 23));
		southPanel.add(adminExit);
		adminExit.setVisible(false);


		JPanel eastPanel = new JPanel();
		getContentPane().add(eastPanel, BorderLayout.EAST);
		eastPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel centerEastPanel = new JPanel();
		eastPanel.add(centerEastPanel, BorderLayout.CENTER);
		centerEastPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel northCenterEastPanel = new JPanel();
		centerEastPanel.add(northCenterEastPanel, BorderLayout.NORTH);
		northCenterEastPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel nombreSoldePanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) nombreSoldePanel.getLayout();
		flowLayout_1.setVgap(1);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		northCenterEastPanel.add(nombreSoldePanel);
		
		JLabel nombreSoldeLabel = new JLabel("Nombre de produits sold\u00E9s ajout\u00E9s : ");
		nombreSoldePanel.add(nombreSoldeLabel);
				
		nombreProduitSolde.setForeground(Color.BLUE);
		nombreSoldePanel.add(nombreProduitSolde);
				
		JPanel nombreTotalPanel = new JPanel ();
		FlowLayout fl_nombreTotalPanel = (FlowLayout) nombreTotalPanel .getLayout();
		fl_nombreTotalPanel.setVgap(1);
		fl_nombreTotalPanel.setAlignment(FlowLayout.LEFT);
		northCenterEastPanel.add(nombreTotalPanel);
		
		JLabel nombreTotalLabel = new JLabel("Nombre total de produits ajoutés : ");
		nombreTotalPanel.add(nombreTotalLabel);
		
		nombreTotalProduit.setForeground(Color.BLUE);
		nombreTotalPanel.add(nombreTotalProduit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(325, 150));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		eastPanel.add(scrollPane, BorderLayout.NORTH);

		produitModel.setProduits(dataProducts.getList());
		nombreProduitSolde.setText(String.valueOf(getNbProduitssolde()));
		nombreTotalProduit.setText(String.valueOf(getNbProduits()));
		table.setModel(produitModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(73);
		table.getColumnModel().getColumn(1).setPreferredWidth(240);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(300);
		scrollPane.setViewportView(table);

		table.getSelectionModel().addListSelectionListener(e -> {
			try {
				onSelectedRow();
			} catch (DAOException ex) {
				throw new RuntimeException(ex);
			}
		});

		//Fields
		if(table.getRowCount() > 0){
			table.setRowSelectionInterval(0, 0);
			onSelectedRow();
		}


		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel northCenterPanel = new JPanel();
		northCenterPanel.setBorder(new TitledBorder(null, "D\u00E9tails sur le produit ...", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		centerPanel.add(northCenterPanel, BorderLayout.NORTH);
		northCenterPanel.setLayout(new GridLayout(4, 1, 0, 0));
		
		JPanel nomPanel = new JPanel();
		FlowLayout fl_nomPanel = (FlowLayout) nomPanel.getLayout();
		fl_nomPanel.setAlignment(FlowLayout.RIGHT);
		fl_nomPanel.setVgap(2);
		northCenterPanel.add(nomPanel);
		
		JLabel nomLabel = new JLabel("Nom : ");
		nomPanel.add(nomLabel);
				
		nomPanel.add(nomTF);
		nomTF.setColumns(10);
		
		JPanel prixPanel = new JPanel();
		FlowLayout fl_prixPanel = (FlowLayout) prixPanel.getLayout();
		fl_prixPanel.setAlignment(FlowLayout.RIGHT);
		fl_prixPanel.setVgap(2);
		northCenterPanel.add(prixPanel);
		
		JLabel prixLabel = new JLabel("Prix : ");
		prixPanel.add(prixLabel);
				
		prixPanel.add(prixTF);
		prixTF.setColumns(10);
		
		JPanel soldePanel = new JPanel();
		FlowLayout fl_soldePanel = (FlowLayout) soldePanel.getLayout();
		fl_soldePanel.setVgap(2);
		northCenterPanel.add(soldePanel);
				
		soldePanel.add(soldeBR);
		soldeBR.addChangeListener(changeEvent -> onChangeClicked());
		soldePanel.add(nonSoldeBR);
		
		FlowLayout fl_tauxPanel = (FlowLayout) tauxPanel.getLayout();
		fl_tauxPanel.setAlignment(FlowLayout.RIGHT);
		fl_tauxPanel.setVgap(2);
		northCenterPanel.add(tauxPanel);
		
		JLabel tauxLabel = new JLabel("Taux : ");
		tauxPanel.add(tauxLabel);
		
		tauxPanel.add(tauxTF);
		tauxTF.setColumns(10);
	}

	/* METHODS LISTENERS */


	//ON ROW SELECTED
	private void onSelectedRow() throws DAOException {
		if(table.getSelectedRow() == -1) return;

		try{
			supprimer.setEnabled(true);
			modifier.setEnabled(true);
			int row = table.getSelectedRow();
			int id = (int) produitModel.getValueAt(row, 0);
			Produit produit = dataProducts.getObjectAt(id);
			String nom = produit.getNom();
			String prix;
			if (produit instanceof ProduitSolde) {
				prix = String.valueOf(((ProduitSolde) produit).getPrixNoSolde());
				nonSoldeBR.setSelected(false);
				soldeBR.setSelected(true);
				tauxPanel.setVisible(true);
				String taux = String.valueOf((((ProduitSolde) produit).getSolde()));
				tauxTF.setText(taux);
			} else {
				prix = String.valueOf(produit.getPrix());
				nonSoldeBR.setSelected(true);
				soldeBR.setSelected(false);
				tauxPanel.setVisible(false);
				tauxTF.setText("");
			}
			nomTF.setText(nom);
			prixTF.setText(prix);
		}catch (DAOException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error : " ,JOptionPane.ERROR_MESSAGE);
		}
	}


	//ON CLICK ON EDIT BTN
	private void onEditClicked() {
		try{
			if (table.getSelectedRow() < 0)
				throw new IllegalAccessException("Il faut qu'un élément soit au préalable sélectionné !");

			String nom = nomTF.getText();
			String prix = prixTF.getText();

			if(nom.length() < 1 || prix.length() < 1)
				throw new MissingDataException("Veuillez remplir tous les chapmps !");

			double prixDouble = Double.parseDouble(prix);
			Produit produit;

			int id = (int) produitModel.getValueAt(table.getSelectedRow(), 0);

			if (soldeBR.isSelected()) {
				String taux = tauxTF.getText();
				if(taux.length() < 1)
					throw new MissingDataException("Veuillez remplir tous les chapmps !");
				int tauxInt = Integer.parseInt(taux);
				produit = new ProduitSolde(nom, prixDouble, tauxInt);

				produit.setId(id);
			} else {
				produit = new Produit(nom, prixDouble);
				produit.setId(id);
			}

			if(JOptionPane.showConfirmDialog(null, "Voulez vous confirmer les modifications ?") == JOptionPane.YES_OPTION){
				dataProducts.update(produit, id);
				produitModel.setProduits(dataProducts.getList());
				JOptionPane.showMessageDialog(null, "Modification réussie !");
				table.setRowSelectionInterval(0, 0);
				onSelectedRow();
			}else{
				if(table.getRowCount() > 0){
					table.setRowSelectionInterval(0, 0);
					onSelectedRow();
				}
			}
		} catch (DAOException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Dao Error : ", JOptionPane.ERROR_MESSAGE);
		}catch (NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Rappel: le prix et le solde sont des nombres...", "Error", JOptionPane.ERROR_MESSAGE);
		}catch(UnsupportedOperationException | ClassCastException | IllegalAccessException | MissingDataException | IllegalArgumentException e){
			JOptionPane.showMessageDialog(null,  e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			resetProductDetailsForm();
			nombreTotalProduit.setText(valueOf(getNbProduits()));
			nombreProduitSolde.setText(valueOf(getNbProduitssolde()));
		}
	}

	//ON CLICK ON DELETE BTN
	private void onDeleteClicked(){
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Voulez vous vraiment supprimer cet élément ?")) {
			try{
				if (table.getValueAt(table.getSelectedRow(), 3) == "Yes") {
					ProduitSolde.deleteOneProduit();
				}
				int id = (int) produitModel.getValueAt(table.getSelectedRow(), 0);

				dataProducts.remove(id);

				produitModel.setProduits(dataProducts.getList());
				nombreTotalProduit.setText(valueOf(getNbProduits()));
				nombreProduitSolde.setText(valueOf(getNbProduitssolde()));

				if(table.getRowCount() > 0){
					table.setRowSelectionInterval(0, 0);
					onSelectedRow();
				}else {
					supprimer.setEnabled(false);
					modifier.setEnabled(false);
					resetProductDetailsForm();
				}

				JOptionPane.showMessageDialog(null, "Suppression réussie !");
			}catch(DAOException e){
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error : ", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	//ON CLICK ON AJOUTER BTN
	private void onAjouterClicked () {
		try {
			String nom = nomTF.getText();
			String prix = prixTF.getText();

			if(nom.length() < 1 || prix.length() < 1)
				throw new MissingDataException("Veuillez remplir tous les champs !");

			double prixDouble = Double.parseDouble(prix);

			if (prixDouble < 0)
				throw new BadFormatException("Bien tenté mais le prix doit etre un nombre POSITIF !");

			Produit produit;

			if (soldeBR.isSelected()) {
				String taux = tauxTF.getText();
				if(taux.length() < 1)
					throw new MissingDataException("Veuillez bien remplir tous les champs !");

				int tauxInt = Integer.parseInt(taux);

				produit = new ProduitSolde(nom, prixDouble, tauxInt);
				nombreProduitSolde.setText(valueOf(getNbProduitssolde()));
			} else {
				produit = new Produit(nom, prixDouble);
			}

			for (Produit p: dataProducts.getList()) {
				if (p.getId() == p.getId())
					produit.setId(produit.getId()+1);
			}

			dataProducts.add(produit);
			produitModel.setProduits(dataProducts.getList());
			JOptionPane.showMessageDialog(null, "Ajout réussi !");
			table.setRowSelectionInterval(0, 0);
			onSelectedRow();
		}catch (MissingDataException | BadFormatException | DAOException | NumberFormatException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error :" , JOptionPane.ERROR_MESSAGE);
		} catch(UnsupportedOperationException | ClassCastException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
		}finally {
			nombreTotalProduit.setText(valueOf(getNbProduits()));
			nombreProduitSolde.setText(valueOf(getNbProduitssolde()));
			resetProductDetailsForm();
		}
	}

	private void onChangeClicked () {
		tauxPanel.setVisible(soldeBR.isSelected());
	}
	
	private void resetProductDetailsForm () {
		this.nomTF.setText(null);
		this.prixTF.setText(null);
		this.tauxTF.setText(null);		
	}

	private void onClearClicked(){
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Voulez vous vraiment réinitialiser la liste ?", "?", JOptionPane.YES_NO_OPTION)) {
				this.produitModel.clear();

				resetProductDetailsForm();
				Produit.resetNbProduit();
				ProduitSolde.resetNbProduit();
				modifier.setEnabled(false);
				supprimer.setEnabled(false);
		}
	}

	private void onQuitterClicked () {
		if (JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter ?", "?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			this.dispose();

			UIConnexion connexcion = new UIConnexion();
			connexcion.setVisible(true);
		}
	}

	private void onAdminExit(){
		this.dispose();

		UIMenuAdmin menu = new UIMenuAdmin();
		menu.setVisible(true);
	}

	private int getNbProduits(){
		try{
			return dataProducts.getList().size();
		}catch (DAOException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error : ", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}

	private int getNbProduitssolde(){
		int i = 0;
		try{
			for (Produit produit : dataProducts.getList()) {
				if (produit instanceof ProduitSolde)
					i++;
				}
		}catch (DAOException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error : ", JOptionPane.ERROR_MESSAGE);
		}
		return i;
	}
}