package com.Bank.BankSecurity.service;

import com.Bank.BankSecurity.model.Account;
import com.Bank.BankSecurity.model.User;
import com.Bank.BankSecurity.repository.AccountRepository;
import com.Bank.BankSecurity.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.encoder = encoder;
    }

    @Transactional
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        User saved = userRepository.save(user);

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setBalance(0.0);
        account.setUser(saved);
        accountRepository.save(account);

        return saved;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String generateAccountNumber() {
        String candidate;
        do {
            candidate = "ACC" + String.format("%09d", new Random().nextInt(1_000_000_000));
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }
}
