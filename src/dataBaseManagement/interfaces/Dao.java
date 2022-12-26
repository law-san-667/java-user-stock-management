package dataBaseManagement.interfaces;

import app.exceptions.DAOException;

import java.util.List;

public interface Dao<E> {
    void add(E object) throws DAOException;
    void update(E object, int index) throws DAOException;
    void remove(int index) throws DAOException;
    List<E> getList() throws DAOException;
    E getObjectAt(int row) throws DAOException;
    void clear() throws DAOException;
}
