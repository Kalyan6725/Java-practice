package org.northernarc.testingassignment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.controller.AccountController.*;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class AccountControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private AccountService accountService;

    private CreateAccountRequest createAccountRequest;
    private DepositRequest depositRequest;
    private WithdrawRequest withdrawRequest;
    private TransferRequest transferRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setAccountNumber("ACC001");
        createAccountRequest.setCustomerId(1L);
        createAccountRequest.setAccountType(Account.AccountType.SAVINGS);
        createAccountRequest.setBalance(1000.0);

        depositRequest = new DepositRequest();
        depositRequest.setAccountId(1L);
        depositRequest.setAmount(500.0);

        withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId(1L);
        withdrawRequest.setAmount(200.0);

        transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(1L);
        transferRequest.setToAccountId(2L);
        transferRequest.setAmount(300.0);
    }

    @Test
    public void createAccount_withValidData_returns201Created() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC001");
        account.setBalance(1000.0);

        when(accountService.createAccount(anyString(), anyLong(), any(Account.AccountType.class), anyDouble()))
                .thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(accountService, times(1))
                .createAccount(anyString(), anyLong(), any(Account.AccountType.class), anyDouble());
    }

    @Test
    public void createAccount_withMissingAccountNumber_returns400BadRequest() throws Exception {
        createAccountRequest.setAccountNumber(null);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never())
                .createAccount(anyString(), anyLong(), any(Account.AccountType.class), anyDouble());
    }

    @Test
    public void getAllAccounts_returnsOkWithList() throws Exception {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setAccountNumber("ACC001");

        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("ACC002");

        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));

        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    public void getAccountById_whenAccountExists_returnsOk() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC001");
        account.setBalance(1000.0);

        when(accountService.getAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC001"));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    public void updateAccount_withValidData_returnsOk() throws Exception {
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setBalance(1500.0);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(1500.0);

        when(accountService.updateAccount(1L, 1500.0)).thenReturn(account);

        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.0));

        verify(accountService, times(1)).updateAccount(1L, 1500.0);
    }

    @Test
    public void deleteAccount_whenAccountExists_returns204NoContent() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(1L);
    }

    @Test
    public void deposit_withValidAmount_returnsOk() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1500.0);

        when(accountService.deposit(1L, 500.0)).thenReturn(account);

        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.0));

        verify(accountService, times(1)).deposit(1L, 500.0);
    }

    @Test
    public void deposit_withMissingAmount_returns400BadRequest() throws Exception {
        depositRequest.setAmount(null);

        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).deposit(anyLong(), anyDouble());
    }

    @Test
    public void withdraw_withValidAmount_returnsOk() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(800.0);

        when(accountService.withdraw(1L, 200.0)).thenReturn(account);

        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.0));

        verify(accountService, times(1)).withdraw(1L, 200.0);
    }

    @Test
    public void withdraw_withMissingAmount_returns400BadRequest() throws Exception {
        withdrawRequest.setAmount(null);

        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdraw(anyLong(), anyDouble());
    }

    @Test
    public void transfer_withValidData_returnsOk() throws Exception {
        doNothing().when(accountService).transfer(1L, 2L, 300.0);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).transfer(1L, 2L, 300.0);
    }

    @Test
    public void transfer_withMissingAmount_returns400BadRequest() throws Exception {
        transferRequest.setAmount(null);

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).transfer(anyLong(), anyLong(), anyDouble());
    }
}
