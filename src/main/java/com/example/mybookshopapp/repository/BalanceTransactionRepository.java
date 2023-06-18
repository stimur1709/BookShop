package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.data.entity.payments.BalanceTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BalanceTransactionRepository extends ModelRepository<BalanceTransaction> {

    @Query(value = "select B.ID, B.STATUS_PAYMENT_ID, B.USER_ID, " +
            "       B.BOOK_ID, B.VALUE, B.TIME, " +
            "       B.DESCRIPTION, B.CODE_PAYMENT_EX, B.CODE_PAYMENT_IN " +
            "from BALANCE_TRANSACTION B " +
            "where B.USER_ID = ?1 " +
            "  and B.STATUS_PAYMENT_ID in (?2) ", nativeQuery = true)
    Page<BalanceTransaction> findBalanceTransactions(Integer userId, List<Integer> statuses, Pageable pageable);

    @Query("select distinct b.codePaymentEx from BalanceTransaction b where b.statusPayment in ?1")
    List<String> findDistinctByStatusPaymentIn(Collection<Integer> statusPayments);

    @Query("select distinct b.codePaymentEx from BalanceTransaction b where b.statusPayment in ?1 and b.codePaymentIn = ?2")
    List<String> findDistinctByStatusPaymentInAndCodePaymentEx(Collection<Integer> statusPayments, UUID uuidIn);

    @Modifying
    @Transactional
    @Query("update BalanceTransaction b set b.statusPayment = ?1 where b.codePaymentEx = ?2")
    void updateStatusByCode(int status, UUID code);

}
