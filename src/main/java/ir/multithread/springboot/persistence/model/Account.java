package ir.multithread.springboot.persistence.model;


import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Validated
@Entity
@Table(name="Account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	@Column(name = "ACCOUNT_NUMBER", nullable = false, unique = true)
	private String accountNumber; //encripte
	
	
	@Min(value = 10000, message = "Balance must be at least 10,000 Rial")
	@Column(name = "BALANCE", nullable = false)
	private double accountBalance; //encripte
	
	@Column(name = "accountCreationDate", nullable = false)
	private LocalDate accountOpenDate;
	
	@NotNull
	@Column
	private Long recordNumber;
	
	@Enumerated(EnumType.STRING)
	private AccountType type;
	
	@Column(name = "account_limit", nullable = false)
	private double accountLimit;
	
	@ManyToOne
    @JoinColumn(name = "account_customer_id", nullable = false)
    private Customer accountCustomerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountLimitValue) {
		this.accountBalance = accountLimitValue;
	}

	public LocalDate getAccountOpenDate() {
		return accountOpenDate;
	}

	public void setAccountOpenDate(LocalDate localDate) {
		this.accountOpenDate = localDate;
	}

	public Long getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Long recordNumber) {
		this.recordNumber = recordNumber;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public double getAccountLimit() {
		return accountLimit;
	}

	public void setAccountLimit(double accountLimitValue) {
		this.accountLimit = accountLimitValue;
	}

	public Customer getCustomerId() {
		return accountCustomerId;
	}

	public void setCustomerId(Customer customerId) {
		this.accountCustomerId = customerId;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber + ", accountBalance=" + accountBalance
				+ ", accountOpenDate=" + accountOpenDate + ", recordNumber=" + recordNumber + ", type=" + type
				+ ", accountLimit=" + accountLimit + ", customerId=" + accountCustomerId + "]";
	}

	
}
