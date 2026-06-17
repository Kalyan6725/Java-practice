package dao;
import entity.Customer;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

public interface CustomerDao {
    public void addCustomer(Customer customer) throws SQLException;
    public Customer getCustomerById(int id);
    public void getAllCustomers();
    public void deleteCustomerById(int id);
    public void UpdateKYCStatus(int id,String KYCStatus);
}
