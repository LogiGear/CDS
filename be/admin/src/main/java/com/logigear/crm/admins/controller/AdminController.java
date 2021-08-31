package com.logigear.crm.admins.controller;

import com.logigear.crm.admins.model.EmployeeDetails;
import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import com.logigear.crm.admins.payload.UserUpdateRequest;
import com.logigear.crm.admins.response.EmployeeDetailsDTO;
import com.logigear.crm.admins.response.UserResponse;
import com.logigear.crm.admins.service.EmployeeService;
import com.logigear.crm.admins.service.RoleService;
import com.logigear.crm.admins.service.UserService;
import com.logigear.crm.admins.payload.EmployeePayload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/api/admins")
public class AdminController {

    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminController(EmployeeService employeeService, RoleService roleService, UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping("role")
    public ResponseEntity<List<Role>> getRoles() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(roleService.getRoles());
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping("role/{id}")
    public ResponseEntity<List<Role>> getRolesById(@PathVariable("id") Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(roleService.getRoleByUserID(id));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping("user")
    public ResponseEntity<List<User>> getUsers() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(userService.findAll());
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping("user/{id}")
    public ResponseEntity<UserResponse> getUserRoles(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(new UserResponse(user));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @PostMapping("user")
    public ResponseEntity<UserResponse> updateUserRoles(@Valid @RequestBody UserUpdateRequest req) {
        User user = userService.updateUserRoles(req);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(new UserResponse(user));
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping("user_role")
    public ResponseEntity<List<Role>> getUsersbyRole(
            @RequestParam(name = "roleName", required = true) RoleName roleName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(userService.getUserByRole(roleName));
    }

    /**
     * Users must have ADMIN role before using this method. Get all employees.
     *
     * @return A list of current employees.
     */
    @PostAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @GetMapping()
    public ResponseEntity<List<EmployeeDetails>> getAll() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    /**
     * Users must have ADMIN role before using this method. (Partially) update the
     * current employee details.
     *
     * @param req The provided employee partial information.
     * @return Current details of an employee
     */
    @PreAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @PatchMapping(value = "update", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<EmployeeDetails> updateEmployeeDetails(@RequestBody EmployeeDetailsDTO req) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                .body(employeeService.updateEmployeeDetails(req));
    }

    /*
     * @PreAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
     * 
     * @PatchMapping(value = "updatemanager/{id}", produces =
     * {MediaType.APPLICATION_JSON_VALUE}) public ResponseEntity<?>
     * updateEmployeeManager(@RequestBody UpdateManagerRequest
     * req, @PathVariable("id") Long id) { roleService.validRoles(RoleName.MANAGER,
     * req.getManagerEmail()); User u =
     * userService.findUserByEmail(req.getManagerEmail()); HttpHeaders
     * responseHeaders = new HttpHeaders(); return
     * ResponseEntity.status(HttpStatus.OK) .headers(responseHeaders)
     * .body(employeeService.updateManager(u,id)); }
     * 
     * @PreAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
     * 
     * @PatchMapping(value = "updatecdm/{id}", produces =
     * {MediaType.APPLICATION_JSON_VALUE}) public ResponseEntity<?>
     * updateEmployeeManager(@Valid @RequestBody UpdateCdmRequest
     * req, @PathVariable("id") Long id) { roleService.validRoles(RoleName.CDM,
     * req.getCdmEmail()); User u = userService.findUserByEmail(req.getCdmEmail());
     * HttpHeaders responseHeaders = new HttpHeaders(); return
     * ResponseEntity.status(HttpStatus.OK) .headers(responseHeaders)
     * .body(employeeService.updateCdm(u,id)); }
     */

    @PreAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @PatchMapping(value = "manager-project-department/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateManagerProjectDepartmentById(@RequestBody EmployeePayload req,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.updateEmployee(req, id));
    }

    @PreAuthorize("hasAnyRole(@roleConfiguration.getAdminRoles())")
    @PatchMapping(value = "manager-project-department", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateManagerProjectDepartment(@RequestBody EmployeePayload req) {
        return ResponseEntity.ok(employeeService.updateEmployee(req, req.getId()));
    }
}
