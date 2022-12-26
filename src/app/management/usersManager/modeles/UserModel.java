package app.management.usersManager.modeles;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UserModel extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Vector<Object[]> rows = new Vector<>();
    private final List<User> users = new ArrayList<>();
    protected String [] columns = {"Id", "Login", "Password", "Admin" };

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return rows.size() ;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object [] obj = rows.get(rowIndex);
        return obj[columnIndex];
    }

    public int getRow(int id){
        for(int i = 0 ; i < users.size() ; i++ ){
            if (users.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    public void setUsers(List<User> userslist) {
        clear ();
        users.addAll(userslist);

        for (User user : users ) {
            rows.add( new Object [] {
                    user.getId(),
                    user.getLogin(),
                    user.getPassword(),
                    user instanceof Admin ? "admin" : "user"
            });
        }
        fireTableDataChanged();
    }

    public void clear () {
        rows.clear();
        users.clear();

        fireTableDataChanged();
    }


}
