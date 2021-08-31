package com.logigear.crm.template.controller;

import com.logigear.crm.template.model.User;
import com.logigear.crm.template.payload.UserPayload;
import com.logigear.crm.template.response.UserResponse;
import com.logigear.crm.template.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/template/api/template")
public class TemplateController {

    private final UserService userService;

    @Autowired
    public TemplateController(UserService userService) {
        this.userService = userService;
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @GetMapping()
    public ResponseEntity<?> getUser() {
        List<UserResponse> userResponse = new ArrayList<>();
        HttpHeaders responseHeaders = new HttpHeaders();
        Iterator<User> iterator =userService.findAll().iterator();
        while(iterator.hasNext()){
            userResponse.add(new UserResponse(iterator.next()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(userResponse);
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        UserResponse userResponse = new UserResponse(userService.findById(id));
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(userResponse);
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @PostMapping("{id}")
    public ResponseEntity<?> postUser(@PathVariable("id") Long id,@Valid @RequestBody UserPayload payload) {
        User user = userService.findById(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(user);
    }

    @PostAuthorize("hasAnyRole(@roleConfiguration.getUserRoles())")
    @PostMapping()
    public ResponseEntity<?> postUser(@Valid @RequestBody UserPayload payload) {
        User user = userService.findById(payload.getId());
        HttpHeaders responseHeaders = new HttpHeaders();
        return ResponseEntity.status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(user);
    }
}
