package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.dto.request.AdminCustomerDTO;
import org.northernarc.loanmanagementproject.dto.request.CustomerUpdateRequest;
import org.northernarc.loanmanagementproject.dto.request.RegisterRequest;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer register(RegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered: " + request.getEmail());
        }
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setBranch(request.getBranch());
        customer.setRole("USER");
        customer.setStatus("ACTIVE");
        return customerRepository.save(customer);
    }

    @Override
    public Customer createByAdmin(AdminCustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email already registered: " + dto.getEmail());
        }
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setBranch(dto.getBranch());
        customer.setRole(dto.getRole() != null ? dto.getRole() : "USER");
        customer.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE");
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(CustomerUpdateRequest request, boolean allowRoleStatus) {
        Customer existing = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId().toString()));

        if (StringUtils.hasText(request.getCustomerName())) {
            existing.setCustomerName(request.getCustomerName());
        }
        if (StringUtils.hasText(request.getPhone())) {
            existing.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getAddress())) {
            existing.setAddress(request.getAddress());
        }
        if (StringUtils.hasText(request.getBranch())) {
            existing.setBranch(request.getBranch());
        }
        if (StringUtils.hasText(request.getPassword())) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (allowRoleStatus) {
            if (StringUtils.hasText(request.getRole())) {
                existing.setRole(request.getRole());
            }
            if (StringUtils.hasText(request.getStatus())) {
                existing.setStatus(request.getStatus());
            }
        }
        return customerRepository.save(existing);
    }

    @Override
    public void delete(Long customerId) {
        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        customerRepository.delete(existing);
    }
}
