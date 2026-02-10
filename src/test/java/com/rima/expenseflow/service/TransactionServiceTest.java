package com.rima.expenseflow.service;

import com.rima.expenseflow.dto.TransactionRequest;
import com.rima.expenseflow.dto.TransactionResponse;
import com.rima.expenseflow.exception.ResourceNotFoundException;
import com.rima.expenseflow.model.Transaction;
import com.rima.expenseflow.model.User;
import com.rima.expenseflow.model.enums.Category;
import com.rima.expenseflow.model.enums.TransactionType;
import com.rima.expenseflow.repository.TransactionRepository;
import com.rima.expenseflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private Transaction testTransaction;
    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUser(testUser);
        testTransaction.setAmount(new BigDecimal("50.00"));
        testTransaction.setDescription("Test transaction");
        testTransaction.setCategory(Category.FOOD);
        testTransaction.setType(TransactionType.EXPENSE);
        testTransaction.setTransactionDate(LocalDate.now());

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("50.00"));
        transactionRequest.setDescription("Test transaction");
        transactionRequest.setCategory(Category.FOOD);
        transactionRequest.setType(TransactionType.EXPENSE);
        transactionRequest.setTransactionDate(LocalDate.now());
    }

    @Test
    void createTransaction_WhenValid_ShouldCreateTransaction() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // When
        TransactionResponse result = transactionService.createTransaction(1L, transactionRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(result.getDescription()).isEqualTo("Test transaction");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> transactionService.createTransaction(99L, transactionRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getTransactions_ShouldReturnPagedTransactions() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> transactionPage = new PageImpl<>(Arrays.asList(testTransaction));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Your service calls findByUser(user, pageable) with User object, not userId
        when(transactionRepository.findByUser(any(User.class), any(Pageable.class)))
                .thenReturn(transactionPage);

        // When
        Page<TransactionResponse> result = transactionService.getTransactions(1L, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));

        verify(userRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).findByUser(any(User.class), any(Pageable.class));
    }
    @Test
    void getTransactionById_WhenExists_ShouldReturnTransaction() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // Your service uses findById, not findByIdAndUserId
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(testTransaction));

        // When
        TransactionResponse result = transactionService.getTransactionById(1L, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getTransactionById_WhenNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // Your service uses findById
        when(transactionRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> transactionService.getTransactionById(1L, 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteTransaction_WhenExists_ShouldDeleteTransaction() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // Your service uses findById
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(testTransaction));

        // When
        transactionService.deleteTransaction(1L, 1L);

        // Then
        verify(transactionRepository, times(1)).delete(testTransaction);
    }
}