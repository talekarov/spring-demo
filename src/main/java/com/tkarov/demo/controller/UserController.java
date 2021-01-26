package com.tkarov.demo.controller;

import com.google.common.base.Preconditions;
import com.tkarov.demo.dto.request.UserRequestBody;
import com.tkarov.demo.dto.response.UserResponseBody;
import com.tkarov.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "Endpoints for managing users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @ApiOperation(value = "List all users", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of all users"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @GetMapping(value = "/all")
    public List<UserResponseBody> readUsers() {
        return service.getUsers();
    }

    @ApiOperation(value = "Find user by id", response = UserResponseBody.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @GetMapping(value = "/id={id}")
    public UserResponseBody findById(@PathVariable ("id") Long id){
        return service.findById(id);
    }

    @ApiOperation(value = "Register new user", response = UserResponseBody.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully registered"),
            @ApiResponse(code = 201, message = "User successfully created"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @PostMapping(value = "/registration", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseBody createUser(@RequestBody @Valid UserRequestBody userRequestBody){
        Preconditions.checkNotNull(userRequestBody);
        return service.createUser(userRequestBody);
    }

    @ApiOperation(value = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @DeleteMapping(value = "/id={id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable ("id") Long id){
        service.deleteById(id);
    }


    @ApiOperation(value = "Find user by username", response = UserResponseBody.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user by username"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @GetMapping(value = "/un={username}")
    public UserResponseBody getByUsername(@PathVariable ("username") String username) { return service.findByUsername(username); }

    @ApiOperation(value = "Update user by id", response = UserResponseBody.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user by id"),
            @ApiResponse(code = 401, message = "You are not authorized to view this resource"),
            @ApiResponse(code = 403, message = "Access to this resource is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ResponseBody
    @PutMapping(value = "/id={id}", produces = "application/json")
    public UserResponseBody updateUser(@RequestBody @Valid UserRequestBody userRequestBody, @PathVariable ("id") Long id) {
        return service.updateUser(userRequestBody,id);
    }
}