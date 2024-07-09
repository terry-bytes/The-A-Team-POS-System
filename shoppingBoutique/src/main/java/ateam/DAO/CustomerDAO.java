package ateam.DAO;


import ateam.Models.Customer;
import java.util.List;

public interface CustomerDAO {

    boolean addCustomer(Customer customer);

    Customer getCustomerById(int customer_ID);

    List<Customer> getAllCustomers();

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int customer_ID); 
}
