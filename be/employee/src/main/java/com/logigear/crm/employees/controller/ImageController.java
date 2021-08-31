package com.logigear.crm.employees.controller;

import com.logigear.crm.employees.response.EmployeeImageResponse;
import com.logigear.crm.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees/api/image")
public class ImageController {
    private final EmployeeService employeeService;

    @Autowired
    public ImageController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * All users must be authenticated before using this method. Get an employee's
     * profile picture based on their provided employee id.
     *
     * @param id The provided employee id
     * @return Image properties represented in image name, image type and base64
     *         encoded image data.
     */
    @PreAuthorize("hasAnyRole(@roleConfiguration.userRoles)")
    @GetMapping(path = { "{id}" })
    public ResponseEntity<String> getImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.getEncodedBase64ImageByEmployeeId(id));
    }

    /**
     * All users must be authenticated before using this method. Update an
     * employee's profile picture based on their provided employee id.
     *
     * @param file The provided picture which is from the client.
     * @param id   The provided employee id associated with the image.
     * @return Image properties represented in image name, image type and base64
     *         encoded image data.
     */
    @PreAuthorize("hasAnyRole(@roleConfiguration.userRoles)")
    @PostMapping()
    // Please response as ResponseEntity not BodyBuilder -> If not, result into 406
    // Not Acceptable
    // Because Spring doesn't know how to parse!
    public ResponseEntity<String> uploadImage(@RequestPart("imageFile") MultipartFile file,
            @RequestParam("id") Long id) {
        employeeService.uploadImageByEmployeeId(file, id);
        return ResponseEntity.ok(employeeService.getEncodedBase64ImageByEmployeeId(id));

        // return ResponseEntity.ok().build();
    }

    /**
     * Users must have ADMIN role before using this method. Get all employees
     * profile images associated with their id.
     *
     * @return The list of EmployeeImageResponse which contains employee id and
     *         base64 encoded image data.
     */
    @PreAuthorize("hasRole(@roleConfiguration.adminRoles)")
    @GetMapping()
    public ResponseEntity<List<EmployeeImageResponse>> getAllEmployeesProfileImage() {
        return ResponseEntity.ok(employeeService.getAllEmployeesProfileImage());
    }
}
