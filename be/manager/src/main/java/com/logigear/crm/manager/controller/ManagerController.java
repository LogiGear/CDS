package com.logigear.crm.manager.controller;

import com.logigear.crm.manager.payload.EmployeePayload;
import com.logigear.crm.manager.response.DepartmentResponse;
import com.logigear.crm.manager.response.EmployeeDetailsDTO;
import com.logigear.crm.manager.response.EmployeeResponseQueryDsl;
import com.logigear.crm.manager.service.DepartmentService;
import com.logigear.crm.manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager/api/manager")
public class ManagerController {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public ManagerController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getCdmRoles())")
    @GetMapping("employee")
    public ResponseEntity<?> getEmployee() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeService.findAllExcludeAdmin(EmployeeResponseQueryDsl.class));
    }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getCdmRoles())")
    @GetMapping("employee/{id}")
    public ResponseEntity<?> getEmployeeBasedOnManagerAndCdm(@PathVariable("id") Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeService.findAllExcludeAdmin(id, EmployeeResponseQueryDsl.class));
    }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getManagerRoles())")
    @GetMapping("department")
    public ResponseEntity<?> getDepartment() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(departmentService.findAll(DepartmentResponse.class));
    }

    // @PreAuthorize("hasAnyRole(@roleConfiguration.getManagerRoles())")
    // @PatchMapping(value = "employee", produces =
    // {MediaType.APPLICATION_JSON_VALUE})
    // public ResponseEntity<?> updateEmployeeDetails(@RequestBody
    // EmployeeDetailsDTO req) {
    // return ResponseEntity.ok(employeeService.updateEmployeeDetails(req));
    // }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getManagerRoles())")
    @PatchMapping(value = "employee-project-department/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateEmployeeProjectDepartmentById(@RequestBody EmployeePayload req,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.updateEmployee(req, id, EmployeeDetailsDTO.class));
    }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getManagerRoles())")
    @PatchMapping(value = "employee-project-department", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateEmployeeProjectDepartment(@RequestBody EmployeePayload req) {
        return ResponseEntity.ok(employeeService.updateEmployee(req, req.getId(), EmployeeDetailsDTO.class));
    }
}
