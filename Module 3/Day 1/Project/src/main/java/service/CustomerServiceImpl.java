package service;

import KYC.KYCService;
import dao.CustomerDao;
import entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

@Component
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private Map<String, KYCService> kycServices;


    // public CustomerServiceImpl(CustomerDao customerDao,KYCService kycService) {
    //     this.customerDao = customerDao;
    //     this.kycService = kycService;
    // }


    public void onboardCustomer(Customer customer) throws SQLException {
        KYCService kycService = kycServices.get(customer.getKycMethod());
        if (kycService == null) {
            System.out.println("Invalid KYC method: " + customer.getKycMethod());
            return;
        }
        if (kycService.verify(customer)) {
            customer.setKYCStatus("Pending");
            customerDao.addCustomer(customer);
        } else {
            System.out.println("KYC verification failed");
        }
    }

    @Override
    public void getAllCustomers() {
        customerDao.getAllCustomers();
    }

    @Override
    public void updateKYCStatus(int id, String status) {
        customerDao.UpdateKYCStatus(id,status);
    }

    @Override
    public void deleteCustomerById(int id) {
        customerDao.deleteCustomerById(id);
    }
}
