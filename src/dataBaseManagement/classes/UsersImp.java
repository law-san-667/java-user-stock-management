package dataBaseManagement.classes;

import app.management.usersManager.modeles.Admin;
import dataBaseManagement.interfaces.DCheck;
import dataBaseManagement.interfaces.Dao;
import app.exceptions.DAOException;
import app.management.usersManager.modeles.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersImp implements Dao<User>, DCheck<User> {

    @Override
    public void add(User object) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Insert into T_Users (login, password, status) values (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, object.getLogin());
            ps.setString(2, object.getPassword());
            ps.setString(3, object instanceof Admin ? "admin" : "user");
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void update(User entity, int index) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Update T_Users Set login=?, password=?, status=? Where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getLogin());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity instanceof Admin ? "admin" : "user");
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void remove(int id) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Delete From T_Users Where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<User> getList() throws DAOException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBManager.getConnection()) {
            String query = "Select * From T_Users";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int identifiant = rs.getInt("id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String status = rs.getString(("status"));
                User user;
                if (status.equals("admin"))
                    user = new Admin(identifiant, login, password);
                else
                    user = new User (identifiant, login, password);
                users.add(user);
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        Collections.sort(users);
        return users;
    }

    @Override
    public User getObjectAt(int id) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Select * From T_Users where id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                String login = rs.getString("login");
                String password = rs.getString("password");
                String status = rs.getString(("status"));
                User user;
                if (status.equals("admin"))
                    user = new Admin(id, login, password);
                else
                    user = new User (id, login, password);
                return user;
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Delete From T_Users";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }

    }

    @Override
    public User readByLoginPassword(String login, String password) throws DAOException {
        try (Connection connection = DBManager.getConnection()) {
            String query = "Select * From T_Users where login=? AND password=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                String theLogin = rs.getString("login");
                String thePassword = rs.getString("password");
                int id = rs.getInt("id");
                String status = rs.getString(("status"));
                User user;
                if (status.equals("admin"))
                    user = new Admin(id, theLogin, thePassword);
                else
                    user = new User (id, theLogin, thePassword);
                return user;
            }
        } catch (Exception e) {
            throw new DAOException(e.getMessage());
        }
        return null;
    }
}
