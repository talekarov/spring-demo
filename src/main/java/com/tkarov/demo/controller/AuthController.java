package com.tkarov.demo.controller;

import com.tkarov.demo.auth.JwtToken;
import com.tkarov.demo.auth.JwtUserDetailsService;
import com.tkarov.demo.dto.request.JwtRequest;
import com.tkarov.demo.dto.response.JwtResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/authenticate")
@Api(value = "Manage user authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtToken jwtToken;

    private final JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtToken jwtToken, JwtUserDetailsService jwtUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @ApiOperation(value = "Authenticate user", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authentication successful"),
            @ApiResponse(code = 401, message = "Authentication failed")
    })
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtToken.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String email, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        }

        catch (DisabledException e) {

            throw new Exception("USER_DISABLED", e);
        }

        catch (BadCredentialsException e) {

            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
