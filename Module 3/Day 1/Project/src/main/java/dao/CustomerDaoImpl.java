package dao;

import connection.DBManager;
import entity.Customer;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class CustomerDaoImpl implements CustomerDao{
    Map<Integer,Customer> mpp=new LinkedHashMap<>();
    @Override
    public void addCustomer(Customer customer) throws SQLException {
        Connection conn = DBManager.getConnection();
        String sql="insert into customers(id ,name,pan,aadhar,cibilScore,KYCstatus,kycMethod) values(?,?,?,?,?,?,?)";
        PreparedStatement pmst=conn.prepareStatement(sql);
        pmst.setInt(1,customer.getId());
        pmst.setString(2,customer.getName());
        mpp.put(customer.getId(),customer);
        System.out.println("Customer Added");
    }
    @Override
    public Customer getCustomerById(int id){
        return mpp.get(id);
    }
    @Override
    public void getAllCustomers() {
        System.out.println(mpp);
        for(Map.Entry<Integer,Customer> m:mpp.entrySet()){
            System.out.println(m.getKey()+"==>>"+m.getValue());
        }
    }

    @Override
    public void deleteCustomerById(int id) {
        Customer removed=mpp.remove(id);
        if(removed==null){
            System.out.println("No Customer with id: "+id);
        }
        else{
            System.out.println("Customer deleted: "+removed);
        }
    }

    @Override
    public void UpdateKYCStatus(int id, String KYCStatus) {
        Customer customer=getCustomerById(id);
        if(customer!=null) {
            customer.setKYCStatus(KYCStatus);
            mpp.put(id, customer);
        }
    }
}
