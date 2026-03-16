package com.Bank.BankSecurity.controller;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.User;
import com.Bank.BankSecurity.service.AccountService;
import com.Bank.BankSecurity.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newAccountForm(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("accountTypes", Account.AccountType.values());
        return "account-new";
    }

    @PostMapping("/new")
    public String createAccount(@AuthenticationPrincipal UserDetails userDetails,
                                @ModelAttribute Account account,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        account.setUser(user);
        Account saved = accountService.createAccount(account);
        redirectAttributes.addFlashAttribute("successMsg",
                "New account created! Account No: " + saved.getAccountNumber());
        return "redirect:/dashboard";
    }
}
