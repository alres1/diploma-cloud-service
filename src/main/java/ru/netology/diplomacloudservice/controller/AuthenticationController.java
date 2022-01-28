package ru.netology.diplomacloudservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.netology.diplomacloudservice.exceptions.InputDataException;
import ru.netology.diplomacloudservice.exceptions.UnauthorizedException;
import ru.netology.diplomacloudservice.repository.AuthRepository;
import ru.netology.diplomacloudservice.security.JWTUtil;
import ru.netology.diplomacloudservice.security.AuthRequest;
import ru.netology.diplomacloudservice.security.AuthResponse;

@RestController()
@Slf4j
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtTokenUtil;
    private AuthRepository authRepository;

    public AuthenticationController (AuthenticationManager authenticationManager, JWTUtil jwtTokenUtil, AuthRepository authRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authRepository = authRepository;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication;
        if(authRequest.getLogin()==null || authRequest.getPassword()==null){
            log.error("Bad credentials");
            throw new InputDataException("Bad credentials");
        }
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.error("User unauthorized");
            throw new UnauthorizedException("User unauthorized");
        }

        String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        String username = authentication.getName();
        authRepository.putTokenAndUsername(jwt, username);
        log.info("Success authentication user "+username);
        return new AuthResponse(jwt);
    }


    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public ResponseEntity logout(@RequestHeader("auth-token") String authToken) {
        final String token = authToken.substring(7);
        authRepository.removeUserByToken(token);
        log.info("Success logout user");
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
