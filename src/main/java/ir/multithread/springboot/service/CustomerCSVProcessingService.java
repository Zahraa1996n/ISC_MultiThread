package ir.multithread.springboot.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.springframework.stereotype.Service;

import ir.multithread.springboot.persistence.model.Account;
import ir.multithread.springboot.persistence.model.Customer;
import ir.multithread.springboot.persistence.repo.CustomerRepository;
import ir.multithread.springboot.web.Encryption;
import ir.multithread.springboot.web.validation.ErrorLoggingService;
import ir.multithread.springboot.web.validation.ValidatorService;



@Service
public class CustomerCSVProcessingService {

    private final CustomerRepository customerRepository;
    private final ValidatorService validatorService;
    private final ErrorLoggingService errorLoggingService;

    public CustomerCSVProcessingService(CustomerRepository customerRepository,
                                        ValidatorService validatorService,
                                        ErrorLoggingService errorLoggingService) {
        this.customerRepository = customerRepository;
        this.validatorService = validatorService;
        this.errorLoggingService = errorLoggingService;
    }

    public void processCustomerCSV(String customerFilePath, int numberOfThreads) throws IOException {
       
        List<String> allLines = Files.readAllLines(Paths.get(customerFilePath));

        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be greater than zero.");
        }

        int totalLines = allLines.size();
        int chunkSize = (totalLines + numberOfThreads - 1) / numberOfThreads; 

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        int chunkCount = (totalLines + chunkSize - 1) / chunkSize; 

        for (int i = 0; i < chunkCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalLines);
            List<String> chunkLines = allLines.subList(start, end);

            executor.submit(() -> processChunk(chunkLines));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS); // منتظر بودن تا تمام نخ‌ها کار خود را تمام کنند
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
    }

    private void processChunk(List<String> chunkLines) {
        for (String line : chunkLines) {
            try {
                processCustomerLine(line); 
            } catch (Exception e) {
                
                errorLoggingService.logError("customers.csv", e.getMessage(), "PROCESS_ERROR");
            }
        }
    }

    private void processCustomerLine(String line) throws Exception {
        String[] fields = line.split(",");

        if (fields.length != 6) {
            throw new Exception("Invalid number of fields in line");
        }

       //firstName, surName, birthDate, customerAddress, customerZipCode, customerNationalId
        String firstName = fields[0];
        String surName = fields[1];
        String birthDate = fields[2];
        String customerAddress = fields[3];
        String customerZipCode = fields[4];
        String customerNationalId = fields[5];
        Account account = new Account();
        Customer customer = new Customer();

        if (!validatorService.validateNationalId(customerNationalId)) {
            errorLoggingService.logError("customers.csv", "ERR_NATIONAL_ID", "Invalid national ID format");
        }

        if (!validatorService.validateBirthDate(customer.getBirthDate())) {
            errorLoggingService.logError("customers.csv",  "ERR_BIRTH_DATE", "Birth date is before 1996");
        }

        if (!validatorService.validateAccountNumber(account.getAccountNumber())) {
            errorLoggingService.logError("accounts.csv",  "ERR_ACCOUNT_NUMBER", "Account number is not 22 digits");
        }

        errorLoggingService.writeErrorsToJson("error_log.json");

        String encryptedFirstName = Encryption.encrypt(firstName);
        String encryptedLastName = Encryption.encrypt(surName);
        String encryptedNationalId = Encryption.encrypt(customerNationalId);

        customer.setName(encryptedFirstName); 
        customer.setSurname(encryptedLastName); 
        customer.setBirthDate(LocalDate.parse(birthDate)); 
        customer.setAddress(customerAddress); 
        customer.setZipCode(customerZipCode); 
        customer.setNationalCode(encryptedNationalId); 

        customerRepository.save(customer);
    }
}
