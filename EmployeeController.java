package com.piabudhabi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piabudhabi.domain.Employee;
import com.piabudhabi.dto.EmployeeDTO;
import com.piabudhabi.exception.EmployeeException;
import com.piabudhabi.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeSvc;
	
	@GetMapping("/employee")
	public ResponseEntity<Employee> readEmployee(@RequestParam(name="id", required=false) Long employeeId,
			@RequestParam(name="name", required=false) String name)
	{
		Employee error = new Employee();
		if(name != null) {
			Employee employee = employeeSvc.getEmployee(name);
			System.out.println("Recived employeeName = "+name);
			if(employee != null) {
				return new ResponseEntity<Employee>(employee, HttpStatus.OK);
			}else {
				error.setFirstName("There is no employee with first name "+name+ " OR last name "+name);
				return new ResponseEntity<Employee>(error, HttpStatus.BAD_REQUEST);	
			}
		}
		if(employeeId != null) {
			Employee employee = employeeSvc.getEmployee(employeeId);
			System.out.println("Recived employeeId = "+employeeId);
			if(employee != null) {
				return new ResponseEntity<Employee>(employee, HttpStatus.OK);
			}else {
				error.setFirstName("There is no employee with ID "+employeeId);
				return new ResponseEntity<Employee>(error, HttpStatus.BAD_REQUEST);	
			}
		}
		error.setFirstName("Employee name or Employee ID is required");
		return new ResponseEntity<Employee>(error, HttpStatus.BAD_REQUEST);

	}
	
	@PostMapping("/employee")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO employee){
		System.out.println("Recived FirstName = "+employee.getFirstName());
		System.out.println("Recived LasttName = "+employee.getLastName());
		System.out.println("Recived Address = "+employee.getAddress());
		System.out.println("Recived ContactNumber = "+employee.getContactNumber());
		System.out.println("Recived Designation = "+employee.getDesignation());
		
		Employee employeeDB = new Employee();
		employeeDB.setFirstName(employee.getFirstName());
		employeeDB.setLastName(employee.getLastName());
		employeeDB.setDesignation(employee.getDesignation());
		employeeDB.setContactNumber(employee.getContactNumber());
		employeeDB.setAddress(employee.getAddress());
		
		try {
			Long employeeId = employeeSvc.saveEmployee(employeeDB);
			if(employeeId !=null) {
				return new ResponseEntity<String>("Saved employee succesfully and its ID :"+employeeId, HttpStatus.OK);
			}
		}catch(EmployeeException ex) {
			return new ResponseEntity<String>("Cannot save the employee : "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Cannot save the employee", HttpStatus.BAD_REQUEST);
	}

}
