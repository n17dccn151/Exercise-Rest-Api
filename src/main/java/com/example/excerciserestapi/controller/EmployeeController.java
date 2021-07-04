package com.example.excerciserestapi.controller;


import com.example.excerciserestapi.entity.Employee;
import com.example.excerciserestapi.exception.BadRequestException;
import com.example.excerciserestapi.exception.ResourceNotFoundException;
import com.example.excerciserestapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("")
    public ResponseEntity< List<Employee>> getAllEmployee(){
        List<Employee> employees = employeeRepository.findAll();
        return new ResponseEntity<>(employees,HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
        throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("employee not found for this id: " +employeeId));
        return new ResponseEntity<>(employee,HttpStatus.OK);
    }


    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee){
        Employee saveEmployee = new Employee();
        try {
            saveEmployee = employeeRepository.save(employee);
        }catch (Exception e){
            throw new BadRequestException("invalid request");
        }

        return new ResponseEntity<>(saveEmployee,HttpStatus.OK);
    }



    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetail) throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("employee not found for this id: " +employeeId));

        employee.setEmail(employeeDetail.getEmail());
        employee.setFirstName(employeeDetail.getFirstName());
        employee.setLastName(employeeDetail.getLastName());
        employee.setRole(employeeDetail.getRole());

        Employee saveEmployee = new Employee();
        try {
            saveEmployee = employeeRepository.save(employee);
        }catch (Exception e){
            throw new BadRequestException("invalid request");
        }

        return new ResponseEntity<>(saveEmployee,HttpStatus.OK);
    }


    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Map<String, Boolean>>  deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("employee not found for this id: " + employeeId));
        this.employeeRepository.delete(employee);

        Map<String, Boolean> map = new HashMap<>();
        map.put("deleted", Boolean.TRUE);

        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
