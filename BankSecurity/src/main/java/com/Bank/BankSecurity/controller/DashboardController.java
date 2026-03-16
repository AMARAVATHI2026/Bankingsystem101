package com.Bank.BankSecurity.controller;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.Transaction;
import com.Bank.BankSecurity.model.User;
import com.Bank.BankSecurity.service.AccountService;
import com.Bank.BankSecurity.service.TransactionService;
import com.Bank.BankSecurity.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public DashboardController(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Account> accounts = accountService.getAccountsByUser(user);
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
        List<Transaction> recentTx = accounts.isEmpty() ? List.of()
                : transactionService.getTransactionsByAccounts(accounts).stream().limit(5).toList();
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("recentTransactions", recentTx);
        return "dashboard";
    }
}
