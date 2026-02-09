package com.rima.expenseflow.controller;

import com.rima.expenseflow.dto.MonthlySummaryResponse;
import com.rima.expenseflow.dto.TransactionRequest;
import com.rima.expenseflow.dto.TransactionResponse;
import com.rima.expenseflow.model.User;
import com.rima.expenseflow.model.enums.Category;
import com.rima.expenseflow.model.enums.TransactionType;
import com.rima.expenseflow.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/transactions")  // CHANGED: Removed /users/{userId}
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse created = transactionService.createTransaction(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<TransactionResponse> transactions = transactionService
                .getTransactions(currentUser.getId(), type, category, pageable);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @PathVariable Long transactionId) {

        TransactionResponse transaction = transactionService
                .getTransactionById(currentUser.getId(), transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse updated = transactionService
                .updateTransaction(currentUser.getId(), transactionId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @PathVariable Long transactionId) {

        transactionService.deleteTransaction(currentUser.getId(), transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @AuthenticationPrincipal User currentUser,  // Get from JWT
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth month) {

        YearMonth targetMonth = (month != null) ? month : YearMonth.now();

        MonthlySummaryResponse summary = transactionService
                .getMonthlySummary(currentUser.getId(), targetMonth);
        return ResponseEntity.ok(summary);
    }
}