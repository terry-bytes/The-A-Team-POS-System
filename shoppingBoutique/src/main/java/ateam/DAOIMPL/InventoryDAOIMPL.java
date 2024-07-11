package ateam.DAOIMPL;

import ateam.Models.Inventory;
import ateam.BDconnection.Connect;
import ateam.DAO.InventoryDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOIMPL implements InventoryDAO {

    private Connection connection;

    public InventoryDAOIMPL() {
        Connect connect = new Connect();
        this.connection = connect.connectToDB();
    }

    @Override
    public boolean addInventory(Inventory inventory, int addedByEmployeeId) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {

            String sqlInsert = "INSERT INTO inventory (product_ID, store_ID, inventory_quantity, reorder_point, added_by_employee_ID) "
                    + "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setInt(1, inventory.getProduct_ID());
            preparedStatement.setInt(2, inventory.getStore_ID());
            preparedStatement.setInt(3, inventory.getInventory_quantity());
            preparedStatement.setInt(4, inventory.getReorder_point());
            preparedStatement.setInt(5, addedByEmployeeId);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(preparedStatement);
        }

        return success;
    }

    @Override
    public Inventory getInventoryById(int inventory_ID) {
        Inventory inventory = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sqlSelect = "SELECT * FROM inventory WHERE inventory_ID = ?";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, inventory_ID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                inventory = new Inventory();
                inventory.setInventory_ID(resultSet.getInt("inventory_ID"));
                inventory.setProduct_ID(resultSet.getInt("product_ID"));
                inventory.setStore_ID(resultSet.getInt("store_ID"));
                inventory.setInventory_quantity(resultSet.getInt("inventory_quantity"));
                inventory.setReorder_point(resultSet.getInt("reorder_point"));
                inventory.setLast_updated(resultSet.getTimestamp("last_updated"));
                inventory.setAdded_by_employee_ID(resultSet.getInt("added_by_employee_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(resultSet, preparedStatement);
        }

        return inventory;
    }

    @Override
    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            statement = connection.createStatement();

            String sqlSelectAll = "SELECT * FROM inventory";
            resultSet = statement.executeQuery(sqlSelectAll);

            while (resultSet.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventory_ID(resultSet.getInt("inventory_ID"));
                inventory.setProduct_ID(resultSet.getInt("product_ID"));
                inventory.setStore_ID(resultSet.getInt("store_ID"));
                inventory.setInventory_quantity(resultSet.getInt("inventory_quantity"));
                inventory.setReorder_point(resultSet.getInt("reorder_point"));
                inventory.setLast_updated(resultSet.getTimestamp("last_updated"));
                inventory.setAdded_by_employee_ID(resultSet.getInt("added_by_employee_ID"));

                inventories.add(inventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(resultSet, statement);
        }

        return inventories;
    }

    @Override
    public boolean updateInventory(Inventory inventory) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {

            String sqlUpdate = "UPDATE inventory SET product_ID = ?, store_ID = ?, inventory_quantity = ?, reorder_point = ?, last_updated = CURRENT_TIMESTAMP WHERE inventory_ID = ?";
            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setInt(1, inventory.getProduct_ID());
            preparedStatement.setInt(2, inventory.getStore_ID());
            preparedStatement.setInt(3, inventory.getInventory_quantity());
            preparedStatement.setInt(4, inventory.getReorder_point());
            preparedStatement.setInt(5, inventory.getInventory_ID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(preparedStatement);
        }

        return success;
    }

    @Override
    public boolean deleteInventory(int inventory_ID) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {

            String sqlDelete = "DELETE FROM inventory WHERE inventory_ID = ?";
            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, inventory_ID);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            close(preparedStatement);
        }

        return success;
    }

    private void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
