package ir.multithread.springboot.web;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.multithread.springboot.service.AccountCSVProcessingService;
import ir.multithread.springboot.service.CustomerCSVProcessingService;

@RestController
@RequestMapping("/process")
public class CSVController {
	private final AccountCSVProcessingService accountCSVProcessingService;
    private final CustomerCSVProcessingService customerCSVProcessingService;

    public CSVController(AccountCSVProcessingService accountCSVProcessingService,
                         CustomerCSVProcessingService customerCSVProcessingService) {
        this.accountCSVProcessingService = accountCSVProcessingService;
        this.customerCSVProcessingService = customerCSVProcessingService;
    }

    @PostMapping("/account")
    public ResponseEntity<String> processAccountCSV(@RequestParam("file") String accountFilePath,
                                                    @RequestParam("threads") int numberOfThreads
                                                    ) {
        try {
            accountCSVProcessingService.processAccountCSV(accountFilePath, numberOfThreads);
            return ResponseEntity.ok("Account CSV processing started.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Account CSV file.");
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<String> processCustomerCSV(@RequestParam("file") String customerFilePath,
                                                     @RequestParam("threads") int numberOfThreads
                                                     ) {
        try {
            customerCSVProcessingService.processCustomerCSV(customerFilePath, numberOfThreads);
            return ResponseEntity.ok("Customer CSV processing started.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Customer CSV file.");
        }
    }

}
