package dataBaseManagement.interfaces;

import app.exceptions.DAOException;

public interface DCheck<E> {
    E readByLoginPassword(String login, String password) throws DAOException;
}
