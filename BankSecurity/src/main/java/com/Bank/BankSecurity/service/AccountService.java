package com.Bank.BankSecurity.service;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.User;
import com.Bank.BankSecurity.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
            account.setAccountNumber(generateAccountNumber());
        }
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
    }

    private String generateAccountNumber() {
        String candidate;
        do {
            candidate = "ACC" + String.format("%09d", new Random().nextInt(1_000_000_000));
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }
}
