package KYC;

import entity.Customer;
import org.springframework.stereotype.Component;

@Component("aadhar")
public class AadharKYCService implements KYCService{
    @Override
    public boolean verify(Customer customer){
        if(customer.getCibilScore()>=700 && customer.getAadhaar()!=null && !customer.getAadhaar().trim().isEmpty()){
            System.out.println("AAdhar verification:Good Cibil");
            return true;
        }
        System.out.println("AAdhar:Bad cibil or aadhaar missing");
        return false;
    }
}
