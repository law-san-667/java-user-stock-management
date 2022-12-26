package app.management.usersManager.ui;

import app.management.usersManager.modeles.Admin;
import app.management.usersManager.modeles.User;
import app.management.usersManager.modeles.UserModel;
import dataBaseManagement.classes.UsersImp;
import dataBaseManagement.interfaces.Dao;
import app.exceptions.BadFormatException;
import app.exceptions.DAOException;
import app.utils.Utilitaire;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIUserManager extends JFrame {
    private JButton searchButton;
    private JTextField searchField;
    private JRadioButton createRadioButton;
    private JPanel contentPane;
    private JButton exitButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton listButton;
    private JButton clearButton;
    private JButton saveButton;
    private JTable table;
    private JScrollPane scrollPane;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField loginField;
    private JRadioButton updateRadioButton;
    private JCheckBox adminCheckBox;
    private JPanel confirmPasswordPanel;
    private JMenuBar menu;
    private JMenu file ;
    private JMenu about ;
    private JMenuItem exit;
    private ButtonGroup btnGroup;
    private JMenuItem aPropos;
    private Dao<User> dataUsers;
    private UserModel userModel ;


    public UIUserManager() throws DAOException {
        //Basic configs
        setTitle("Gestion des Users - version 3.0");
        setSize(new Dimension(600, 350));
        setResizable(false);

        //INITIALISATIONS
        createComponents();
        initComponents();
        setContentPane(contentPane);

        Utilitaire.setLookAndFeel(this);
        Utilitaire.center(this, getSize());
    }

    private void createComponents(){

        exit = new JMenuItem("Exit");
        aPropos = new JMenuItem("A propos");
        menu  = new JMenuBar();
        file = new JMenu("File");
        about = new JMenu("?");
        dataUsers = new UsersImp();

        userModel = new UserModel();
        btnGroup =  new ButtonGroup();

    }

    private void initComponents() throws DAOException {
        file.add(exit);
        about.add(aPropos);
        menu.add(file);
        menu.add(about);
        this.setJMenuBar(menu);

        exit.addActionListener(event ->onExitClicked());

        aPropos.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //JTable
        userModel.setUsers(dataUsers.getList());
        table.setModel(userModel);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(220);
        table.getColumnModel().getColumn(3).setPreferredWidth(280);
        table.setAlignmentX(JLabel.CENTER);
        table.getTableHeader().setAlignmentX(JLabel.CENTER);
        scrollPane.setViewportView(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            try {
                onSelectedRow();
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //Radio buttons
        createRadioButton.addActionListener(e -> onCreateSelected());
        updateRadioButton.addActionListener(e-> {
            try {
                onUpdateSelected();
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateRadioButton.setSelected(true);

        btnGroup.add(createRadioButton);
        btnGroup.add(updateRadioButton);

        if(createRadioButton.isSelected()) {
            saveButton.setVisible(true);
            updateButton.setVisible(false);
        } else if (updateRadioButton.isSelected()) {
            saveButton.setVisible(false);
            updateButton.setVisible(true);
        }

        //Buttons
        updateButton.setVisible(false);

        exitButton.addActionListener(event -> onExitClicked());
        saveButton.addActionListener(event -> onSaveClick());
        updateButton.addActionListener(event -> onUpdateClicked());
        listButton.addActionListener(event -> onListClicked());
        deleteButton.addActionListener(event -> {
            try {
                onDeleteClick();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        });

        clearButton.addActionListener(event -> {
            try {
                onClearClick();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        });
        searchButton.addActionListener(event -> {
            try {
                onGoClicked();
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        });

        //Fields
        if(table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
            onSelectedRow();
        }
    }

    private void onListClicked() {
        try {
            userModel.setUsers(dataUsers.getList());
        }catch (DAOException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onGoClicked() throws DAOException {
        String login = searchField.getText();
        if (login.length() < 1)
            return;

        List<User> listSearch = new ArrayList<>();
        for (User u: dataUsers.getList()) {
            if (Objects.equals(u.getLogin(), login))
                listSearch.add(u);
        }
        if (listSearch.size() > 0)
            userModel.setUsers(listSearch);
        else
            JOptionPane.showMessageDialog(null, "This login does not belong to any user !",
                    "Error", JOptionPane.ERROR_MESSAGE );
        if(table.getRowCount() > 0){
            table.setRowSelectionInterval(0, 0);
            onSelectedRow();
        }

    }

    private void onUpdateClicked() {
        try{
            if (table.getSelectedRow() < 0)
                throw new IllegalAccessException("You must select at least one element !");

            int row = table.getSelectedRow();
            int id = (int) table.getValueAt(row, 0);
            String login = loginField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if(login.length() < 1 || password.length() < 1 )
                throw new NullPointerException("Please fill all the fields");

            User updatedUser;
            if(adminCheckBox.isSelected())
                updatedUser = new Admin(id, login, password);
            else updatedUser = new User(id, login, password);

            for (User p: dataUsers.getList()) {
                if (p.getLogin().equals(login) && p.getId() != id )
                    throw new IllegalArgumentException("This user already exist !");
            }

            dataUsers.update(updatedUser, updatedUser.getId());
            userModel.setUsers(dataUsers.getList());

            int theRow = userModel.getRow(id);

            if(theRow != -1){
                table.setRowSelectionInterval(theRow, theRow);
                onSelectedRow();
            }
            JOptionPane.showMessageDialog(null, "Well Updated !");

        }catch (NullPointerException | IllegalAccessException | IllegalArgumentException | DAOException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void onSelectedRow() throws DAOException {
        if(!updateRadioButton.isSelected()) {
            updateRadioButton.setSelected(true);
            createRadioButton.setSelected(false);
            onUpdateSelected();
        }
            int row = table.getSelectedRow();

            if(row < 0)
                return;
            int id = (int) table.getValueAt(row, 0);

            deleteButton.setEnabled(true);
            updateButton.setEnabled(true);
            User u = dataUsers.getObjectAt(id);


            loginField.setText(String.valueOf(u.getLogin()));
            passwordField.setText(String.valueOf(u.getPassword()));
            adminCheckBox.setSelected(u instanceof Admin);
    }

    private void onExitClicked () {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Exit ?")) {
            this.setVisible(false);
            UIMenuAdmin menu = new UIMenuAdmin();
            menu.setVisible(true);
        }
    }

    private void onSaveClick(){
        try {
            String login = loginField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String passwordConfirm = String.valueOf(confirmPasswordField.getPassword());
            boolean admin;

            if(login.length() < 1 || password.length() < 1 || passwordConfirm.length() < 1)
                throw new NullPointerException("Please fill all the fields !");

            if (!passwordConfirm.equals(password))
                throw new BadFormatException("Passwords have to be the equals !");

            for (User user: dataUsers.getList()) {
                if (user.getLogin().equals(login))
                    throw new IllegalArgumentException("This user already exist !");
            }
            admin = adminCheckBox.isSelected();

            User user;
            if (admin)
                user = new Admin(login, password);
            else
                user = new User(login, password);

            for (User p: dataUsers.getList()) {
                if (p.getId() == user.getId())
                    user.setId(user.getId()+1);
            }

            dataUsers.add(user);
            userModel.setUsers(dataUsers.getList());

            JOptionPane.showMessageDialog(null, "Well Added !");

        }catch(UnsupportedOperationException | ClassCastException | DAOException | IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "Unexpected error : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch (NullPointerException | BadFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        finally {
            resetUsersForm();
            deleteButton.setEnabled(false);
        }
    }

    private void resetUsersForm() {
        loginField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        adminCheckBox.setSelected(false);
    }


    private void onDeleteClick() throws DAOException {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "You really want to delete this user ?")) {
            int row = table.getSelectedRow();
            int id = (int) table.getValueAt(row, 0);
            dataUsers.remove(id);
            User.deleteOneUser();
            userModel.setUsers(dataUsers.getList());

            deleteButton.setEnabled(false);
            updateButton.setEnabled(false);
            resetUsersForm();
            JOptionPane.showMessageDialog(null, "Well Deleted !");
        }
    }

    private void onClearClick() throws DAOException {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "You really want to clear the list ?")){
            userModel.clear();
            User.resetNbUser();
            resetUsersForm();
        }
    }

    private void onCreateSelected(){
        saveButton.setVisible(true);
        updateButton.setVisible(false);
        confirmPasswordPanel.setVisible(true);
        deleteButton.setVisible(false);
        resetUsersForm();
    }

    private void onUpdateSelected() throws DAOException {
        saveButton.setVisible(false);
        updateButton.setVisible(true);
        confirmPasswordPanel.setVisible(false);
        deleteButton.setVisible(true);

        int row = table.getSelectedRow();
        if(table.getRowCount() > 0 && table.getSelectedRow() < 0){
            table.setRowSelectionInterval(0, 0);
            onSelectedRow();
        }else if (row >= 0){
            table.setRowSelectionInterval(row, row);
            onSelectedRow();
        }

    }
}

