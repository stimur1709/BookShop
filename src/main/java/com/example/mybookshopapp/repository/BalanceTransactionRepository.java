package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {


    @Query("select distinct b.codePayment from BalanceTransaction b where b.statusPayment in ?1")
    List<String> findDistinctByStatusPaymentIn(Collection<Integer> statusPayments);

    @Modifying
    @Transactional
    @Query("update BalanceTransaction b set b.statusPayment = ?1 where b.codePayment = ?2")
    void updateStatusByCode(int status, UUID code);

}
