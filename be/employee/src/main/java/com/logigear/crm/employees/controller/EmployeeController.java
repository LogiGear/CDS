package com.logigear.crm.employees.controller;

import java.util.List;

import com.logigear.crm.employees.exception.ResourceNotFoundException;
import com.logigear.crm.employees.model.User;
import com.logigear.crm.employees.model.composite.EmployeeDetails_Project;
import com.logigear.crm.employees.response.*;
import com.logigear.crm.employees.service.*;
import com.logigear.crm.employees.response.EmployeeDetailsDTO;
import com.logigear.crm.employees.service.DepartmentService;
import com.logigear.crm.employees.service.EmployeeProjectService;
import com.logigear.crm.employees.service.ProjectService;
import com.logigear.crm.employees.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.logigear.crm.employees.model.EmployeeDetails;

@RestController
@RequestMapping("/employees/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ProjectService projectService;
    private final EmployeeProjectService employeeProjectService;
    private final UserService userService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService,
            ProjectService projectService, EmployeeProjectService employeeProjectService, UserService userService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.projectService = projectService;
        this.employeeProjectService = employeeProjectService;
        this.userService = userService;
    }

    /**
     * All users must be authenticated before using this method. Get an employee by
     * using the provided employee id in Long datatype.
     *
     * @param id The provided employee id
     * @return An employee associated with provided employee id.
     */

    @PostAuthorize("hasAnyRole(@roleConfiguration.userRoles)")
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDetailsProjectDTO> getDetailsProjectDTO(@PathVariable("id") Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeProjectService.getProjectDetailByEmployee(id));
    }

    /**
     * Users must have ADMIN role before using this method. Get all employees.
     *
     * @return A list of current employees.
     */
    @PostAuthorize("hasRole(@roleConfiguration.adminRoles)")
    // @Secured("ADMIN")
    @GetMapping()
    public ResponseEntity<List<EmployeeDetails>> getAll() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(employeeService.getEmployees());
    }

    /**
     * Users must have ADMIN role before using this method. (Partially) update the
     * current employee details.
     *
     * @param req The provided employee partial information.
     * @return Current details of an employee
     */
    @PreAuthorize("hasAnyRole(@roleConfiguration.userRoles)")
    @PatchMapping(value = "update", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<EmployeeDetails> updateEmployeeDetails(@RequestBody EmployeeDetailsDTO req) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeService.updateEmployeeDetails(req));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getCdmRoles())")
    @GetMapping("department")
    public ResponseEntity<?> getDepartmentsNotDeleted() {
        List<DepartmentResponse> departmentResponses = departmentService.findAllByIsDeleted(DepartmentResponse.class,
                false);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(departmentResponses);
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getCdmRoles())")
    @GetMapping("project")
    public ResponseEntity<?> getProjects() {
        List<ProjectResponse> projectResponses = projectService.findAllByIsDeleted(ProjectResponse.class, false);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(projectResponses);
    }

    @PatchMapping(value = "update-project-department/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateProjectAndDepartmentForEmployeeId(
            @RequestBody EmployeeUpdateRequest employeeUpdateRequest, @PathVariable("id") Long id) {
        boolean flagMan = false;
        boolean flagAdm = false;
        employeeUpdateRequest.setId(id);
        for (GrantedAuthority s : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (s.getAuthority().toUpperCase().equals("ROLE_MANAGER") && !flagMan) {
                System.out.println("Flag Mang: " + flagMan);
                EmployeeUpdateRequest employeeUpdateRequestDto = employeeService
                        .updateEmployeeDepartmentAndProject(employeeUpdateRequest);

                if (employeeUpdateRequestDto == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResourceNotFoundException("NOT_FOUND"));
                }

                flagMan = true;
                System.out.println("has role MANAGER");
                // return ResponseEntity.status(HttpStatus.OK).body(employeeUpdateRequestDto);
            }
            if (s.getAuthority().toUpperCase().equals("ROLE_ADMIN") && !flagAdm) {
                System.out.println("Flag Admin: " + flagAdm);
                EmployeeUpdateRequest employeeUpdateRequestDto = employeeService
                        .updateEmployeeWorkingInfo(employeeUpdateRequest);

                if (employeeUpdateRequestDto == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResourceNotFoundException("NOT_FOUND"));
                }

                flagAdm = true;
                System.out.println("has role ADMIN");
                // return ResponseEntity.status(HttpStatus.OK).body(employeeUpdateRequestDto);
            }
        }
        if (flagAdm || flagMan) {
            System.out.println("Flag Man + Adm: " + flagAdm + " " + flagMan);
            return ResponseEntity.status(HttpStatus.OK).body("UPDATE");
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new RuntimeException("AUTHORITY_NOT_ACCEPTED"));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @GetMapping("employee-in-department/{id}")
    public ResponseEntity<?> getEmployeeByDepartment(@PathVariable("id") Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeService.getEmployeeByDepartment(id, false, EmployeeResponse.class));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @GetMapping("department-structure/{id}")
    public ResponseEntity<?> getDepartmentStructure(@PathVariable("id") Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(departmentService.findDepartment(DepartmentStructureResponse.class, false, id));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @GetMapping("department-structure")
    public ResponseEntity<?> getAllDepartmentStructure() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(departmentService.findAllDepartment(DepartmentStructureResponse.class, false));
    }

}
