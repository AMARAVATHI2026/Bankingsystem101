package com.Bank.BankSecurity.service;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.Transaction;
import com.Bank.BankSecurity.repository.AccountRepository;
import com.Bank.BankSecurity.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction deposit(Long accountId, double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setType(Transaction.TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setDescription(description != null ? description : "Deposit");
        tx.setBalanceAfter(account.getBalance());
        tx.setAccount(account);
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction withdraw(Long accountId, double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (account.getBalance() < amount)
            throw new IllegalArgumentException("Insufficient balance. Available: ₹" + account.getBalance());
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setType(Transaction.TransactionType.WITHDRAWAL);
        tx.setAmount(amount);
        tx.setDescription(description != null ? description : "Withdrawal");
        tx.setBalanceAfter(account.getBalance());
        tx.setAccount(account);
        return transactionRepository.save(tx);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive");
        if (fromId.equals(toId)) throw new IllegalArgumentException("Cannot transfer to the same account");

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (from.getBalance() < amount)
            throw new IllegalArgumentException("Insufficient balance. Available: ₹" + from.getBalance());

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        accountRepository.save(from);
        accountRepository.save(to);

        String desc = description != null ? description : "Transfer";

        Transaction outTx = new Transaction();
        outTx.setType(Transaction.TransactionType.TRANSFER_OUT);
        outTx.setAmount(amount);
        outTx.setDescription(desc + " → " + to.getAccountNumber());
        outTx.setBalanceAfter(from.getBalance());
        outTx.setAccount(from);
        outTx.setLinkedAccountId(toId);
        transactionRepository.save(outTx);

        Transaction inTx = new Transaction();
        inTx.setType(Transaction.TransactionType.TRANSFER_IN);
        inTx.setAmount(amount);
        inTx.setDescription(desc + " ← " + from.getAccountNumber());
        inTx.setBalanceAfter(to.getBalance());
        inTx.setAccount(to);
        inTx.setLinkedAccountId(fromId);
        transactionRepository.save(inTx);
    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccountOrderByDateDesc(account);
    }

    public List<Transaction> getRecentTransactions(Account account, int limit) {
        return transactionRepository.findByAccountOrderByDateDesc(account, PageRequest.of(0, limit)).getContent();
    }

    public List<Transaction> getTransactionsByAccounts(List<Account> accounts) {
        return transactionRepository.findByAccountInOrderByDateDesc(accounts);
    }
}
