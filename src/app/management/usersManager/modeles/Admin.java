package app.management.usersManager.modeles;

public class Admin extends User{
    public Admin(int id, String login, String password) {
        super(id, login, password);
    }

    public Admin(String login, String password) {
        super(login, password);
    }
}
