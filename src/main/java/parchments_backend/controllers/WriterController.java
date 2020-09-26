package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.config.JwtTokenUtil;
import parchments_backend.domain.JwtRequest;
import parchments_backend.domain.JwtResponse;
import parchments_backend.domain.UsernameDto;
import parchments_backend.domain.WriterDto;
import parchments_backend.services.WriterService;
import javax.validation.Valid;

@Controller
@RequestMapping(path="/writer")
public class WriterController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WriterService writerService;

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<Object> register(@Valid @RequestBody WriterDto writerDto) {
        UserDetails userDetails = writerService.register(writerDto);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkValidUsername(@RequestBody @Valid UsernameDto usernameDto) throws Exception {
        boolean isValid = writerService.checkValidUsername(usernameDto.getUsername());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = writerService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

