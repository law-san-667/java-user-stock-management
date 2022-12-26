package app.management.usersManager.ui;

import app.management.loginManager.UIConnexion;
import app.management.productsManager.ui.UIProductManager;
import app.utils.Utilitaire;
import app.exceptions.DAOException;

import javax.swing.*;
import java.awt.*;

public class UIMenuAdmin extends JFrame {
    private JPanel root;
    private JButton userButton;
    private JButton productsButton;
    private JButton logOutButton;

    public UIMenuAdmin() {
        setTitle("Admin Menu");
        setSize(new Dimension(220, 210));
        setResizable(false);
        setContentPane(root);

        userButton.addActionListener(e -> displayUserManager());
        Utilitaire.setLookAndFeel(this);
        Utilitaire.center(this, getSize());

        productsButton.addActionListener(e -> displayProductsManager());
        logOutButton.addActionListener(e -> onLogOut());
    }

    private void onLogOut() {
        this.dispose();
        UIConnexion conn =  new UIConnexion();
        conn.setVisible(true);
    }

    private void displayProductsManager() {
        this.setVisible(false);
        UIProductManager prodFrame = new UIProductManager();
        prodFrame.setVisible(true);
        prodFrame.adminExit.setVisible(true);
        prodFrame.quitter.setVisible(false);
    }

    private void displayUserManager() {
        this.setVisible(false);

        try{
            new UIUserManager().setVisible(true);
        }catch (DAOException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error : ", JOptionPane.ERROR_MESSAGE);
        }
    }

}
