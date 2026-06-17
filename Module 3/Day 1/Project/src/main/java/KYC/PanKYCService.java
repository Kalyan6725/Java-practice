package KYC;

import entity.Customer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("pan")
@Primary
public class PanKYCService implements KYCService {
    @Override
    public boolean verify(Customer customer) {
        if (customer.getCibilScore() >= 700) {
            if(!(customer.getPan().trim().isEmpty())){
                System.out.println("Pan verification:Good Cibil Score");
                return true;
            }
        }
        System.out.println("Bad cibil/pan not found");
        return false;
    }
}
