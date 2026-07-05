package org.northernarc.loanmanagementproject.config;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataInitializer - Initializes default ADMIN and MANAGER accounts on application startup
 * 
 * This component runs ONCE when the application starts.
 * It creates default ADMIN and MANAGER accounts if they don't already exist.
 * 
 * Workflow:
 * 1. Application starts
 * 2. @PostConstruct method called
 * 3. Check if ADMIN exists
 * 4. If not, create default ADMIN
 * 5. Check if MANAGER exists
 * 6. If not, create default MANAGER
 * 7. Log initialization status
 * 
 * SECURITY NOTE:
 * These are default credentials for initial setup.
 * CHANGE THEM IMMEDIATELY IN PRODUCTION!
 */
@Component
public class DataInitializer {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    /**
     * Initialize default ADMIN and MANAGER accounts
     * Called automatically after all beans are created
     */
    @PostConstruct
    public void init() {
        logger.info("=== Starting Data Initialization ===");
        
        // Create default ADMIN account
        createAdminIfNotExists();
        
        // Create default MANAGER account
        createManagerIfNotExists();
        
        logger.info("=== Data Initialization Complete ===");
    }
    
    /**
     * Create default ADMIN account if it doesn't exist
     * 
     * Default ADMIN credentials:
     * Email: admin@example.com
     * Password: admin@123
     * 
     * ⚠️ IMPORTANT: Change these credentials in production!
     */
    private void createAdminIfNotExists() {
        // Check if ADMIN already exists
        if (customerRepository.existsByRole("ADMIN")) {
            logger.info("✓ ADMIN account already exists - skipping creation");
            return;
        }
        
        try {
            Customer admin = new Customer();
            admin.setCustomerName("System Administrator");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin@123")); // ✅ Encoded with BCrypt
            admin.setBranch("Head Office");
            admin.setRole("ADMIN");
            
            customerRepository.save(admin);
            logger.info("✓ ADMIN account created successfully");
            logger.info("  Email: admin@example.com");
            logger.info("  Password: admin@123");
            logger.warn("  ⚠️  CHANGE PASSWORD IN PRODUCTION!");
            
        } catch (Exception e) {
            logger.error("✗ Failed to create ADMIN account: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create default MANAGER account if it doesn't exist
     * 
     * Default MANAGER credentials:
     * Email: manager@example.com
     * Password: manager@123
     * 
     * ⚠️ IMPORTANT: Change these credentials in production!
     */
    private void createManagerIfNotExists() {
        // Check if MANAGER already exists (email not admin's)
        if (customerRepository.findByRole("MANAGER").size() > 0) {
            logger.info("✓ MANAGER account already exists - skipping creation");
            return;
        }
        
        try {
            Customer manager = new Customer();
            manager.setCustomerName("Branch Manager");
            manager.setEmail("manager@example.com");
            manager.setPassword(passwordEncoder.encode("manager@123")); // ✅ Encoded with BCrypt
            manager.setBranch("Mumbai Branch");
            manager.setRole("MANAGER");
            
            customerRepository.save(manager);
            logger.info("✓ MANAGER account created successfully");
            logger.info("  Email: manager@example.com");
            logger.info("  Password: manager@123");
            logger.warn("  ⚠️  CHANGE PASSWORD IN PRODUCTION!");
            
        } catch (Exception e) {
            logger.error("✗ Failed to create MANAGER account: " + e.getMessage(), e);
        }
    }
}
