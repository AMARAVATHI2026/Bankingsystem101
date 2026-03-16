package com.Bank.BankSecurity.repository;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByDateDesc(Account account);
    Page<Transaction> findByAccountOrderByDateDesc(Account account, Pageable pageable);
    List<Transaction> findByAccountInOrderByDateDesc(List<Account> accounts);
}
