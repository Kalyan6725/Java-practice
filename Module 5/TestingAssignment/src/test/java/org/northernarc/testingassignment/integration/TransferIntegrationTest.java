package org.northernarc.testingassignment.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.controller.AccountController.*;
import org.northernarc.testingassignment.dto.RegisterRequest;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.entity.Transaction;
import org.northernarc.testingassignment.repository.AccountRepository;
import org.northernarc.testingassignment.repository.CustomerRepository;
import org.northernarc.testingassignment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class TransferIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account sourceAccount;
    private Account destinationAccount;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        transactionRepository.deleteAll();

        // Create customers
        customer1 = new Customer();
        customer1.setName("Customer 1");
        customer1.setEmail("customer1" + System.currentTimeMillis() + "@test.com");
        customer1.setPhone("9876543210");
        customer1.setPassword("password123");
        customer1 = customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setName("Customer 2");
        customer2.setEmail("customer2" + System.currentTimeMillis() + "@test.com");
        customer2.setPhone("9876543210");
        customer2.setPassword("password123");
        customer2 = customerRepository.save(customer2);

        // Create accounts
        sourceAccount = new Account();
        sourceAccount.setAccountNumber("ACC001");
        sourceAccount.setCustomer(customer1);
        sourceAccount.setAccountType(Account.AccountType.SAVINGS);
        sourceAccount.setBalance(1000.0);
        sourceAccount = accountRepository.save(sourceAccount);

        destinationAccount = new Account();
        destinationAccount.setAccountNumber("ACC002");
        destinationAccount.setCustomer(customer2);
        destinationAccount.setAccountType(Account.AccountType.SAVINGS);
        destinationAccount.setBalance(500.0);
        destinationAccount = accountRepository.save(destinationAccount);
    }

    @Test
    public void transfer_withValidAmountAndAccounts_successfullyTransfersAmount() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(200.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void transfer_withZeroAmount_returnsBadRequest() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(0.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transfer_withNegativeAmount_returnsBadRequest() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(-100.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transfer_withSameSourceAndDestination_returnsBadRequest() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(sourceAccount.getId());
        request.setAmount(100.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transfer_withInsufficientBalance_returnsBadRequest() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(2000.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transfer_createsTransactionRecordsForBothAccounts() throws Exception {
        long initialTransactionCount = transactionRepository.count();

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(200.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify transaction records created
        long finalTransactionCount = transactionRepository.count();
        assert finalTransactionCount == initialTransactionCount + 2;
    }

    @Test
    public void transfer_isAtomic_bothAccountsUpdatedOrNone() throws Exception {
        Double sourceBalanceBefore = sourceAccount.getBalance();
        Double destinationBalanceBefore = destinationAccount.getBalance();

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(sourceAccount.getId());
        request.setToAccountId(destinationAccount.getId());
        request.setAmount(200.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify atomicity - both updated or both unchanged
        sourceAccount = accountRepository.findById(sourceAccount.getId()).orElse(null);
        destinationAccount = accountRepository.findById(destinationAccount.getId()).orElse(null);

        assert sourceAccount != null && destinationAccount != null;
        // Either both updated or both unchanged
        boolean sourceChanged = !sourceAccount.getBalance().equals(sourceBalanceBefore);
        boolean destChanged = !destinationAccount.getBalance().equals(destinationBalanceBefore);
        assert sourceChanged == destChanged;
    }

    @Test
    public void deposit_increasesAccountBalance() throws Exception {
        DepositRequest request = new DepositRequest();
        request.setAccountId(sourceAccount.getId());
        request.setAmount(500.0);

        Double balanceBefore = sourceAccount.getBalance();

        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        sourceAccount = accountRepository.findById(sourceAccount.getId()).orElse(null);
        assert sourceAccount != null;
        assert sourceAccount.getBalance() > balanceBefore;
    }

    @Test
    public void withdraw_decreasesAccountBalance() throws Exception {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountId(sourceAccount.getId());
        request.setAmount(300.0);

        Double balanceBefore = sourceAccount.getBalance();

        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        sourceAccount = accountRepository.findById(sourceAccount.getId()).orElse(null);
        assert sourceAccount != null;
        assert sourceAccount.getBalance() < balanceBefore;
    }

    @Test
    public void deposit_createsTransactionRecord() throws Exception {
        long initialTransactionCount = transactionRepository.count();

        DepositRequest request = new DepositRequest();
        request.setAccountId(sourceAccount.getId());
        request.setAmount(500.0);

        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assert transactionRepository.count() == initialTransactionCount + 1;
    }

    @Test
    public void withdraw_createsTransactionRecord() throws Exception {
        long initialTransactionCount = transactionRepository.count();

        WithdrawRequest request = new WithdrawRequest();
        request.setAccountId(sourceAccount.getId());
        request.setAmount(200.0);

        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assert transactionRepository.count() == initialTransactionCount + 1;
    }

    @Test
    public void getTransactionsByAccountId_returnsAllTransactionsForAccount() throws Exception {
        // Perform deposit
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId(sourceAccount.getId());
        depositRequest.setAmount(500.0);

        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());

        // Get transactions
        mockMvc.perform(get("/api/transactions/account/" + sourceAccount.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
