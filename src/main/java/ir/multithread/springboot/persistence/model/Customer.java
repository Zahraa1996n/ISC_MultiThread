package ir.multithread.springboot.persistence.model;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@Entity
@Table(name="CUSTOMER")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Column
	private Long recordNumber;
	
	@Column(name = "CUSTOMER_NAME", length = 50, nullable = false)
	private String name; //encript
	
	@Column(name = "CUSTOMER_Family", length = 50, nullable = false)
	private String surname; //encript
	
	@NotBlank
	@Temporal(TemporalType.DATE)
	private LocalDate birthDate;
	
	@NotBlank
    @Column(length = 100)
    private String address;
    
    @Column(unique = true, nullable = false)
    private String nationalCode ; //code meli
    
    @NotBlank
    @Column
	private String zipCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Long recordNumber) {
		this.recordNumber = recordNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate parsedBirthDate) {
		this.birthDate = parsedBirthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNationalCode() {
		return nationalCode;
	}

	public void setNationalCode(String nationalCode) {
		this.nationalCode = nationalCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", recordNumber=" + recordNumber + ", name=" + name + ", surname=" + surname
				+ ", birthDate=" + birthDate + ", address=" + address + ", nationalCode=" + nationalCode + ", zipCode="
				+ zipCode + "]";
	}



}
