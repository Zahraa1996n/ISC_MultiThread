package ir.multithread.springboot.web.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import ir.multithread.springboot.persistence.model.Account;
import ir.multithread.springboot.persistence.model.Customer;

@Service
public class ValidatorService {

    public boolean validateNationalId(String nationalId) {
        return nationalId != null && nationalId.matches("\\d{10}"); 
    }

    public boolean validateBirthDate(LocalDate birthDate) {
        LocalDate minValidDate = LocalDate.of(1996, 1, 1);
        return birthDate != null && birthDate.isAfter(minValidDate);
    }

    public boolean validateAccountLimit(BigDecimal accountBalance, BigDecimal accountLimit) {
        return accountBalance != null && accountLimit != null && accountBalance.compareTo(accountLimit) <= 0;
    }

    public boolean validateAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{22}"); 
    }

    public boolean validateAccountCustomerCompatibility(Customer customer, Account account) {
        return true; 
    }
    
    public boolean validateAccountBalance(double balance, double accountLimit) {
        return balance <= accountLimit;
    }
}

