package app.management.loginManager;

import app.management.productsManager.ui.UIProductManager;
import app.management.usersManager.modeles.Admin;
import app.management.usersManager.modeles.User;
import app.management.usersManager.ui.UIMenuAdmin;
import app.utils.Utilitaire;
import app.exceptions.DAOException;
import app.exceptions.MissingDataException;

import javax.swing.*;
import java.awt.*;

public class UIConnexion extends JFrame {
    private JPanel root;
    private JPanel buttonsPanel;
    private JPanel messagePanel;
    private JPanel topPanel;
    private JTabbedPane tabbedPane1;
    private JTextField login_input;
    private JButton btnValidate;
    private JButton btnQuit;
    private JPasswordField password_input;
    private JLabel error_message;
    private JLabel iconLabel;

    public UIConnexion(){
        //Basic settings
        super("Connexion");
        this.setContentPane(root);
        this.setSize(400, 275);
        setResizable(false);
        Utilitaire.setLookAndFeel(this);
        Utilitaire.center(this, getSize());

        initComponents();
    }

    private void onQuitClicked() {
            int answer = JOptionPane.showConfirmDialog(null,"Do you really want to quit ?",
                    "Close", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                dispose();
            }
    }

    private void onAddClicked() {
            try {
                actionOnValidate();
            }catch(MissingDataException x){
                //Display error message
                error_message.setForeground(Color.RED);
                error_message.setText(x.getMessage());

                //Empty fields
                login_input.setText("");
                password_input.setText("");
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
    }

    private void actionOnValidate() throws MissingDataException, DAOException {
        String login = login_input.getText();
        String password = String.valueOf(password_input.getPassword());

        User user = User.authentificate(login, password);

        if (user != null) {
            boolean admin = (user instanceof Admin);

            this.setVisible(false);
            if (admin) {
                UIMenuAdmin menu = new UIMenuAdmin();
                menu.setVisible(true);

            } else {
                UIProductManager uiProducts = new UIProductManager();
                uiProducts.setVisible(true);
            }
        }
        else{
            error_message.setForeground(Color.RED);
            error_message.setText("Vous n'êtes pas authentitfié !");
        }
    }
    private void initComponents(){
        //LOCK IMAGE
        ImageIcon iconLock = new ImageIcon("media/locked.png");
        Image imgLoc = iconLock.getImage();
        Image imgScaledLock = imgLoc.getScaledInstance(150, 140, Image.SCALE_SMOOTH) ;
        ImageIcon newIconLock = new ImageIcon(imgScaledLock);

        //BUTTONS
        btnValidate.addActionListener(e -> onAddClicked());
        btnQuit.addActionListener(e -> onQuitClicked());

        //
        Font font_error = new Font("", Font.PLAIN, 19);
        iconLabel.setIcon(newIconLock);
        error_message.setFont(font_error);


    }
}
