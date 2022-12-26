package app.management.usersManager.modeles;

import dataBaseManagement.interfaces.DCheck;
import app.exceptions.DAOException;
import app.exceptions.MissingDataException;
import dataBaseManagement.classes.UsersImp;

public class User implements Comparable<User> {
    private static int nbUsers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String login;
    private String password;


    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public User(String login, String password) {
        setId(++nbUsers);
        setLogin(login);
        setPassword(password);
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public static void deleteOneUser(){
        nbUsers--;
    }
    public static void resetNbUser(){
        nbUsers = 0;
    }

    public static User authentificate(String login, String password) throws MissingDataException, DAOException {

        //Si le mdp ou le login sont null ou vide on throw l'erreur MissingdataException
        if (login == null || login.length() == 0 || password.length() == 0)
            throw new MissingDataException("Veuillez remplir tous les champs !");
        DCheck<User> dataUsers = new UsersImp();
        User user;
        user = dataUsers.readByLoginPassword(login, password);
        return user;
    }

    @Override
    public String  toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return Integer.compare(getId(), o.getId());
    }
}
