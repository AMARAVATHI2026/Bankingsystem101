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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService,
                                  AccountService accountService,
                                  UserService userService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/deposit")
    public String depositForm(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = userService.findByEmail(ud.getUsername());
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        return "deposit";
    }

    @PostMapping("/transaction/deposit")
    public String doDeposit(@RequestParam Long accountId, @RequestParam double amount,
                            @RequestParam(required = false) String description,
                            RedirectAttributes ra) {
        try {
            Transaction tx = transactionService.deposit(accountId, amount, description);
            ra.addFlashAttribute("successMsg",
                String.format("₹%.2f deposited. New balance: ₹%.2f", amount, tx.getBalanceAfter()));
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/deposit";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = userService.findByEmail(ud.getUsername());
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        return "withdraw";
    }

    @PostMapping("/transaction/withdraw")
    public String doWithdraw(@RequestParam Long accountId, @RequestParam double amount,
                             @RequestParam(required = false) String description,
                             RedirectAttributes ra) {
        try {
            Transaction tx = transactionService.withdraw(accountId, amount, description);
            ra.addFlashAttribute("successMsg",
                String.format("₹%.2f withdrawn. New balance: ₹%.2f", amount, tx.getBalanceAfter()));
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/withdraw";
    }

    @GetMapping("/transfer")
    public String transferForm(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = userService.findByEmail(ud.getUsername());
        model.addAttribute("accounts", accountService.getAccountsByUser(user));
        return "transfer";
    }

    @PostMapping("/transaction/transfer")
    public String doTransfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId,
                             @RequestParam double amount,
                             @RequestParam(required = false) String description,
                             RedirectAttributes ra) {
        try {
            transactionService.transfer(fromAccountId, toAccountId, amount, description);
            ra.addFlashAttribute("successMsg", String.format("₹%.2f transferred successfully.", amount));
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/transfer";
    }

    @GetMapping("/transactions")
    public String transactionHistory(@AuthenticationPrincipal UserDetails ud,
                                     @RequestParam(required = false) Long accountId,
                                     Model model) {
        User user = userService.findByEmail(ud.getUsername());
        List<Account> accounts = accountService.getAccountsByUser(user);
        model.addAttribute("accounts", accounts);
        if (accountId != null) {
            Account selected = accountService.getAccountById(accountId);
            model.addAttribute("selectedAccount", selected);
            model.addAttribute("transactions", transactionService.getTransactionsByAccount(selected));
        } else if (!accounts.isEmpty()) {
            model.addAttribute("transactions", transactionService.getTransactionsByAccounts(accounts));
        }
        return "transactions";
    }
}
