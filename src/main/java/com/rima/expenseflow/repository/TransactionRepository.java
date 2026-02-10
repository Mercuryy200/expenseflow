package com.rima.expenseflow.repository;

import com.rima.expenseflow.model.Transaction;
import com.rima.expenseflow.model.User;
import com.rima.expenseflow.model.enums.Category;
import com.rima.expenseflow.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions for a user by userId
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    // Find by userId and type
    Page<Transaction> findByUserIdAndType(Long userId, TransactionType type, Pageable pageable);

    // Find by userId and category
    Page<Transaction> findByUserIdAndCategory(Long userId, Category category, Pageable pageable);

    // Find by userId, type, and category
    Page<Transaction> findByUserIdAndTypeAndCategory(
            Long userId,
            TransactionType type,
            Category category,
            Pageable pageable
    );

    // Find single transaction by id and userId (for authorization)
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    // Find all transactions for a user (original method - keep for backward compatibility)
    Page<Transaction> findByUser(User user, Pageable pageable);

    // Find by user and type
    Page<Transaction> findByUserAndType(User user, TransactionType type, Pageable pageable);

    // Find by user and category
    Page<Transaction> findByUserAndCategory(User user, Category category, Pageable pageable);

    // Find by user and date range
    List<Transaction> findByUserAndTransactionDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

    // Find by user, type, and date range
    List<Transaction> findByUserAndTypeAndTransactionDateBetween(
            User user,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate
    );

    // Calculate total by user and type
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type")
    BigDecimal calculateTotalByUserAndType(
            @Param("user") User user,
            @Param("type") TransactionType type
    );

    // Calculate total by user, type, and date range
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalByUserAndTypeAndDateRange(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Get category breakdown for user
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = :type " +
            "GROUP BY t.category")
    List<Object[]> getCategoryBreakdown(
            @Param("user") User user,
            @Param("type") TransactionType type
    );
}