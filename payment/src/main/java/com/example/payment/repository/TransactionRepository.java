package com.example.payment.repository;

import com.example.payment.domain.Transaction;
import com.example.payment.dto.MonthlyExpenseDto;
import com.example.payment.dto.MonthlyPaymentDto;
import com.example.payment.dto.MonthlySpendDto;
import com.example.payment.response.PaymentResponse;
import com.example.payment.response.PaymentsResponse;
import com.example.payment.response.TransactionResponse;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(UUID userId);
    Page<Transaction> findByUserIdAndAccountIdOrderByCreatedAtDesc(UUID userId , Long accountId, Pageable pageable);

    @Query(value = "SELECT t FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.categoryId = :categoryId " +
            "AND t.transactionType = 'expense' " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findByUserIdAndCategoryIdAndCreatedAt(@Param("userId") UUID userId,
                                                            @Param("categoryId") Long categoryId,
                                                            @Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT t FROM Transaction t " +
            "WHERE t.userId = :userId " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsByUserAndYearAndMonth(@Param("userId") UUID userId,
                                                            @Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);

    @Query(value= "select sum(t.price) from Transaction t " +
            "where t.transactionType = 'expense' " +
            "AND t.userId = :userId " +
            "AND t.createdAt BETWEEN :startDate AND :endDate ")
    Double findTotalMonthlySpending(@Param("userId") UUID userId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT new com.example.payment.dto.MonthlySpendDto(t.categoryId, t.categoryName, sum(t.price)) from Transaction t "  +
            "where t.transactionType = 'expense' " +
            "AND t.userId = :userId " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY t.categoryId, t.categoryName " +
            "ORDER BY sum(t.price) DESC")
    List<MonthlySpendDto> findMonthlySpendingByCreatedAt(@Param("userId") UUID userId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.example.payment.dto.MonthlySpendDto(t.categoryId, t.categoryName, sum(t.price)) " +
            "FROM Transaction t " +
            "WHERE t.transactionType = 'expense' " +
            "AND t.userId = :userId " +
            "AND t.createdAt BETWEEN :startDate AND CURRENT_DATE " +
            "GROUP BY t.categoryId, t.categoryName " +
            "ORDER BY SUM(t.price) DESC")
    List<MonthlySpendDto> findSumOfLast30Days(@Param("userId") UUID userId,
                                              @Param("startDate") LocalDateTime startDate);
    @Query("SELECT new com.example.payment.dto.MonthlyPaymentDto(t.recipient, t.price) " +
            "FROM Transaction t " +
            "WHERE t.transactionType = 'expense' " +
            "AND t.userId = :userId " +
            "AND t.createdAt BETWEEN :startDate AND CURRENT_DATE " +
            "GROUP BY t.recipient, t.price " +
            "ORDER BY SUM(t.price) DESC")
    List<MonthlyPaymentDto> findPaymentsOfLast30Days(@Param("userId") UUID userId,
                                                     @Param("startDate") LocalDateTime startDate);

    @Query("SELECT new com.example.payment.response.PaymentResponse(t.recipient, t.price, t.categoryName) " +
            "FROM Transaction t " +
            "WHERE t.transactionType = 'expense' " +
            "And t.userId = :userId " +
            "And t.createdAt between :startDate and CURRENT_DATE " +
            "group by t.categoryName, t.recipient, t.price ")
    List<PaymentResponse> findTransactionByUserId(@Param("userId") UUID userId,
                                                  @Param("startDate") LocalDateTime startDate);

    @Query("select new com.example.payment.response.PaymentsResponse(t.price, t.categoryName) " +
            "FROM Transaction t " +
            "WHERE t.transactionType = 'expense' " +
            "AND t.userId = :userId " +
            "And t.createdAt between :startDate and CURRENT_DATE " +
            "group by t.price, t.categoryName")
    List<PaymentsResponse> findPaymentsByUserId(@Param("userId") UUID userId,
                                                @Param("startDate") LocalDateTime startDate);
}
