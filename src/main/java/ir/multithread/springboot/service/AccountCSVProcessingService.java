package ir.multithread.springboot.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;


import ir.multithread.springboot.persistence.model.Account;
import ir.multithread.springboot.persistence.model.Customer;
import ir.multithread.springboot.persistence.repo.AccountRepository;
import ir.multithread.springboot.persistence.repo.CustomerRepository;
import ir.multithread.springboot.web.Encryption;
import ir.multithread.springboot.web.validation.ErrorLoggingService;
import ir.multithread.springboot.web.validation.ValidatorService;

@Service
public class AccountCSVProcessingService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ValidatorService validatorService;
    private final ErrorLoggingService errorLoggingService;

    public AccountCSVProcessingService(AccountRepository accountRepository,
                                       CustomerRepository customerRepository,
                                       ValidatorService validatorService,
                                       ErrorLoggingService errorLoggingService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.validatorService = validatorService;
        this.errorLoggingService = errorLoggingService;
    }

    public void processAccountCSV(String accountFilePath, int numberOfThreads) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get(accountFilePath));

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        int totalLines = allLines.size();
        
        // Calculate chunk size based on total lines and number of threads
        int chunkSize = (totalLines + numberOfThreads - 1) / numberOfThreads; // Ensure all lines are covered

        int chunkCount = (totalLines + chunkSize - 1) / chunkSize; // Calculate the total number of chunks

        for (int i = 0; i < chunkCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalLines);
            List<String> chunkLines = allLines.subList(start, end);

            executor.submit(() -> processChunk(chunkLines));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
    }

    private void processChunk(List<String> chunkLines) {
        for (String line : chunkLines) {
            try {
                processAccountLine(line);
            } catch (Exception e) {
                errorLoggingService.logError("accounts.csv", e.getMessage(), "PROCESS_ERROR");
            }
        }
    }

    private void processAccountLine(String line) throws Exception {
        String[] fields = line.split(",");

        if (fields.length != 4) {
            throw new Exception("Invalid number of fields in line");
        }

        String accountNumber = fields[0];
        String balance = fields[1];
        String accountLimit = fields[2];
        String customerNationalId = fields[3];

        
        if (!validatorService.validateAccountNumber(accountNumber)) {
            errorLoggingService.logError("accounts.csv", "ERR_ACCOUNT_NUMBER", "Account number is not 22 digits");
            return; 
        }

        double accountBalance;
        double accountLimitValue;

        try {
            accountBalance = Double.parseDouble(balance);
            accountLimitValue = Double.parseDouble(accountLimit);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid balance or account limit format");
        }

        if (!validatorService.validateAccountBalance(accountBalance, accountLimitValue)) {
            errorLoggingService.logError("accounts.csv", "ERR_BALANCE_LIMIT", "Account balance exceeds account limit");
            return;
        }

        Customer customer = customerRepository.findByNationalCode(customerNationalId);
        if (customer == null) {
            errorLoggingService.logError("accounts.csv", "ERR_CUSTOMER_NOT_FOUND", "Customer not found for national ID: " + customerNationalId);
            return;
        }

        String encryptedAccountNumber = Encryption.encrypt(accountNumber);

        Account account = new Account();
        account.setAccountNumber(encryptedAccountNumber); // شماره حساب رمزنگاری‌شده
        account.setAccountBalance(accountBalance); // موجودی حساب
        account.setAccountBalance(accountLimitValue); // محدودیت حساب
        account.setCustomerId(customer); // تطابق حساب با مشتری

        accountRepository.save(account);
    }
}

