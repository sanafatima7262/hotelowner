package com.piabudhabi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.piabudhabi.domain.Company;
import com.piabudhabi.dto.CompanyDTO;
import com.piabudhabi.exception.CompanyException;
import com.piabudhabi.service.CompanyService;

@RestController
public class CompanyController {

	//@Autowired
	//private CompanyRepository repository;

	@Autowired
	private CompanyService companySvc;
	
    @GetMapping("/company/{id}/{name}")
    public ResponseEntity<Company> readCompany(@PathVariable("id") Long companyId,@PathVariable("name") String companyName)
    {
    	Company company = companySvc.getCompany(companyId);

        // Print and display name and age
        System.out.println("Recived companyId = "+companyId);
        System.out.println("Recived companyName = "+companyName);
    	if(company != null) {
    		return new ResponseEntity<Company>(company, HttpStatus.OK);
    	}
        Company error = new Company();
        error.setCompanyName("There is no company with id "+companyId);
        return new ResponseEntity<Company>(error, HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping("/company")
    public ResponseEntity<String> createCompany(@RequestBody CompanyDTO company) {
    	System.out.println("Recived ContractorLevel = "+company.getContractorLevel());
        System.out.println("Recived ContractorType = "+company.getContractorType());
        System.out.println("Recived Names = "+company.getNames());
        Company companyDB = new Company();
        companyDB.setContractorLevel(company.getContractorLevel());
        companyDB.setContractorType(company.getContractorType());
        companyDB.setCompanyName(company.getNames());
        
        try {
        	Long companyId = companySvc.saveCompany(companyDB);
        	if(companyId != null) {
            	return new ResponseEntity<String>("Saved company successfully and its ID: "+companyId, HttpStatus.OK);
        	}
        }catch(CompanyException ex) {
        	return new ResponseEntity<String>("Cannot Save company: "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Cannot Save company", HttpStatus.BAD_REQUEST);
    }
}