package config;

import KYC.AadharKYCService;
import KYC.KYCService;
import KYC.PanKYCService;
import dao.CustomerDao;
import dao.CustomerDaoImpl;
import org.springframework.context.annotation.ComponentScan;
import service.CustomerService;
import service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan({
        "dao",
        "service",
        "KYC",
        "UI",
        "config"
})
public class MySpringConfiguration {
//    @Bean
//    public CustomerDao customerDao() {
//        return new CustomerDaoImpl();
//    }
//    @Bean("aadhaar")
//    public KYCService aadhaarKycService() {
//        return new AadharKYCService();
//    }
//    @Bean("pan")
//    public KYCService panKycService() {
//        return new PanKYCService();
//    }
//    @Bean
//    public CustomerService customerService(
//            CustomerDao customerDao,
//            @Qualifier("aadhaar")
//            KYCService kycService) {
//        return new CustomerServiceImpl(
//                customerDao,
//                kycService
//        );
//    }
//        @Bean
//        public CustomerService customerService(CustomerDao dao,KYCService kyc){
//                return new CustomerServiceImpl(dao,kyc);
//        }
        @Bean
        public Scanner scanner() {
                return new Scanner(System.in);
        }
}