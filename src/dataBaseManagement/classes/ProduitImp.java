package dataBaseManagement.classes;

import dataBaseManagement.interfaces.Dao;
import app.exceptions.DAOException;
import app.management.productsManager.modeles.Produit;
import app.management.productsManager.modeles.ProduitSolde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ProduitImp implements Dao<Produit>{
    @Override
    public void add(Produit object) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Insert into T_Products (nom, prix, solde, taux) values (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, object.getNom());
            ps.setDouble(2,(object instanceof ProduitSolde) ? ((ProduitSolde) object).getPrixNoSolde() : object.getPrix());
            ps.setString(3, (object instanceof ProduitSolde)? "solde" : "normal");
            ps.setInt(4, (object instanceof ProduitSolde) ? ((ProduitSolde) object).getSolde() : 0);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Dao : " + e.getMessage());
        }
    }

    @Override
    public void update(Produit object, int index) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Update T_Products Set nom=?, prix=?, solde=?, taux=? Where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, object.getNom());
            ps.setDouble(2, (object instanceof ProduitSolde) ? ((ProduitSolde) object).getPrixNoSolde() : object.getPrix());
            ps.setString(3, (object instanceof ProduitSolde)? "solde" : "normal");
            ps.setInt(4, (object instanceof ProduitSolde) ? ((ProduitSolde) object).getSolde() : 0);
            ps.setInt(5, object.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void remove(int id) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Delete From T_Products Where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Produit> getList() throws DAOException {
        List<Produit> produits = new ArrayList<>();
        try (Connection connection = DBManager.getConnection()) {
            String query = "Select * From T_Products";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int identifiant = rs.getInt("id");
                String nom = rs.getString("nom");
                double prix = rs.getDouble("prix");

                if (Objects.equals(rs.getString("solde"), "solde")){
                    int taux = rs.getInt("taux");
                    ProduitSolde produitSolde = new ProduitSolde(identifiant, nom, prix, taux);
                    produits.add(produitSolde);
                }else{
                    Produit produit = new Produit (identifiant, nom, prix);
                    produits.add(produit);
                }
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        Collections.sort(produits);
        return produits;
    }

    @Override
    public Produit getObjectAt(int id) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Select * From T_Products where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                String nom = rs.getString("nom");
                double prix = rs.getDouble("prix");
                if (Objects.equals(rs.getString("solde"), "solde")) {
                    int taux = rs.getInt("taux");
                    return new ProduitSolde(id, nom, prix, taux);
                }
                return new Produit (id, nom, prix);
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return null;
    }
    @Override
    public void clear() throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Delete From T_Products";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }

    }
}
