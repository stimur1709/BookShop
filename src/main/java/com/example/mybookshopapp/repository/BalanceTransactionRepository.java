package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {

    Page<BalanceTransaction> findByUserAndStatusPaymentOrderByTimeDesc(int user, int status, Pageable pageable);

    @Query("select distinct b.codePaymentEx from BalanceTransaction b where b.statusPayment in ?1")
    List<String> findDistinctByStatusPaymentIn(Collection<Integer> statusPayments);

    @Query("select distinct b.codePaymentEx from BalanceTransaction b where b.statusPayment in ?1 and b.codePaymentIn = ?2")
    List<String> findDistinctByStatusPaymentInAndCodePaymentEx(Collection<Integer> statusPayments, UUID uuidIn);

    @Modifying
    @Transactional
    @Query("update BalanceTransaction b set b.statusPayment = ?1 where b.codePaymentEx = ?2")
    void updateStatusByCode(int status, UUID code);

}
