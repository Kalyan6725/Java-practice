package org.northernarc.testingassignment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.entity.Transaction;
import org.northernarc.testingassignment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        testTransaction.setAmount(500.0);
        testTransaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    public void getAllTransactions_returnsOkWithList() throws Exception {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(500.0);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(300.0);

        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    public void getAllTransactions_whenNoTransactions_returnsEmptyList() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    public void getTransactionById_whenTransactionExists_returnsOk() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        mockMvc.perform(get("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(500.0));

        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    public void getTransactionsByAccountId_returnsOkWithList() throws Exception {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(500.0);
        transaction1.setTransactionType(Transaction.TransactionType.DEPOSIT);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(200.0);
        transaction2.setTransactionType(Transaction.TransactionType.WITHDRAWAL);

        when(transactionService.getTransactionsByAccountId(1L))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/api/transactions/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(500.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amount").value(200.0));

        verify(transactionService, times(1)).getTransactionsByAccountId(1L);
    }

    @Test
    public void getTransactionsByAccountId_whenNoTransactions_returnsEmptyList() throws Exception {
        when(transactionService.getTransactionsByAccountId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/transactions/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(transactionService, times(1)).getTransactionsByAccountId(1L);
    }
}
