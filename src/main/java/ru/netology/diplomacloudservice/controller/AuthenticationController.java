package ru.netology.diplomacloudservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @Autowired
    private AuthRepository authRepository;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication;
        if(authRequest.getLogin()==null || authRequest.getPassword()==null){
            throw new InputDataException("Bad credentials");
        }
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            //System.out.println(authentication);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("User unauthorized");
        }

        String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        String username = authentication.getName();
        authRepository.putTokenAndUsername(jwt, username);

        return new AuthResponse(jwt);
    }


    // @PostMapping ("/logout") - возвращает 405 ошибку Method Not Allowed
    @RequestMapping(value = {"/logout2"}, method = RequestMethod.POST)
    public ResponseEntity logout(@RequestHeader("auth-token") String authToken) {
        final String token = authToken.substring(7);
        authRepository.removeUserByToken(token);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
