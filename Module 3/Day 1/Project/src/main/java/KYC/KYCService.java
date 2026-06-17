package KYC;

import entity.Customer;
import org.springframework.stereotype.Component;

public interface KYCService {
    boolean verify(Customer customer);
}