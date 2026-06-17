package service;

import entity.Customer;

import java.sql.SQLException;

public interface CustomerService {
    void onboardCustomer(Customer customer) throws SQLException;
    void getAllCustomers();
    void updateKYCStatus(int id, String status);
    void deleteCustomerById(int id);
}
