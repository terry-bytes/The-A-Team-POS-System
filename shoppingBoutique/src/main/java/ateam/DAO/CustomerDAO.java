package ateam.DAO;


import ateam.Models.Customer;
import java.util.List;

public interface CustomerDAO {

    void addCustomer(Customer customer);

    Customer getCustomerById(int customer_ID);

    List<Customer> getAllCustomers();

    void updateCustomer(Customer customer);

    void deleteCustomer(int customer_ID); 
}
